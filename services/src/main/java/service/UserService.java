package service;

import loc.task.db.UserDao;
import loc.task.db.exceptions.DaoException;
import loc.task.entity.User;
import loc.task.vo.Account;
import loc.task.vo.AccountSuperior;
import loc.task.vo.TaskOutFilter;
import org.apache.log4j.Logger;
import utils.org.mindrot.jbcrypt.BCrypt;

import java.util.HashSet;
import java.util.Set;

public class UserService {

    //    private Transaction transaction = null;
    //    Session session = HibernateUtil.getHibernateUtil().getSession();
    private static Logger log = Logger.getLogger(UserService.class);
    private static UserDao userDao = null;
    public final static Integer employeeRole = 1;
    public final static Integer superiorRole = 2;
    public final static Integer statusTaskNew = 1;
    public final static Integer statusTaskInApprove= 2;
    public final static Integer statusTaskInProduction = 3;
    public final static Integer statusTaskInChecking = 4;
    public final static Integer statusTaskReport = 5;
    public final static Integer statusTaskReady = 6;
    public final static Integer statusTaskDelete = 7;

    private UserService() {
    }

    public static Account getAccount(int userId, String userPassword) {
        return getAccount(userId, null, userPassword);
    }
    public static Account getAccount(String userLogin, String userPassword) {
        return getAccount(0, userLogin, userPassword);
    }
    private static Account getAccount(int userId, String userLogin, String userPassword) {
        Account account = null;
        User user;
        try {
            if (userLogin != null) {
                user = getUserDao().findEntityByLogin(userLogin);
            } else {
                user = getUserDao().get(userId);
            }
            if (user != null) {
                userPassword = "sваываыsd" + "dsdf@@"; //TODO !ХАРДКОД //чтобы не вводить пароль :)
//                userPassword = userPassword + "dsdf@@"; //проконтролить локальную соль Soul
                if (BCrypt.checkpw(userPassword, user.getPasswordHash())) {
                    account = createAccount(user); //System.out.println("It matches");
                } else {
                    user = null;
                }
            }
        } catch (DaoException e) {
            log.error(e, e);
        }
        return account;
    }
    private static Account createAccount(User user) {
        Account account = null;
        if (user.getRole() == employeeRole) {
            TaskOutFilter currentTasksFilter = TaskService.getTaskOutFilter(getDefaultEmployeeStatusList(), user.getUserId());
            account = new Account(user, currentTasksFilter, TaskService.getTasksList(currentTasksFilter, user.getUserId()));
        } else if (user.getRole() == superiorRole) {
            //TODO могут быть проблемы с LAZY userID  можно еще размер мапки ограничить
            TaskOutFilter currentTasksFilter = TaskService.getTaskOutFilter(getDefaultSuperiorStatusList()); //общий фильтр
            TaskOutFilter reportTaskFilter = TaskService.getTaskOutFilter(getSuperiorReportStatusList());//дополнительный фильтр
            account = new AccountSuperior(user, currentTasksFilter, TaskService.getTasksList(currentTasksFilter),
                    reportTaskFilter, TaskService.getTasksList(reportTaskFilter));
            //TODO ?? загружается полностью юзер обленили юзера )))
        }
        return account;
    }
    //TODO тоже в таск сервис закинуть?
    private static Set<Integer> getSuperiorReportStatusList() {
        Set<Integer> statusList = new HashSet<>(1);
        statusList.add(statusTaskReport); //Назначено время
        return statusList;
    }
    private static Set<Integer> getDefaultSuperiorStatusList() {
        Set<Integer> statusList = new HashSet<>(2);
        statusList.add(statusTaskInApprove);
        statusList.add(statusTaskInChecking);
        return statusList;
    }
    private static Set<Integer> getDefaultEmployeeStatusList() {
        Set<Integer> statusList = new HashSet<>(5);
        statusList.add(statusTaskNew);
        statusList.add(statusTaskInApprove);
        statusList.add(statusTaskInProduction);
        statusList.add(statusTaskInChecking);
        statusList.add(statusTaskReport);
        return statusList;
    }
    public static User getUser(int userId) throws DaoException {
        return getUserDao().get(userId);
    }
    public static UserDao getUserDao() {
        if (userDao == null) {
            userDao = new UserDao();
        }
        return userDao;
    }

//    public static TaskDao getTaskDao() {
//
//        if (taskDao == null) {
//            taskDao = new TaskDao();
//        }
//        return taskDao;
//    }


