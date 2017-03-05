package loc.task.dao;

import loc.task.entity.Task;
import lombok.extern.log4j.Log4j;
import org.hibernate.Query;
import org.hibernate.ReplicationMode;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

//TODO (ТЗ) уникальный заголовок таска нужен?

@Log4j
@Repository()
public class TaskDao extends BaseDao<Task> implements ITaskDao {
    @Autowired
    public TaskDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public void replicate(Task task) {
        getSession().replicate(task, ReplicationMode.LATEST_VERSION);
        log.info("replicate task:" + task.getTaskId());
    }

    //TODO (ТЗ) количество тасков по статусу, при кэшировании - влияние на количество страниц критично ли?? решить
    @Override
    public long getCountTask(Set<Integer> includeStatus, Integer userId) {
//            System.out.println("GetCount includeStatus: " + includeStatus + "userId:" + userId); //TODO LOG
        String hql = "SELECT DISTINCT COUNT (T) FROM Task T JOIN T.userList U WHERE T.statusId IN (:statusId) AND U.userId IN(:userId)";
        Query query = getSession().createQuery(hql);
        System.out.println(hql);
        query.setCacheable(true); //TODO КЭШ +
        query.setCacheRegion("task");
        query.setParameterList("statusId", includeStatus);
        query.setParameter("userId", userId);
//            log.info("GetCount includeStatus: " + includeStatus + "userId:" + userId);
        return (Long) query.uniqueResult();
    }

    @Override
    public long getCountTask(Set<Integer> includeStatus) {
        String hql = "SELECT COUNT (T) FROM Task T WHERE T.statusId IN (:statusId)";
//            System.out.println(hql);
        Query query = getSession().createQuery(hql);
        query.setCacheable(true);//TODO КЭШ +
        query.setCacheRegion("task");
        query.setParameterList("statusId", includeStatus);
        return (Long) query.uniqueResult();
    }

    @Override
    public Task getTaskToUser(long taskId, int userId) {
        String hql = "SELECT T FROM Task T JOIN T.userList U WHERE T.taskId =:taskId AND U.userId=:userId";
        Query query = getSession().createQuery(hql);
        query.setParameter("taskId", taskId);
        query.setParameter("userId", userId);
        return (Task) query.uniqueResult();
    }

    @Override
    public List<Task> getTasks(int firstResult, int tasksPerPage, Set<Integer> includeStatus,
                               int sort, boolean ask, Set<Integer> usersId) {
        String hql;
        if (usersId != null) {
            hql = "SELECT DISTINCT T FROM Task T JOIN T.userList U WHERE T.statusId IN (:statusId) AND U.userId IN(:userId)";
        } else {
            hql = "SELECT DISTINCT T FROM Task T JOIN T.userList U WHERE T.statusId IN (:statusId)";
        }
        hql = hql.concat(getSorting(sort, ask));
//            System.out.println(hql);
        Query query = getSession().createQuery(hql);
        if (usersId != null) {
            query.setParameterList("userId", usersId);
        }
        query.setCacheable(true);
        query.setCacheRegion("task");
        query.setParameterList("statusId", includeStatus);
        query.setFirstResult(firstResult);
        query.setMaxResults(tasksPerPage);
        return query.list();
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
            case 6:
                sorting = sorting.append(" ORDER BY T.dateReporting"); //date_reporting
                break;
            default:
                break;
        }
        if (!ask) {
            sorting = sorting.append(" DESC");
        }
        return sorting.toString();
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

//    public List<Object[]> getCurrentTask(int page, int tasksPerPage, long totalCount,
//                                         HashSet<Integer> includeStatus, int sort, boolean ask) {
//        Session session = HibernateUtil.getHibernateUtil().getSession();
//        long countPage = totalCount / (long) tasksPerPage;
//        if (totalCount % (long) tasksPerPage > 0) {
//            countPage++;
//        }
//        if (page > (int) countPage) {
//            page = (int) countPage;
//        }
//        System.out.println("countPage:" + countPage);
//        System.out.println("tasksPerPage:" + tasksPerPage);
//        System.out.println("Page:" + page);
//        int firstResult = (page - 1) * tasksPerPage;
//        String hql = "SELECT DISTINCT T.dateCreation , T.taskId,T.statusId , U.login FROM Task T JOIN T.userList U WHERE T.statusId IN (:statusId)";
//        hql = hql.concat(getSorting(sort, ask));
//        System.out.println(hql);
//        Query query = session.createQuery(hql);
////        query.getQueryString().join(" ORDER BY T.statusId");
////        query.setCacheable(true);
////        query.setCacheRegion("task");
//        query.setParameterList("statusId", includeStatus);
//        query.setFirstResult(firstResult);
//        query.setMaxResults(tasksPerPage);
//        List<Object[]> results = query.list();
//        return results;
//    }
}