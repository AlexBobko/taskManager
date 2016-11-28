package service;

import loc.task.db.BaseDao;
import loc.task.db.TaskDao;
import loc.task.db.exceptions.DaoException;
import loc.task.entity.Task;
import loc.task.entity.TaskContent;
import loc.task.entity.User;
import loc.task.util.HibernateUtil;
import loc.task.vo.Account;
import loc.task.vo.TaskOutFilter;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.text.SimpleDateFormat;
import java.util.*;


public class TaskService {
    private static Logger log = Logger.getLogger(TaskService.class); //TODO log add e  log.error(e, e);
    private static TaskDao taskDao = null;
    private static BaseDao baseDao = null;

    final static Integer defaultStatusTask = 1;
    //    private Transaction transaction = null;
    //    Session session = HibernateUtil.getHibernateUtil().getSession();

    private TaskService() {
    }

    public static Account updateTaskList(Account ac) {
        //обновить количество постов
        if (ac.getUser().getRole() == UserService.employeeRole) {
            updateTaskOutFilter(ac.getCurrentTasksFilter(), ac.getUser().getUserId());

            ac.setCurrentTasks(getTasksList(ac.getCurrentTasksFilter(), ac.getUser().getUserId()));

        } else if (ac.getUser().getRole() == UserService.superiorRole) {
            updateTaskOutFilter(ac.getCurrentTasksFilter(), null);
            ac.setCurrentTasks(getTasksList(ac.getCurrentTasksFilter()));
        }
        return ac;
    }

    public static List<Task> getTasksList(TaskOutFilter f, Integer userId) {
        Set<Integer> usersId = new HashSet<>(1);
        usersId.add(userId);
        ArrayList<Task> userList = (ArrayList<Task>) getTaskDao().getTasks(f.getPage(), f.getTasksPerPage(), f.getTotalCount(), f.getIncludeStatus(), f.getSort(), f.isAsk(),usersId);
//        for (Task task:userList ) {
//            System.out.println(task.getTaskId());
//        }
        return userList;
    }

    public static List<Task> getTasksList(TaskOutFilter f) {
        return getTaskDao().getTasks(f.getPage(), f.getTasksPerPage(), f.getTotalCount(), f.getIncludeStatus(), f.getSort(), f.isAsk());
    }

    public static long getCountTask(Set<Integer> includeStatus, int userId) {

        return getTaskDao().getCountTask(includeStatus, userId);
    }

    public static Task addNewTask(Account account, int employeeId,
                                  String titleTask, String bodyTask, String strTaskDeadline) throws DaoException {
        Date taskDeadline = convertDate(strTaskDeadline);
        Set<User> users = new HashSet<>();
        if (employeeId == 0) {
            users.add(account.getUser());
        } else {
            User user = UserService.getUser(employeeId);
            users.add(user);
        }
        TaskContent content = new TaskContent(bodyTask);
        Task newTask = new Task(defaultStatusTask, new Date(), titleTask, taskDeadline, content, users);
        getTaskDao().saveOrUpdate(newTask);
        return newTask;
    }

    public static Date convertDate(String postDate) {
        String[] dl = postDate.split("/");
        int year = Integer.parseInt(dl[2]);
        int month = Integer.parseInt(dl[0]);
        int dayOfMonth = Integer.parseInt(dl[1]);
        int defaultHour = 20;
        int defaultMinute = 15;
        Date bodyDeadline = new Date(year, month, dayOfMonth, defaultHour, defaultMinute);
        return bodyDeadline;
    }

    public static boolean updateTask(Account account, long taskId, Integer status) {
        Session session = HibernateUtil.getHibernateUtil().getSession();
        Transaction transaction = session.beginTransaction();
        boolean b = false;
        Task task;
        try {
            if (account.getUser().getRole() == UserService.employeeRole) {
                task = getTaskDao().getTaskToUser(taskId, account.getUser().getUserId());

                System.out.println("TASK STATUS UPDATE&??" + task.getTaskId() + " new status: " + task.getStatusId());
            } else {
                task = getTaskDao().get(taskId);
            }
            SimpleDateFormat dateFormat = account.getDateFormat();
            task.setStatusId(status);

            Calendar calendar = Calendar.getInstance();
            String history = task.getContent().getHistory();
            history = history.concat(dateFormat.format(calendar.getTime())).concat("~status:").
                    concat(status.toString()).concat("~~");
            task.getContent().setHistory(history);
//            getBaseDao().saveOrUpdate(task.getContent());
            System.out.println("TASK UPDATE 3 " + session.getStatistics() + ": " + task.getTaskId() + " new status: " + task.getStatusId());

//            getTaskDao().saveOrUpdate(task);

            transaction.commit();

            System.out.println("TASK UPDATE 4 " + session.getStatistics() + ": " + task.getTaskId() + " new status: " + task.getStatusId());
//            account.getCurrentTasks().put(task.getTaskId(), task);
            System.out.println("TASK UPDATE 5 " + session.getStatistics() + ": " + task.getTaskId() + " new status: " + task.getStatusId());
            session.close();
            b = true;
        } catch (DaoException e) {
            transaction.rollback();
            session.close();
            log.error(e, e);
        }
        return b;
    }

