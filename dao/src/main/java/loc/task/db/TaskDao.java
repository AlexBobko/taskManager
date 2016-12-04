package loc.task.db;

import loc.task.db.exceptions.DaoException;
import loc.task.entity.Task;
import loc.task.util.HibernateUtil;
import lombok.extern.log4j.Log4j;
import org.hibernate.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

//TODO (ТЗ) уникальный заголовок таска нужен?

@Log4j
public class TaskDao extends BaseDao<Task> {
    private static TaskDao taskDao = null;

    private TaskDao() {
        log.info("SINGLE TONE: create new TaskDao()");
    }

    private static synchronized TaskDao getInstance() {
        if (taskDao == null) {
            taskDao = new TaskDao();
        }
        return taskDao;
    }

    public static TaskDao getTaskDao() {
        if (taskDao == null) {
            return getInstance();
        }
        return taskDao;
    }

    public Task getTaskToUser(long taskId, int userId) throws DaoException {
        try {
            Session session = HibernateUtil.getHibernateUtil().getSession();
            String hql = "SELECT T FROM Task T JOIN T.userList U WHERE T.taskId =:taskId AND U.userId=:userId";
            Query query = session.createQuery(hql);
            query.setParameter("taskId", taskId);
            query.setParameter("userId", userId);
            Task result = (Task) query.uniqueResult();
            return result;
        } catch (NonUniqueResultException e) {
            log.error("Error getTaskToUser " + " in Dao" + e);
            throw new DaoException(e);
        }
    }

    public List<Task> getTasks(int page, int tasksPerPage, long totalCount,Set<Integer> includeStatus,
                               int sort, boolean ask) throws DaoException {
        Set<Integer> usersId = null;
        return getTasks(page, tasksPerPage, totalCount, includeStatus, sort, ask, usersId);
    }

    public List<Task> getTasks(int page, int tasksPerPage, long totalCount, Set<Integer> includeStatus,
                               int sort, boolean ask, Integer userId) throws DaoException {
        Set<Integer> usersId = new HashSet<>(1);
        usersId.add(userId);
        return getTasks(page, tasksPerPage, totalCount, includeStatus, sort, ask, usersId);
    }

    public List<Task> getTasks(int page, int tasksPerPage, long totalCount, Set<Integer> includeStatus,
                               int sort, boolean ask, Set<Integer> usersId) throws DaoException {
        try {
            Session session = HibernateUtil.getHibernateUtil().getSession();
            long countPage = totalCount / (long) tasksPerPage;
            if (totalCount % (long) tasksPerPage > 0) {
                countPage++;
            }
            if (page > (int) countPage) {
                page = (int) countPage;
            } else if (page <= 0) {
                page = 1;
            }
            System.out.println("getTasks:");
            System.out.println("countPage:" + countPage);
            System.out.println("tasksPerPage:" + tasksPerPage);
            System.out.println("Page:" + page);
            int firstResult = (page - 1) * tasksPerPage;
            String hql;
            if (usersId != null) {
                hql = "SELECT DISTINCT T FROM Task T JOIN T.userList U WHERE T.statusId IN (:statusId) AND U.userId IN(:userId)";
            } else {
                hql = "SELECT DISTINCT T FROM Task T JOIN T.userList U WHERE T.statusId IN (:statusId)";
            }
            System.out.println("1:" + hql);
            hql = hql.concat(getSorting(sort, ask));
            System.out.println(hql);
            Query query = session.createQuery(hql);
            if (usersId != null) {
                query.setParameterList("userId", usersId);
            }
            //TODO ?? кэширование тасков при учете версионности
            query.setCacheable(true);
            query.setCacheRegion("task");
            query.setParameterList("statusId", includeStatus);
            query.setFirstResult(firstResult);
            query.setMaxResults(tasksPerPage);
            List<Task> results = query.list();
            return results;
        } catch (HibernateException e) {
            log.error("Error getTasks " + " in Dao" + e);
            throw new DaoException(e);
        }
    }

    //TODO (ТЗ) добавить сортировку по дате сдачи + выборку тасков между датами ?
    private String getSorting(int sort, boolean ask) {
        StringBuffer sorting = new StringBuffer();
        switch (sort) {
            case 1:
                sorting = sorting.append(" ORDER BY T.dateCreation");
                break;
            case 2:
                sorting = sorting.append(" ORDER BY T.taskId");
                break;
            case 3:
                sorting = sorting.append(" ORDER BY T.statusId");
                break;
            case 4:
                sorting = sorting.append(" ORDER BY U.login");
                break;
            case 5:
                sorting = sorting.append(" ORDER BY T.title");
                break;
            default:
                break;
        }
        if (!ask) {
            sorting = sorting.append(" DESC");
        }
        return sorting.toString();
    }