    //              статусы важные руководителю: 2, 4, 5
    //статусы в которые может перевести таск работник: 1->2,3->4,
//            1(1) - Создано (новый) - еще можно редактировать. <br/>
//            2(1) - На утверждении - ждет утвеждения начальника, для начала работы<br/>
//            3(2) - В работе, подчиненный выполняет задание<br/>
//            4(1) - На проверкe - задание передано на проверку руководителю для назначения времени приема<br/>
//            5(2) - К сдаче - назначено время приема<br/>
//            6(2) - Выполнено - задача закрывается и уходит в архив<br/>
//            7     добавить на UI -  7 удалена
    //        final Set<Integer> includeStatusDefault = new HashSet<>(6);
//        includeStatusDefault.add(1);
//        includeStatusDefault.add(2);
//        includeStatusDefault.add(3);
//        includeStatusDefault.add(4);
//        includeStatusDefault.add(5);
//        includeStatusDefault.add(6);



//    public Map<Long, Task> getTasksList(TaskOutFilter f, Integer userId) {
//        Set<Integer> usersId = new HashSet<>(1);
//        usersId.add(userId);
//        List<Task> tasksList = getTaskDao().getCurrentTaskUser(f.getPage(), f.getTasksPerPage(), f.getTotalCount(), f.getIncludeStatus(), usersId);
//        Map<Long, Task> currentTasks = new HashMap(f.getTasksPerPage());
//        for (Task task : tasksList) {
//            currentTasks.put(task.getTaskId(), task);
//        }
//        return currentTasks;
//    }
//
//    public Map<Long, Task> getTasksList(TaskOutFilter f) {
//        List<Task> tasksList = getTaskDao().getTasks(f.getPage(), f.getTasksPerPage(), f.getTotalCount(), f.getIncludeStatus(), f.getSort(), f.isAsk());
//        Map<Long, Task> currentTasks = new HashMap(f.getTasksPerPage());
//        for (Task task : tasksList) {
//            currentTasks.put(task.getTaskId(), task);
//        }
//        return currentTasks;
//    }
//
//
//    public static long getCountTask(Set<Integer> includeStatus, int userId) {
//        return getTaskDao().getCountTask(includeStatus, userId);
//    }

//    public Task addNewTask(Account account, int responsiblePersonId,
//                           String titleTask, String bodyTask, String strTaskDeadline) throws DaoException {
//        Date taskDeadline = convertDate(strTaskDeadline);
//        Set<User> personList = new HashSet<>();
//        if (responsiblePersonId == 0) {
//            personList.add(account.getUser());
//        } else {
//            UserService userService = new UserService();
//            User user = userService.getUser(responsiblePersonId);
//            personList.add(user);
//        }
//        TaskContent content = new TaskContent(bodyTask);
//        Task newTask = new Task(defaultStatusTask, new Date(), titleTask, taskDeadline, content, personList);
//        getTaskDao().saveOrUpdate(newTask);
//        return newTask;
//    }



//    public boolean updateTask(Account account, long taskId, Integer status) {
//        boolean b = false;
//        transaction = session.beginTransaction();
//        try {
//            Task task;
//            //status permitted to employee - 2,4
//            final Integer taskStatusToApprove = 2;
//            final Integer taskStatusInChecking = 4;
//            if (account.getUser().getRole() == employeeRole) {
//                if (taskStatusToApprove.equals(status) || taskStatusInChecking.equals(status)) {
//                    task = getTaskDao().getTaskToUser(taskId, account.getUser().getUserId());
//                } else {
//                    return false;
//                }
//            } else {
//                task = getTaskDao().get(taskId);
//            }
//            SimpleDateFormat dateFormat = account.getDateFormat();
//            task.setStatusId(status);
//            Calendar calendar = Calendar.getInstance();
//            String history = task.getContent().getHistory();
//            history = history.concat(dateFormat.format(calendar.getTime())).concat("~status:").
//                    concat(status.toString()).concat("~~");
//            task.getContent().setHistory(history);
//            getTaskDao().saveOrUpdate(task);
//            transaction.commit();
//            b = true;
//        } catch (DaoException e) {
//            transaction.rollback();
//            log.error(e, e);
//        }
//        return b;
//    }

//    public Task getTask(Account account, Long taskId) {
//        User user = account.getUser();
//        int role = user.getRole();
//        Task task = null;
//        if (role == 1) {
//            System.out.println(user.getUserId());
//            task = getTaskDao().getTaskToUser(taskId, user.getUserId());
//        } else {
//            try {
//                task = getTaskDao().get(taskId);
//            } catch (DaoException e) {
//                log.error(e, e);
//            }
//        }
//        return task;
//    }

//    public boolean updateTaskBody(Account account, Task task, String bodyTask, Integer newStatus) {
//        SimpleDateFormat dateFormat = account.getDateFormat();
//        boolean b;
//        try {
//            transaction = session.beginTransaction();
//            getTaskDao().refresh(task);
//            int userId = account.getUser().getUserId();
//            task.setStatusId(newStatus);// устанавливаем статус на проверке
//            String currentTaskBody = task.getContent().getBody();
//            Calendar calendar = Calendar.getInstance();
//            currentTaskBody = currentTaskBody.concat("\n\r").concat(dateFormat.format(calendar.getTime())).concat(" user:" + userId).concat(bodyTask);
//            task.getContent().setBody(currentTaskBody);
//            StringBuffer history = new StringBuffer(task.getContent().getHistory());
//            history = history.append(dateFormat.format(calendar.getTime())).append("~status:").append("~korrect body").append(newStatus).append("~~");
//            task.getContent().setHistory(history.toString());
//            getTaskDao().saveOrUpdate(task);
//            transaction.commit();
//            b = true;
//        } catch (DaoException e) {
//            log.error(e, e);
//            transaction.rollback();
//            b = false;
//        }
//        return b;
//    }


}
