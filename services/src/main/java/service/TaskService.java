package service;

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
    final private Integer employeeRole = 1;
    final private Integer superiorRole = 2;
    final private Integer defaultStatusTask = 1;
    private Transaction transaction = null;

    Session session = HibernateUtil.getHibernateUtil().getSession();

    public TaskService() {

    }

    //TODO на каком этапе проверять права на операцию, например пагинации
    public Account updateTaskList(Account ac) {
        if (ac.getUser().getRole() == employeeRole) {
            ac.setCurrentTasks(getTasksList(ac.getCurrentTasksFilter(), ac.getUser().getUserId()));


        } else if (ac.getUser().getRole() == superiorRole) {
            ac.setCurrentTasks(getTasksList(ac.getCurrentTasksFilter()));
        }
        return ac;
    }

    public Map<Long, Task> getTasksList(TaskOutFilter f, Integer userId) {
        Set<Integer> usersId = new HashSet<>(1);
        usersId.add(userId);
        List<Task> tasksList = getTaskDao().getCurrentTaskUser(f.getPage(), f.getTasksPerPage(), f.getTotalCount(), f.getIncludeStatus(), usersId);
        Map<Long, Task> currentTasks = new HashMap(f.getTasksPerPage());
        for (Task task : tasksList) {
            currentTasks.put(task.getTaskId(), task);

            System.out.println("task.getTaskId "+task.getTaskId());
        }
        return currentTasks;
    }

    public Map<Long,Task> getTasksList(TaskOutFilter f) {
        List<Task> tasksList =getTaskDao().getTasks(f.getPage(), f.getTasksPerPage(), f.getTotalCount(), f.getIncludeStatus(), f.getSort(), f.isAsk());
        Map<Long, Task> currentTasks = new HashMap(f.getTasksPerPage());
        for (Task task : tasksList) {
            currentTasks.put(task.getTaskId(), task);
        }
        return currentTasks;
    }

    public static long getCountTask(Set<Integer> includeStatus, int userId) {

        return getTaskDao().getCountTask(includeStatus, userId);
    }


    public Task addNewTask(Account account, int responsiblePersonId,
                           String titleTask, String bodyTask, String strTaskDeadline) throws DaoException {
        Date taskDeadline = convertDate(strTaskDeadline);
        Set<User> personList = new HashSet<>();
        if (responsiblePersonId == 0) {
            personList.add(account.getUser());
        } else {
            UserService userService = new UserService();
            User user = userService.getUser(responsiblePersonId);
            personList.add(user);
        }
        TaskContent content = new TaskContent(bodyTask);
        Task newTask = new Task(defaultStatusTask, new Date(), titleTask, taskDeadline, content, personList);
        getTaskDao().saveOrUpdate(newTask);
        return newTask;
    }

    public Date convertDate(String postDate) {
        String[] dl = postDate.split("/");
        int year = Integer.parseInt(dl[2]);
        int month = Integer.parseInt(dl[0]);
        int dayOfMonth = Integer.parseInt(dl[1]);
        int defaultHour = 20;
        int defaultMinute = 15;
        Date bodyDeadline = new Date(year, month, dayOfMonth, defaultHour, defaultMinute);
        return bodyDeadline;
    }

    public boolean updateTask(Account account, long taskId, Integer status) {
        boolean b = false;
        transaction = session.beginTransaction();
        try {
            Task task;
            //status permitted to employee - 2,4
            final Integer taskStatusToApprove = 2;
            final Integer taskStatusInChecking = 4;
            if (account.getUser().getRole() == employeeRole) {
                System.out.println(taskStatusToApprove.equals(status) + " "+ taskStatusInChecking.equals(status));
                if (taskStatusToApprove.equals(status) || taskStatusInChecking.equals(status)) {
                    task = getTaskDao().getTaskToUser(taskId, account.getUser().getUserId());
                } else {
                    return false;
                }
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
            getTaskDao().saveOrUpdate(task);
            transaction.commit();
            account.getCurrentTasks().put(task.getTaskId(),task);
            b = true;
        } catch (DaoException e) {
            transaction.rollback();
            log.error(e, e);
        }
        return b;
    }

    public Task getTask(Account account, Long taskId) {
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
    public boolean updateTaskBody(Account account, Task task, String bodyTask, Integer newStatus) {
        SimpleDateFormat dateFormat = account.getDateFormat();
        boolean b;
        try {
            transaction = session.beginTransaction();
            System.out.println("2" + task);
            getTaskDao().refresh(task);
            System.out.println("3" + task);
            int userId = account.getUser().getUserId();
            task.setStatusId(newStatus);// устанавливаем статус на проверке
            String currentTaskBody = task.getContent().getBody();
            Calendar calendar = Calendar.getInstance();
            currentTaskBody = currentTaskBody.concat("\n\r").concat(dateFormat.format(calendar.getTime())).concat(" user:" + userId).concat(bodyTask);
            task.getContent().setBody(currentTaskBody);
            StringBuffer history = new StringBuffer(task.getContent().getHistory());
            history = history.append(dateFormat.format(calendar.getTime())).append("~status:").append("~korrect body").append(newStatus).append("~~");
            task.getContent().setHistory(history.toString());
            System.out.println("4" + task);
//            getTaskDao().saveOrUpdate(task.getContent());
            getTaskDao().saveOrUpdate(task);
            System.out.println("5" + task);
            account.getCurrentTasks().put(task.getTaskId(),task); //обновление в обход кэша?
            transaction.commit();
            b = true;
        } catch (DaoException e) {
            log.error(e, e);
            transaction.rollback();
            b = false;
        }
        return b;
    }

    public static TaskDao getTaskDao() {

        if (taskDao == null) {
            taskDao = new TaskDao();
        }
        return taskDao;
    }
}
