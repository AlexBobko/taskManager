package loc.task.services;

import loc.task.entity.Task;
import loc.task.entity.User;
import loc.task.services.exc.TaskServiceException;
import loc.task.vo.Account;

import java.util.Date;

public interface ITaskService {
    Account createAccount(User user) throws TaskServiceException;

    void updateTaskBody(Account ac, Task task, String bodyTask) throws TaskServiceException;

    void updateTaskStatus(Account ac, long taskId, Integer status) throws TaskServiceException;
    Account updateTaskList(Account ac) throws TaskServiceException;

    Task addNewTask(Account account, int employeeId,
                    String titleTask, String bodyTask, Date deadline) throws TaskServiceException;

    Task getTask(Account account, Long taskId) throws TaskServiceException;

}
