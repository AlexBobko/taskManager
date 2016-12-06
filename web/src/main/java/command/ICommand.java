package command;

import controller.RequestHandler;
import loc.task.services.UserService;

/**
 * ICommand
 */
public interface ICommand {
    // Attribute name
    static final String CMD_VALUE = "cmdValue";
//    static final String PARAM_SESSION_USER = "currentUser";
    static final String ACCOUNT = "account";
    static final String MESSAGE = "message";
    static final String TASK = "curTask";
    static final String LOGIN = "username";
    static final String PASSWORD = "password";
//    static final String TASK_META = "curTaskMeta";
    static final String TASK_EMPLOYEE = "employee";
    final String POST_TITLE = "titleTask";
    final String POST_BODY = "bodyTask";
    final String POST_DEADLINE = "taskDeadline";
    final Integer employeeRole = UserService.employeeRole;
    final Integer superiorRole = UserService.superiorRole;

    String execute(RequestHandler content);
}
