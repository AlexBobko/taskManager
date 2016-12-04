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
import lombok.extern.log4j.Log4j;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.text.SimpleDateFormat;
import java.util.*;

@Log4j
public class TaskService {
//    private static Logger log = Logger.getLogger(TaskService.class); //TODO log add e  log.error(e, e);
    private static TaskService taskService = null;

    private final static TaskDao taskDao = TaskDao.getTaskDao();;
    private final static BaseDao baseDao = BaseDao.getBaseDao();

    public final static Integer statusTaskNew = 1;
    public final static Integer statusTaskApprove = 2;
    public final static Integer statusTaskProcess = 3;
    public final static Integer statusTaskReview = 4;
    public final static Integer statusTaskReport = 5;
    public final static Integer statusTaskReady = 6;
    public final static Integer statusTaskDelete = 7;
    public final static String reasonUpdate = "~status:";
    public final static String reasonUpdateBody = "~body:";

    //TODO ?? вариант создания синглтона
    private TaskService() {
    }

    private static synchronized TaskService getInstance() {
        if (taskService == null) {
            taskService = new TaskService();
        }
        return taskService;
    }

    public static TaskService getTaskService() {
        if (taskService == null) {
            return getInstance();
        }
        return taskService;
    }

    //TODO транзакция updateTaskList
    public Account updateTaskList(Account ac) {
        //TODO убрать дефолтную загрузку контента после решения проблем с КЭШЕМ
        if (ac.getUser().getRole() == UserService.employeeRole) {
            updateTaskOutFilter(ac.getCurrentTasksFilter(), ac.getUser().getUserId());
            ac.setCurrentTasks(getTasksList(ac.getCurrentTasksFilter(), ac.getUser().getUserId()));
        } else if (ac.getUser().getRole() == UserService.superiorRole) {
            updateTaskOutFilter(ac.getCurrentTasksFilter(), null);
            ac.setCurrentTasks(getTasksList(ac.getCurrentTasksFilter()));
        }
        return ac;
    }

    //TODO транзакция updateTaskStatus
    public boolean updateTaskStatus(Account ac, long taskId, Integer status) {

        //TODO ?? в каком месте лучше ограничить операции пользователя (оставить спрингу?)
        Session session = HibernateUtil.getHibernateUtil().getSession();
        Transaction transaction = session.beginTransaction();
        boolean b = false;
        Task task;
        Integer userId = null;
        try {
            if (ac.getUser().getRole() == UserService.employeeRole) {
                task = taskDao.getTaskToUser(taskId, ac.getUser().getUserId());
                //TODO ?? какой метод лучше использовать ? (vs replicate)
                //session.replicate(task, ReplicationMode.LATEST_VERSION);
                System.out.println("TASK STATUS UPDATE:" + taskId + " new status: " + status);
                userId = ac.getUser().getUserId();
            } else {
                task = taskDao.get(taskId);
                //TODO для фильтрации по пользователю передать и присвоить userId из расширенного ака
            }
            task.setStatusId(status);
            updateTaskHistory(task, ac, reasonUpdate);
            System.out.println("TASK UPDATE STATUS 4 " + session.getStatistics() + ": " + taskId);
            //Конфликт по количеству тасков? стоит ли разрешать и стоит ли обновлять, может просто присоединить к сессии?
            ac.setCurrentTasks(getTasksList(ac.getCurrentTasksFilter(), userId));
            transaction.commit();

            System.out.println("TASK UPDATE 5 " + session.getStatistics() + ": " + task.getTaskId() + " new status: " + task.getStatusId());

            b = true;
        } catch (DaoException e) {
            transaction.rollback();
//            session.close();
            log.error(e, e);
        }
        return b;
    }

    //TODO транзакция updateTaskBody
    public boolean updateTaskBody(Account ac, Task task, String bodyTask) {
        Session session = HibernateUtil.getHibernateUtil().getSession();
        Transaction transaction = session.beginTransaction();

        SimpleDateFormat dateFormat = ac.getDateFormat();
        boolean b;
        try {
            taskDao.refresh(task);
//            task = taskDao.get(task.getTaskId());
//            session.refresh(task); //TODO ?? Вынести в ДАО?
            int userId = ac.getUser().getUserId();
            String currentTaskBody = task.getContent().getBody();
            Calendar calendar = Calendar.getInstance();

            task.setStatusId(statusTaskApprove);// устанавливаем статус на проверке

            updateTaskHistory(task, ac, reasonUpdateBody);
            currentTaskBody = currentTaskBody.concat("\n\r").concat(dateFormat.format(calendar.getTime())).concat(" user:" + userId).concat(bodyTask);
            task.getContent().setBody(currentTaskBody);

            System.out.println("currentTaskBody: " + task.getContent().getBody());
            System.out.println("currentTaskHistory: " + task.getContent().getHistory());

//            session.replicate(task, ReplicationMode.OVERWRITE);
            System.out.println("stat:1 " + session.getStatistics() + ": ");

//            session.flush();
            if (userId == -1) {
                throw new DaoException(new Exception());
            }
            transaction.commit();
            b = true;
        } catch (DaoException e) {
            log.error(e, e);
            transaction.rollback();
            b = false;
        }
        return b;
    }