    public static Task getTask(Account account, Long taskId) {
        User user = account.getUser();
        int role = user.getRole();
        Task task = null;
        if (role == 1) {
            System.out.println(user.getUserId());
            task = getTaskDao().getTaskToUser(taskId, user.getUserId());
        } else {
            try {
                task = getTaskDao().get(taskId);
            } catch (DaoException e) {
                log.error(e, e);
            }
        }
        return task;
    }

    //    taskService.updateTaskBody(task,newStatus,bodyTask,account)
    public static boolean updateTaskBody(Account account, Task task, String bodyTask, Integer newStatus) {
        Session session = HibernateUtil.getHibernateUtil().getSession();
        Transaction transaction = session.beginTransaction();
//        session.clear();
        System.out.println(bodyTask);
        SimpleDateFormat dateFormat = account.getDateFormat();
        boolean b;

        try {
//            getTaskDao().refresh(task);
//            task = getTaskDao().get(task.getTaskId());
            session.refresh(task);
//            session.merge(task);
            int userId = account.getUser().getUserId();
            task.setStatusId(newStatus);// устанавливаем статус на проверке
            session.flush();

            String currentTaskBody = task.getContent().getBody();
            Calendar calendar = Calendar.getInstance();
            currentTaskBody = currentTaskBody.concat("\n\r").concat(dateFormat.format(calendar.getTime())).concat(" user:" + userId).concat(bodyTask);
            task.getContent().setBody(currentTaskBody);

            System.out.println("currentTaskBody " + currentTaskBody);

            StringBuffer history = new StringBuffer(task.getContent().getHistory());
            history = history.append(dateFormat.format(calendar.getTime())).append("~status:").append("~korrect body").append(newStatus).append("~~");
            task.getContent().setHistory(currentTaskBody);
            task.getContent().setHistory(history.toString());
//            session.clear();
            System.out.println("stat:1 " + session.getStatistics() + ": ");
//            getTaskDao().saveOrUpdate(task);
            session.flush();
            System.out.println("stat:2 " + session.getStatistics() + ": ");
//            session.flush();
            if (userId == -1) {
                throw new DaoException(new Exception());
            }
//            account.getCurrentTasks().put(task.getTaskId(), task); //обновление в обход кэша?
            transaction.commit();
            b = true;
        } catch (DaoException e) {
            log.error(e, e);
            transaction.rollback();
            b = false;
        } finally {
            session.close();
        }
        return b;
    }


    public static TaskOutFilter getTaskOutFilter(Set<Integer> includeStatus) {
        return getTaskOutFilter(includeStatus, null);
    }

    public static TaskOutFilter getTaskOutFilter(Set<Integer> includeStatus, Integer userId) {
        TaskOutFilter taskOutFilter = new TaskOutFilter(includeStatus);
        return updateTaskOutFilter(taskOutFilter, userId);
    }

    public static TaskOutFilter updateTaskOutFilter(TaskOutFilter taskOutFilter, Integer userId) {
        long totalCount = 1L;
        if (userId != null) {
            totalCount = TaskService.getCountTask(taskOutFilter.getIncludeStatus(), userId);
        } else {
            totalCount = getTaskDao().getCountTask(taskOutFilter.getIncludeStatus());
        }
        long countPage = totalCount / (long) taskOutFilter.getTasksPerPage();
        if (totalCount % (long) taskOutFilter.getTasksPerPage() > 0) {
            countPage++;
        }
        taskOutFilter.setTotalCount(totalCount);
        taskOutFilter.setCountPage(countPage);
        return taskOutFilter;
    }

    public static TaskDao getTaskDao() {

        if (taskDao == null) {
            taskDao = new TaskDao();
        }
        return taskDao;
    }

    public static BaseDao getBaseDao() {

        if (baseDao == null) {
            baseDao = new BaseDao();
        }
        return baseDao;
    }
}
