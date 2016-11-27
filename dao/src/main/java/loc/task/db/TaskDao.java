package loc.task.db;

import loc.task.entity.Task;
import org.apache.log4j.Logger;
import org.hibernate.Query;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TaskDao extends BaseDao<Task> {

    private static Logger log = Logger.getLogger(TaskDao.class);

    public Task getTaskToUser(long taskId, int userId) {
        String hql = "SELECT T FROM Task T JOIN T.personList U WHERE T.taskId =:taskId AND U.userId=:userId";
        Query query = session.createQuery(hql);
        query.setParameter("taskId", taskId);
        query.setParameter("userId", userId);
        Task result = (Task) query.uniqueResult();
        return result;
    }


//              статусы важные руководителю: 2, 4, 5
//            1(1) - Создано (новый) - еще можно редактировать. <br/>
//            2(1) - На утверждении - ждет утвеждения начальника, для начала работы<br/>
//            3(2) - В работе, подчиненный выполняет задание<br/>
//            4(1) - На проверкe - задание передано на проверку руководителю для назначения времени приема<br/>
//            5(2) - К сдаче - назначено время приема<br/>
//            6(2) - Выполнено - задача закрывается и уходит в архив<br/>
//                 добавить на UI -  7 удалена

    public Integer setStatus(long taskId, int statusId) {
//        Session session = util.getSession();
        String hql = "UPDATE FROM Task T SET T.statusId = :statusId WHERE T.taskId =:taskId";
        System.out.println(hql);
        Query query = session.createQuery(hql);
        query.setParameter("statusId", statusId);
        query.setParameter("taskId", taskId);

        Integer result = query.executeUpdate();
        System.out.println("count results: " + result);
        return result;
    }


    public List<Object[]> getCurrentTask() {
        HashSet<Integer> includeStatus = new HashSet<Integer>();
        includeStatus.add(2);
        includeStatus.add(4);
        includeStatus.add(5);
        return getCurrentTask(includeStatus);
    }

    public List<Object[]> getCurrentTask(HashSet<Integer> includeStatus) {
        if (includeStatus.isEmpty()) {
            includeStatus.add(2);
            includeStatus.add(4);
        }
        int page = 1;
        return getCurrentTask(page, includeStatus);
    }

    public List<Object[]> getCurrentTask(int page, HashSet<Integer> includeStatus) {
        int tasksPerPage = 10;
        return getCurrentTask(page, tasksPerPage, includeStatus);
    }

    public List<Object[]> getCurrentTask(int page, int tasksPerPage, HashSet<Integer> includeStatus) {
        long totalCount = getCountTask(includeStatus);
        return getCurrentTask(page, tasksPerPage, totalCount, includeStatus);
    }

    public List<Object[]> getCurrentTask(int page, int tasksPerPage, long totalCount, HashSet<Integer> includeStatus) {
        int sort = 1;
        return getCurrentTask(page, tasksPerPage, totalCount, includeStatus, sort);
    }

    public List<Object[]> getCurrentTask(int page, int tasksPerPage, long totalCount, HashSet<Integer> includeStatus, int sort) {
        boolean ask = false;
        return getCurrentTask(page, tasksPerPage, totalCount, includeStatus, sort, ask);
    }

    public List<Object[]> getCurrentTask(int page, int tasksPerPage, long totalCount,
                                         HashSet<Integer> includeStatus, int sort, boolean ask) {
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
//        Session session = util.getSession();
        String hql = "SELECT DISTINCT T.dateCreation , T.taskId,T.statusId , U.login FROM Task T JOIN T.personList U WHERE T.statusId IN (:statusId)";
        hql = hql.concat(getSorting(sort, ask));
        System.out.println(hql);
        Query query = session.createQuery(hql);
//        query.getQueryString().join(" ORDER BY T.statusId");
        query.setCacheable(true);
        query.setCacheRegion("task");
        query.setParameterList("statusId", includeStatus);
        query.setFirstResult(firstResult);
        query.setMaxResults(tasksPerPage);
        List<Object[]> results = query.list();
        return results;
    }



    public List<Task> getTasks(int page, int tasksPerPage, long totalCount,
                                         Set<Integer> includeStatus, int sort, boolean ask) {
        long countPage = totalCount / (long) tasksPerPage;
        if (totalCount % (long) tasksPerPage > 0) {
            countPage++;
        }
        if (page > (int) countPage) {
            page = (int) countPage;
        }else if(page<=0){page=1;}

        System.out.println("countPage:" + countPage);
        System.out.println("tasksPerPage:" + tasksPerPage);
        System.out.println("Page:" + page);

        int firstResult = (page - 1) * tasksPerPage;

//        Session session = util.getSession();
        String hql = "SELECT DISTINCT T FROM Task T JOIN T.personList U WHERE T.statusId IN (:statusId)";
        hql = hql.concat(getSorting(sort, ask));

        System.out.println(hql);

        Query query = session.createQuery(hql);
//        query.getQueryString().join(" ORDER BY T.statusId");
        query.setCacheable(true);
        query.setCacheRegion("task");
        query.setParameterList("statusId", includeStatus);
        query.setFirstResult(firstResult);
        query.setMaxResults(tasksPerPage);
        List<Task> results = query.list();
        return results;
    }

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
            default:
                break;
        }
        if (!ask) {
            sorting = sorting.append(" DESC");
        }
