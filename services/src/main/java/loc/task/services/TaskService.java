package loc.task.services;

import loc.task.dao.ITaskDao;
import loc.task.dao.IUserDao;
import loc.task.entity.Task;
import loc.task.entity.TaskContent;
import loc.task.entity.User;
import loc.task.services.exc.TaskServiceException;
import loc.task.vo.Account;
import loc.task.vo.AccountSuperior;
import loc.task.vo.TaskOutFilter;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

@Log4j
@Service
//@Transactional
public class TaskService implements ITaskService {

    @Autowired
    private ITaskDao taskDao;
    @Autowired
    private IUserDao userDao;

    public final static Integer statusTaskNew = 1;
    public final static Integer statusTaskApprove = 2;
    public final static Integer statusTaskProcess = 3;
    public final static Integer statusTaskReview = 4;
    public final static Integer statusTaskReport = 5;
    public final static Integer statusTaskReady = 6;
    public final static Integer statusTaskDelete = 7;
    public final static String reasonUpdateStatus = "~status:";
    public final static String reasonUpdateBody = "~body:";

    public TaskService() {
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY, readOnly = true)
    public Account createAccount(User user) throws TaskServiceException {
        Account account = null;
        //TODO костыли
        int employeeRole = 1;
        int superiorRole = 2;
        if (user.getRole() == employeeRole) {
            TaskOutFilter currentTasksFilter = getTaskOutFilter(getDefaultEmployeeStatusList(), user.getUserId());
            account = new Account(user, currentTasksFilter, getTasksList(currentTasksFilter, user.getUserId()));
        } else if (user.getRole() == superiorRole) {
            TaskOutFilter currentTasksFilter = getTaskOutFilter(getDefaultSuperiorStatusList()); //общий фильтр
            TaskOutFilter reportTaskFilter = getTaskOutFilter(getSuperiorReportStatusList());//дополнительный фильтр
            account = new AccountSuperior(user, currentTasksFilter, getTasksList(currentTasksFilter),
                    reportTaskFilter, getTasksList(reportTaskFilter));
            //TODO ?? загружается полностью юзер (персонал дата) обленили юзера )))
        }
        return account;
    }


    //TODO транзакция updateTaskList ОБРАБОТАТЬ ДАО ЭКС
    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public Account updateTaskList(Account ac) throws TaskServiceException {
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

    //TODO транзакция updateTaskStatus +
    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void updateTaskStatus(Account ac, long taskId, Integer status) throws TaskServiceException {
        //TODO ?? в каком месте лучше ограничить операции пользователя (оставить спрингу?)
        //TODO (Spring) легализация операций + фильрация данных
        Task task;
        Integer userId = null;
        if (ac.getUser().getRole() == UserService.employeeRole) {
            task = taskDao.getTaskToUser(taskId, ac.getUser().getUserId());
            //session.replicate(task, ReplicationMode.LATEST_VERSION);
            System.out.println("TASK STATUS UPDATE:" + taskId + " new status: " + status);
            userId = ac.getUser().getUserId();
        } else {
            task = taskDao.get(taskId);
            //TODO для фильтрации по пользователю передать и присвоить userId из расширенного ака
        }
        task.setStatusId(status);
        updateTaskHistory(task, ac, reasonUpdateStatus);
//        System.out.println("TASK UPDATE STATUS 4 " + session.getStatistics() + ": " + taskId);
        //Конфликт по количеству тасков? стоит ли разрешать и стоит ли обновлять, может просто присоединить к сессии?
        ac.setCurrentTasks(getTasksList(ac.getCurrentTasksFilter(), userId));
//            transaction.commit();
//        System.out.println("TASK UPDATE 5 " + session.getStatistics() + ": " + task.getTaskId() + " new status: " + task.getStatusId());

//        Exception e) {
//            transaction.rollback();
//            log.error("Error updateTaskStatus " + e, e);
        //TODO (Spring) вернуть ошибку на UI
//            log.error(e, e);
    }

    //TODO транзакция updateTaskBody
    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void updateTaskBody(Account ac, Task task, String bodyTask) throws TaskServiceException {
        SimpleDateFormat dateFormat = ac.getDateFormat();
        taskDao.replicate(task);         // taskDao.refresh(task);
        int userId = ac.getUser().getUserId();
        String currentTaskBody = task.getContent().getBody();
        Calendar calendar = Calendar.getInstance();
        task.setStatusId(statusTaskApprove);    //TODO (ТЗ) устанавливаем статус 2 )
        updateTaskHistory(task, ac, reasonUpdateBody);
        currentTaskBody = currentTaskBody.concat("\n\r").concat(dateFormat.format(calendar.getTime())).concat(" ***user:" + userId).concat(bodyTask);
        task.getContent().setBody(currentTaskBody);
//            System.out.println("stat (updateTaskBody): " + session.getStatistics());
//            transaction.commit();

//        } catch (DaoException e) {
//            log.error(e, e);
//            transaction.rollback();
//            throw new TaskServiceException(e);
        //TODO (Spring) вернуть ошибку на UI
    }

    //TODO транзакция addNewTask (обработка дао ЕХ)
    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, isolation = Isolation.REPEATABLE_READ)
    public Task addNewTask(Account account, int employeeId,
                           String titleTask, String bodyTask, Date deadline) throws TaskServiceException {
        Set<User> users = new HashSet<>();
        if (employeeId == 0) { //TODO ERROR!!!
            users.add(account.getUser());
        } else {
            User user = userDao.load(employeeId);
            users.add(user);
        }
        TaskContent content = new TaskContent(bodyTask);
        Task newTask = new Task(statusTaskNew, new Date(), titleTask, deadline, content, users);
        taskDao.saveOrUpdate(newTask);
//            transaction.commit();
        return newTask;
//        } catch (DaoException e) {
//            log.error(e, e);
//            transaction.rollback();
//            throw new TaskServiceException(e);
        //TODO (Spring) вернуть ошибку на UI
    }