    //TODO (ТЗ) количество тасков по статусу, при кэшировании - влияние на количество страниц кртично ли?? решить
    //TODO ?? ошибка проверки на null если в сигнатуре метода Integ а передаем int проверить
    public long getCountTask(Set<Integer> includeStatus, Integer userId) throws DaoException {
        try {
            Session session = HibernateUtil.getHibernateUtil().getSession();
            System.out.println("GetCount includeStatus: " + includeStatus + "userId:" + userId); //TODO LOG
            String hql = "SELECT DISTINCT COUNT (T) FROM Task T JOIN T.userList U WHERE T.statusId IN (:statusId) AND U.userId IN(:userId)";
            Query query = session.createQuery(hql);
            System.out.println(hql);
            query.setCacheable(true); //TODO КЭШ +
            query.setCacheRegion("task");
            query.setParameterList("statusId", includeStatus);
            query.setParameter("userId", userId);
            Long result = (Long) query.uniqueResult();
            System.out.println("getCountTask: " + result);
            return result;
        } catch (NonUniqueResultException e) {
            log.error("Error getTaskToUser " + " in Dao" + e);
            throw new DaoException(e);
        }
    }

    public long getCountTask(Set<Integer> includeStatus) throws DaoException {
        try {
            Session session = HibernateUtil.getHibernateUtil().getSession();
            String hql = "SELECT COUNT (T) FROM Task T WHERE T.statusId IN (:statusId)";
            System.out.println(hql);
            Query query = session.createQuery(hql);
            query.setCacheable(true);//TODO КЭШ +
            query.setCacheRegion("task");
            query.setParameterList("statusId", includeStatus);
            Long result = (Long) query.uniqueResult();
            System.out.println("getCountTask: " + result);
            return result;
        } catch (NonUniqueResultException e) {
            log.error("Error getTaskToUser " + " in Dao" + e);
            throw new DaoException(e);
        }
    }

    //TODO ?? имеет ли смысл сделать отдельные ВО или проще пользователей через поле таска получать (оптимизация)
    public List<Object[]> getCurrentTask(int page, int tasksPerPage, long totalCount,
                                         HashSet<Integer> includeStatus, int sort, boolean ask) {
        Session session = HibernateUtil.getHibernateUtil().getSession();
        long countPage = totalCount / (long) tasksPerPage;
        if (totalCount % (long) tasksPerPage > 0) {
            countPage++;
        }
        if (page > (int) countPage) {
            page = (int) countPage;
        }
        System.out.println("countPage:" + countPage);
        System.out.println("tasksPerPage:" + tasksPerPage);
        System.out.println("Page:" + page);
        int firstResult = (page - 1) * tasksPerPage;
        String hql = "SELECT DISTINCT T.dateCreation , T.taskId,T.statusId , U.login FROM Task T JOIN T.userList U WHERE T.statusId IN (:statusId)";
        hql = hql.concat(getSorting(sort, ask));
        System.out.println(hql);
        Query query = session.createQuery(hql);
//        query.getQueryString().join(" ORDER BY T.statusId");
//        query.setCacheable(true);
//        query.setCacheRegion("task");
        query.setParameterList("statusId", includeStatus);
        query.setFirstResult(firstResult);
        query.setMaxResults(tasksPerPage);
        List<Object[]> results = query.list();
        return results;
    }
    public void replicate(Task task) throws DaoException {
        try {
            Session session = HibernateUtil.getHibernateUtil().getSession();
            session.replicate(task, ReplicationMode.LATEST_VERSION);
            log.info("replicate task:" + task.getTaskId());
        } catch (HibernateException e) {
            log.error("Error replicate task" + " in Dao" + e);
            throw new DaoException(e);
        }

    }


    //TODO использовать для удаления или удалять общими средствами
//    public Integer setStatus(long taskId, int statusId) {
//        Session session = HibernateUtil.getHibernateUtil().getSession();
//        String hql = "UPDATE FROM Task T SET T.statusId = :statusId WHERE T.taskId =:taskId";
//        System.out.println(hql);
//        Query query = session.createQuery(hql);
//        query.setParameter("statusId", statusId);
//        query.setParameter("taskId", taskId);
//
//        Integer result = query.executeUpdate();
//        System.out.println("count results: " + result);
//        return result;
//
//    }

}