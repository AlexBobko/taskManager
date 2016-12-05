package loc.task.service;

import loc.task.entity.Task;
import loc.task.service.exc.TaskServiceException;
import loc.task.vo.Account;

public interface ITaskService {
    void updateTaskBody(Account ac, Task task, String bodyTask) throws TaskServiceException;

    void updateTaskStatus(Account ac, long taskId, Integer status) throws TaskServiceException;

    Task addNewTask(Account account, int employeeId,
                    String titleTask, String bodyTask, String strTaskDeadline) throws TaskServiceException;
    Task getTask(Account account, Long taskId) throws TaskServiceException;
}