    private void updateTaskHistory(Task task, Account account, String reason) {
        SimpleDateFormat dateFormat = account.getDateFormat();
        Calendar calendar = Calendar.getInstance();
        String history = task.getContent().getHistory();

        //TODO (ТЗ) шлифануть формат истории
        history = history.concat(dateFormat.format(calendar.getTime())).concat(reason).
                concat(task.getStatusId() + "").concat("~user:").concat(account.getUser().getUserId() + "").concat("~~");
        task.getContent().setHistory(history);
    }

    //TODO TRANSACTION
    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Task getTask(Account account, Long taskId) throws TaskServiceException {
        User user = account.getUser();
        Task task;
        if (user.getRole() == UserService.employeeRole) {
            task = taskDao.getTaskToUser(taskId, user.getUserId());
        } else {
            task = taskDao.get(taskId);
        }
        return task;
//        } catch (DaoException e) {
//            log.error(e, e);
//            throw new TaskServiceException(e);
    }

    private List<Task> getTasksList(TaskOutFilter f, Integer userId) throws TaskServiceException {
        Set<Integer> usersId = new HashSet<>(1);
        usersId.add(userId);
        long countPage = f.getTotalCount() / (long) f.getTasksPerPage();
        if (f.getTotalCount() % (long) f.getTasksPerPage() > 0) {
            countPage++;
        }
        if (f.getPage() > (int) countPage) {
            f.setPage((int) countPage);
        } else if (f.getPage() <= 0) {
            f.setPage(1);
        }
        int firstResult = (f.getPage() - 1) * f.getTasksPerPage();
        return taskDao.getTasks(firstResult, f.getTasksPerPage(), f.getIncludeStatus(), f.getSort(), f.isAsk(), usersId);
    }

    private List<Task> getTasksList(TaskOutFilter f) throws TaskServiceException {
        return getTasksList(f, null);
    }

    private TaskOutFilter updateTaskOutFilter(TaskOutFilter taskOutFilter, Integer userId) throws TaskServiceException {
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

    private TaskOutFilter getTaskOutFilter(Set<Integer> includeStatus) throws TaskServiceException {
        return getTaskOutFilter(includeStatus, null);
    }

    private TaskOutFilter getTaskOutFilter(Set<Integer> includeStatus, Integer userId) throws TaskServiceException {
        TaskOutFilter taskOutFilter = new TaskOutFilter(includeStatus);
        return updateTaskOutFilter(taskOutFilter, userId);
    }

    private long getCountTask(Set<Integer> includeStatus, int userId) throws TaskServiceException {

        return taskDao.getCountTask(includeStatus, userId);
    }

    private Set<Integer> getSuperiorReportStatusList() {
        Set<Integer> statusList = new HashSet<>(1);
        statusList.add(TaskService.statusTaskReport); //Назначено время
        return statusList;
    }

    private Set<Integer> getDefaultSuperiorStatusList() {
        Set<Integer> statusList = new HashSet<>(2);
        statusList.add(TaskService.statusTaskApprove);
        statusList.add(TaskService.statusTaskReview);
        return statusList;
    }

    private Set<Integer> getDefaultEmployeeStatusList() {
        Set<Integer> statusList = new HashSet<>(5);
        statusList.add(statusTaskNew);
        statusList.add(statusTaskApprove);
        statusList.add(statusTaskProcess);
        statusList.add(statusTaskReview);
        statusList.add(statusTaskReport);
        return statusList;
    }

}
