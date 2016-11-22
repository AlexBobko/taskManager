package service;

import loc.task.db.TaskDao;
import loc.task.db.exceptions.DaoException;
import loc.task.entity.Task;
import loc.task.entity.TaskContent;
import loc.task.entity.User;
import loc.task.util.HibernateUtil;
import loc.task.vo.Account;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

//              статусы важные руководителю: 2, 4, 5
//            1(1) - Создано (новый) - еще можно редактировать. <br/>
//            2(1) - На утверждении - ждет утвеждения начальника, для начала работы<br/>
//            3(2) - В работе, подчиненный выполняет задание<br/>
//            4(1) - На проверкe - задание передано на проверку руководителю для назначения времени приема<br/>
//            5(2) - К сдаче - назначено время приема<br/>
//            6(2) - Выполнено - задача закрывается и уходит в архив<br/>
//            7     добавить на UI -  7 удалена

public class TaskService {
    private static Logger log = Logger.getLogger(TaskService.class); //TODO log add e  log.error(e, e);
    private static TaskDao taskDao = null;
    final private Integer employeeRole = 1;
    final private Integer defaultStatusTask = 1;
    private Transaction transaction = null;

    Session session = HibernateUtil.getHibernateUtil().getSession();

    public TaskService() {

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
        Task task=null;
        if (role == 1) {
            task = getTaskDao().getTaskToUser(taskId, user.getUserId());
        } else {
            try {
                task = getTaskDao().get(taskId);
            }catch (DaoException e) {
                log.error(e,e);
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
            getTaskDao().refresh(task);
            int userId = account.getUser().getUserId();
            task.setStatusId(newStatus);// устанавливаем статус на проверке
            String currentTaskBody = task.getContent().getBody();
            Calendar calendar = Calendar.getInstance();
            currentTaskBody = currentTaskBody.concat("\n\r").concat(dateFormat.format(calendar.getTime())).concat(" user:" + userId).concat(bodyTask);
            task.getContent().setBody(currentTaskBody);
            StringBuffer history = new StringBuffer(task.getContent().getHistory());
            history = history.append(dateFormat.format(calendar.getTime())).append("~status:").append("~korrect body").append(newStatus).append("~~");
            task.getContent().setHistory(history.toString());
            getTaskDao().saveOrUpdate(task);
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
