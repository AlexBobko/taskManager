package loc.task.services;

import loc.task.dao.ITaskDao;
import loc.task.dao.IUserDao;
import loc.task.entity.Task;
import loc.task.entity.TaskContent;
import loc.task.entity.User;
import loc.task.services.exc.LocServiceException;
import loc.task.vo.Account;
import loc.task.vo.AccountSuperior;
import loc.task.vo.TaskOutFilter;
import lombok.extern.log4j.Log4j;
import org.hibernate.HibernateException;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

//import javax.transaction.Transactional; 2 ая попытка
//import org.springframework.transaction.annotation.Transactional; пробуем

@Log4j
@Service
//@Transactional(propagation = Propagation.REQUIRED)
public class TaskService implements ITaskService {

    @Autowired
    private ITaskDao taskDao;
    @Autowired
    private IUserDao userDao;

    //TODO чекнуть на зацикленность
    @Autowired
    private IUserService userService;


    public TaskService() {
    }

    @Override
//    @Transactional(propagation = Propagation.MANDATORY, readOnly = true)
    public Account createAccount(User user) throws LocServiceException {
//        Transaction tx = taskDao.getSession().beginTransaction();
        Account account = null;

        if (user.getRole() == userService.employeeRole) {
            System.out.println("createAccount 1 ");
            TaskOutFilter currentTasksFilter = getTaskOutFilter(getDefaultEmployeeStatusList(), user.getUserId());
            System.out.println("createAccount 2 ");
            account = new Account(user, currentTasksFilter, getTasksList(currentTasksFilter, user.getUserId()));
            System.out.println("createAccount 3 ");
        } else if (user.getRole() == userService.superiorRole) {
            TaskOutFilter currentTasksFilter = getTaskOutFilter(getDefaultSuperiorStatusList()); //общий фильтр
            TaskOutFilter reportTaskFilter = getTaskOutFilter(getSuperiorReportStatusList());//дополнительный фильтр

            System.out.println("createAccountSuperior 3 ");
            account = new AccountSuperior(user, currentTasksFilter, getTasksList(currentTasksFilter),
                    reportTaskFilter, getTasksList(reportTaskFilter));
            System.out.println("createAccountSuperior 3 ");
            //TODO ?? загружается полностью юзер (персонал дата) обленили юзера )))
        }
        return account;
    }


    //TODO транзакция updateTaskList ОБРАБОТАТЬ ДАО ЭКС
    @Override
//    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public Account updateTaskList(Account ac) throws LocServiceException {
        Transaction tx = taskDao.getSession().beginTransaction();
        //TODO убрать дефолтную загрузку контента после решения проблем с КЭШЕМ
        if (ac.getUser().getRole() == UserService.employeeRole) {
            updateTaskOutFilter(ac.getCurrentTasksFilter(), ac.getUser().getUserId());
            ac.setCurrentTasks(getTasksList(ac.getCurrentTasksFilter(), ac.getUser().getUserId()));
        } else if (ac.getUser().getRole() == UserService.superiorRole) {
            updateTaskOutFilter(ac.getCurrentTasksFilter(), null);
            ac.setCurrentTasks(getTasksList(ac.getCurrentTasksFilter()));
        }
        tx.commit();
        return ac;
    }

    //TODO транзакция updateTaskStatus +
    @Override
//    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void updateTaskStatus(Account ac, long taskId, Integer status) throws LocServiceException {
        //TODO ?? в каком месте лучше ограничить операции пользователя (оставить спрингу?)
        //TODO (Spring) легализация операций + фильрация данных
        Transaction tx = taskDao.getSession().beginTransaction();
        Task task;
        Integer userId = null;
        try {
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
            //Конфликт по количеству тасков? стоит ли разрешать и стоит ли обновлять, может просто присоединить к сессии?
            ac.setCurrentTasks(getTasksList(ac.getCurrentTasksFilter(), userId));
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            log.error("Error updateTaskStatus " + e, e);
            throw new LocServiceException(e);
        }
    }

    //TODO транзакция updateTaskBody
    @Override