    //TODO транзакция addNewTask
    public Task addNewTask(Account account, int employeeId,
                           String titleTask, String bodyTask, String strTaskDeadline) throws DaoException {
        Transaction transaction = HibernateUtil.getHibernateUtil().getSession().beginTransaction();
        Date taskDeadline = UtilService.convertDate(strTaskDeadline);
        Set<User> users = new HashSet<>();
        if (employeeId == 0) {
            users.add(account.getUser());
        } else {
            User user = UserService.getUserService().getUser(employeeId);
            users.add(user);
        }
        TaskContent content = new TaskContent(bodyTask);
        Task newTask = new Task(statusTaskNew, new Date(), titleTask, taskDeadline, content, users);
        taskDao.saveOrUpdate(newTask);

        transaction.commit();
        return newTask;
    }

    public void updateTaskHistory(Task task, Account account, String reason) {
        SimpleDateFormat dateFormat = account.getDateFormat();
        Calendar calendar = Calendar.getInstance();
        String history = task.getContent().getHistory();

        //TODO шлифануть формат истории
        history = history.concat(dateFormat.format(calendar.getTime())).concat(reason).
                concat(task.getStatusId() + "").concat("~user:").concat(account.getUser().getUserId() + "").concat("~~");
        task.getContent().setHistory(history);
    }

    public Task getTask(Account account, Long taskId) {
        User user = account.getUser();
        Task task = null;
        if (user.getRole() == UserService.employeeRole) {
            task = taskDao.getTaskToUser(taskId, user.getUserId());
        } else {
            try {
                task = taskDao.get(taskId);
            } catch (DaoException e) {
                log.error(e, e);
            }
        }
        return task;
    }

    public List<Task> getTasksList(TaskOutFilter f, Integer userId) {
        if (userId != null) {
            Set<Integer> usersId = new HashSet<>(1);
            usersId.add(userId);
            return taskDao.getTasks(f.getPage(), f.getTasksPerPage(), f.getTotalCount(), f.getIncludeStatus(), f.getSort(), f.isAsk(), usersId);
        } else
            return getTasksList(f);
    }

    public List<Task> getTasksList(TaskOutFilter f) {
        return taskDao.getTasks(f.getPage(), f.getTasksPerPage(), f.getTotalCount(), f.getIncludeStatus(), f.getSort(), f.isAsk());
    }

    public TaskOutFilter updateTaskOutFilter(TaskOutFilter taskOutFilter, Integer userId) {
        long totalCount = 1L;
        if (userId != null) {
            totalCount = getCountTask(taskOutFilter.getIncludeStatus(), userId);
        } else {
            totalCount = taskDao.getCountTask(taskOutFilter.getIncludeStatus());
        }
        long countPage = totalCount / (long) taskOutFilter.getTasksPerPage();
        if (totalCount % (long) taskOutFilter.getTasksPerPage() > 0) {
            countPage++;
        }
        taskOutFilter.setTotalCount(totalCount);
        taskOutFilter.setCountPage(countPage);
        if ((int) countPage < taskOutFilter.getPage()) {
            taskOutFilter.setPage((int) countPage);
        }
        return taskOutFilter;
    }

    public TaskOutFilter getTaskOutFilter(Set<Integer> includeStatus) {
        return getTaskOutFilter(includeStatus, null);
    }

    public TaskOutFilter getTaskOutFilter(Set<Integer> includeStatus, Integer userId) {
        TaskOutFilter taskOutFilter = new TaskOutFilter(includeStatus);
        return updateTaskOutFilter(taskOutFilter, userId);
    }

    public long getCountTask(Set<Integer> includeStatus, int userId) {

        return taskDao.getCountTask(includeStatus, userId);
    }

    public Set<Integer> getSuperiorReportStatusList() {
        Set<Integer> statusList = new HashSet<>(1);
        statusList.add(TaskService.statusTaskReport); //Назначено время
        return statusList;
    }

    public Set<Integer> getDefaultSuperiorStatusList() {
        Set<Integer> statusList = new HashSet<>(2);
        statusList.add(TaskService.statusTaskApprove);
        statusList.add(TaskService.statusTaskReview);
        return statusList;
    }

    public Set<Integer> getDefaultEmployeeStatusList() {
        Set<Integer> statusList = new HashSet<>(5);
        statusList.add(statusTaskNew);
        statusList.add(statusTaskApprove);
        statusList.add(statusTaskProcess);
        statusList.add(statusTaskReview);
        statusList.add(statusTaskReport);
        return statusList;
    }


//    public TaskDao getTaskDao() {
//        //TODO перенести в статик поле, если с сингл ДАО гуд
//        if (taskDao == null) {
//            taskDao = TaskDao.getTaskDao();
////            taskDao = new TaskDao();
//        }
//        return taskDao;
//    }

}
