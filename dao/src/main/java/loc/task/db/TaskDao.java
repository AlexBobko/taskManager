package loc.task.db;

import loc.task.entity.Task;
import lombok.extern.log4j.Log4j;
import org.hibernate.Query;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
@Log4j
public class TaskDao extends BaseDao<Task> {

//    private static Logger log = Logger.getLogger(TaskDao.class);
//TODO уникальный заголовок?

    public Task getTaskToUser(long taskId, int userId) {
        String hql = "SELECT T FROM Task T JOIN T.userList U WHERE T.taskId =:taskId AND U.userId=:userId";
        Query query = session.createQuery(hql);
        query.setParameter("taskId", taskId);
        query.setParameter("userId", userId);
        Task result = (Task) query.uniqueResult();
        return result;
    }

    public Integer setStatus(long taskId, int statusId) {
        String hql = "UPDATE FROM Task T SET T.statusId = :statusId WHERE T.taskId =:taskId";
        System.out.println(hql);
        Query query = session.createQuery(hql);
        query.setParameter("statusId", statusId);
        query.setParameter("taskId", taskId);

        Integer result = query.executeUpdate();
        System.out.println("count results: " + result);
        return result;
    }

    public List<Object[]> getCurrentTask(HashSet<Integer> includeStatus) {
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

    public List<Task> getTasks(int page, int tasksPerPage, long totalCount,
                               Set<Integer> includeStatus, int sort, boolean ask) {
        Set<Integer> usersId = null;
        return getTasks(page, tasksPerPage, totalCount,includeStatus, sort, ask, usersId);
    }
    public List<Task> getTasks(int page, int tasksPerPage, long totalCount,
                               Set<Integer> includeStatus, int sort, boolean ask, Integer userId) {
        Set<Integer> usersId=new HashSet<>(1);
        usersId.add(userId);
        return getTasks(page, tasksPerPage, totalCount,includeStatus, sort, ask, usersId);
    }

    //РАБОЧИЙ ВАРИАНТ
    public List<Task> getTasks(int page, int tasksPerPage, long totalCount,
                                         Set<Integer> includeStatus, int sort, boolean ask, Set<Integer> usersId) {
        long countPage = totalCount / (long) tasksPerPage;
        if (totalCount % (long) tasksPerPage > 0) {
            countPage++;
        }
        if (page > (int) countPage) {
            page = (int) countPage;
        }else if(page<=0){page=1;}
        System.out.println("getTasks:");
        System.out.println("countPage:" + countPage);
        System.out.println("tasksPerPage:" + tasksPerPage);
        System.out.println("Page:" + page);
        int firstResult = (page - 1) * tasksPerPage;
//        Session session = util.getSession();
        String hql;
        if (usersId!=null) {
            hql = "SELECT DISTINCT T FROM Task T JOIN T.userList U WHERE T.statusId IN (:statusId) AND U.userId IN(:userId)";
        }else {
            hql = "SELECT DISTINCT T FROM Task T JOIN T.userList U WHERE T.statusId IN (:statusId)";
        }
        System.out.println("1:"+hql);
        hql = hql.concat(getSorting(sort, ask));
        System.out.println(hql);
        Query query = session.createQuery(hql);
        if (usersId!=null) {
            query.setParameterList("userId", usersId);
        }

//        query.setCacheable(true);
//        query.setCacheRegion("task");
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
            case 5:
                sorting = sorting.append(" ORDER BY T.title");
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
        String hql = "SELECT COUNT (T) FROM Task T WHERE T.statusId IN (:statusId)";
        System.out.println(hql);

        Query query = session.createQuery(hql);
        //TODO кэш оключил
//        query.setCacheable(true);
//        query.setCacheRegion("task");
        query.setParameterList("statusId", includeStatus);
        Long result = (Long) query.uniqueResult();
        System.out.println("count results: " + result);
        return result;
    }

    //TODO объединить
    public long getCountTask(Set<Integer> includeStatus, int userId) {
        System.out.println("includeStatus: " + includeStatus);
        String hql = "SELECT DISTINCT COUNT (T) FROM Task T JOIN T.userList U WHERE T.statusId IN (:statusId) AND U.userId IN(:userId)";
        System.out.println(hql);
        Query query = session.createQuery(hql);
//        query.setCacheable(true);
//        query.setCacheRegion("task");
        query.setParameterList("statusId", includeStatus);
        query.setParameter("userId", userId);
        Long result = (Long) query.uniqueResult();
        System.out.println("count results: " + result);
        return result;
    }

}