//    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void updateTaskBody(Account ac, Task task, String bodyTask) throws LocServiceException {
        Transaction tx = taskDao.getSession().beginTransaction();
        try {
            SimpleDateFormat dateFormat = ac.getDateFormat();
            taskDao.replicate(task);         // taskDao.refresh(task);
            int userId = ac.getUser().getUserId();
            String currentTaskBody = task.getContent().getBody();
            Calendar calendar = Calendar.getInstance();
            task.setStatusId(statusTaskApprove);    //TODO (ТЗ) устанавливаем статус 2 )
            updateTaskHistory(task, ac, reasonUpdateBody);
            currentTaskBody = currentTaskBody.concat("\n\r").concat(dateFormat.format(calendar.getTime())).concat(" ***user:" + userId).concat(bodyTask);
            task.getContent().setBody(currentTaskBody);
            tx.commit();
        } catch (HibernateException e) {
            tx.rollback();
            log.error(e);
            throw new LocServiceException(e);
        }
    }

    //TODO транзакция addNewTask (обработка дао ЕХ)
    @Override
//    @Transactional//(propagation = Propagation.REQUIRED, readOnly = false, isolation = Isolation.REPEATABLE_READ)
    public Task addNewTask(Account account, int employeeId,
                           String titleTask, String bodyTask, Date deadline) throws LocServiceException {
        Transaction tx = userDao.getSession().beginTransaction();
        try {
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
            tx.commit();
            return newTask;
        } catch (HibernateException e) {
            log.error(e, e);
            tx.rollback();
            throw new LocServiceException(e);
        }
    }

    //    @Transactional
    public void setAssignmentTaskTime(Account account, Date timeReporting, Task currentTask, Date currentDate) throws LocServiceException {
        //TODO проверить условие
        //TODO дописать в историю таска события
        System.out.println("дата 1");
        Transaction tx = taskDao.getSession().beginTransaction();
        taskDao.replicate(currentTask);
        Long deadline = currentTask.getDeadline().getTime();
        try {
            if (timeReporting.getTime() < deadline | deadline < currentDate.getTime()) {
                currentTask.setDateReporting(timeReporting);

            } else if (deadline > currentDate.getTime() & deadline < timeReporting.getTime()) {
                currentTask.setDateReporting(timeReporting);
                currentTask.setDeadline(timeReporting); //дописать в историю

            } else throw new LocServiceException(new Exception("setAssignmentTaskTimeException"));
            tx.commit();
        } catch (HibernateException e) {
            tx.rollback();
            System.out.println("HibernateException " + e);
            throw new LocServiceException(e);
        } catch (LocServiceException e) {
            System.out.println("LocServiceException " + e);
            tx.rollback();

            log.error(e);
            throw new LocServiceException(e);
        }
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
//    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Task getTask(Account account, Long taskId) throws LocServiceException {
        Transaction tx = taskDao.getSession().beginTransaction();
        User user = account.getUser();
        Task task;
        if (user.getRole() == UserService.employeeRole) {
            task = taskDao.getTaskToUser(taskId, user.getUserId());
        } else {
            task = taskDao.get(taskId);
        }
        tx.commit();
        return task;
    }

    private List<Task> getTasksList(TaskOutFilter f) throws LocServiceException {
        return getTasksList(f, null);
    }

    private List<Task> getTasksList(TaskOutFilter f, Integer userId) throws LocServiceException {
        Set<Integer> usersId = null;
        if (userId != null) {
            usersId = new HashSet<>(1);
            usersId.add(userId);
        }
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


    private TaskOutFilter updateTaskOutFilter(TaskOutFilter taskOutFilter, Integer userId) throws LocServiceException {
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

    private TaskOutFilter getTaskOutFilter(Set<Integer> includeStatus) throws LocServiceException {
        return getTaskOutFilter(includeStatus, null);
    }

    private TaskOutFilter getTaskOutFilter(Set<Integer> includeStatus, Integer userId) throws LocServiceException {
        TaskOutFilter taskOutFilter = new TaskOutFilter(includeStatus);
        return updateTaskOutFilter(taskOutFilter, userId);
    }

    private long getCountTask(Set<Integer> includeStatus, int userId) throws LocServiceException {
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