//        sorting = sorting.append(" ORDER BY T.dateCreation ASC");
        return sorting.toString();
    }


    public long getCountTask(Set<Integer> includeStatus) {
        if (includeStatus.isEmpty()) {
            includeStatus.add(2);
            includeStatus.add(4);
        }
        String hql = "SELECT COUNT (T) FROM Task T WHERE T.statusId IN (:statusId)";
        System.out.println(hql);
//        Session session = util.getSession();
        Query query = session.createQuery(hql);
        query.setCacheable(true);
        query.setCacheRegion("task");
        query.setParameterList("statusId", includeStatus);
        Long result = (Long) query.uniqueResult();
        System.out.println("count results: " + result);
        return result;
    }

    //TODO объединить
    public long getCountTask(Set<Integer> includeStatus, int userId) {
        if (includeStatus.isEmpty()) {
            includeStatus.add(2);
            includeStatus.add(4);
        }
        System.out.println(includeStatus);

//        SELECT COUNT(*) FROM `user` WHERE  `user`.`user_id` IN (2, 3) AND `user`.`F_accountStatus` IN (1);
//        String hql = "SELECT COUNT (T) FROM Task T JOIN User U WHERE T.statusId IN (:statusId)";
        String hql = "SELECT DISTINCT COUNT (T) FROM Task T JOIN T.personList U WHERE T.statusId IN (:statusId) AND U.userId IN(:userId)";
//        String hql = "SELECT COUNT (T) FROM Task T JOIN User U WHERE T.statusId IN (:statusId) AND U.userId IN(:userId)";
        System.out.println(hql);
//        Session session = util.getSession();
        Query query = session.createQuery(hql);
        query.setCacheable(true);
        query.setCacheRegion("task");
        query.setParameterList("statusId", includeStatus);
        query.setParameter("userId", userId);
        Long result = (Long) query.uniqueResult();
        System.out.println("count results: " + result);
        return result;
    }

    public List<Task> getCurrentTaskUser() {
        HashSet<Integer> includeStatus = new HashSet<Integer>();
        includeStatus.add(2);
        includeStatus.add(4);
        includeStatus.add(5);
        return getCurrentTaskUser(includeStatus);
    }

    public List<Task> getCurrentTaskUser(Set<Integer> includeStatus) {
        int page = 1;
        return getCurrentTaskUser(page, includeStatus);
    }

    public List<Task> getCurrentTaskUser(int page, Set<Integer> includeStatus) {
        int tasksPerPage = 10;
        return getCurrentTaskUser(page, tasksPerPage, includeStatus);
    }

    public List<Task> getCurrentTaskUser(int page, int tasksPerPage, Set<Integer> includeStatus) {
        long totalCount = getCountTask(includeStatus);
        return getCurrentTaskUser(page, tasksPerPage, totalCount, includeStatus);
    }
//TODO нужна?
    public List<Task> getCurrentTaskUser(int page, int tasksPerPage, long totalCount, Set<Integer> includeStatus) {
        HashSet<Integer> userId = new HashSet<Integer>(); //пустая коллекция юзеров
        return getCurrentTaskUser(page, tasksPerPage, totalCount, includeStatus, userId);
    }

    public List<Task> getCurrentTaskUser(int page, int tasksPerPage, long totalCount, Set<Integer> includeStatus, Set<Integer> userId) {

        long countPage = totalCount / (long) tasksPerPage;
        if (totalCount % (long) tasksPerPage > 0) {
            countPage++;
        }
        if (page > (int) countPage) {
            page = (int) countPage;
        }

        System.out.println("countPage:" + countPage);
        System.out.println("Page:" + page);

        int firstResult = (page - 1) * tasksPerPage;
//        Session session = util.getSession();
        String hql = "SELECT DISTINCT T FROM Task T JOIN T.personList U WHERE T.statusId IN (:statusId) AND U.userId IN(:userId)";
//        hql = hql.concat(getSorting(sort, ask));

        Query query = session.createQuery(hql);
        query.setCacheable(true);
        query.setCacheRegion("task");

        query.setParameterList("statusId", includeStatus);
        query.setParameterList("userId", userId);
        query.setFirstResult(firstResult);
        query.setMaxResults(tasksPerPage);
        List<Task> results = query.list();
        return results;
    }

}
