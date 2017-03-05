package loc.task.services;

import loc.task.entity.Task;
import loc.task.entity.User;
import loc.task.services.exc.LocServiceException;
import loc.task.vo.Account;

import java.util.Date;

public interface ITaskService {
    Integer statusTaskNew = 1;
    Integer statusTaskApprove = 2;
    Integer statusTaskProcess = 3;
    Integer statusTaskReview = 4;
    Integer statusTaskReport = 5;
    Integer statusTaskReady = 6;
    Integer statusTaskDelete = 7;
    String reasonUpdateStatus = "~status:";
    String reasonUpdateBody = "~body:";

    Account createAccount(User user) throws LocServiceException;

    void updateTaskBody(Account ac, Task task, String bodyTask) throws LocServiceException;

    void updateTaskStatus(Account ac, long taskId, Integer status) throws LocServiceException;
    Account updateTaskList(Account ac) throws LocServiceException;

    //TODO
    void setAssignmentTaskTime(Account account,Date assignmentDate,Task currentTask,Date currentDate) throws LocServiceException;

    Task addNewTask(Account account, int employeeId,
                    String titleTask, String bodyTask, Date deadline) throws LocServiceException;

    Task getTask(Account account, Long taskId) throws LocServiceException;

}
