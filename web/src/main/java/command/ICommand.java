package command;

import controller.RequestHandler;
import service.UserService;

/** ICommand */
public interface ICommand {
	// Attribute name
	public static final String CMD_VALUE = "cmdValue";
	public static final String PARAM_SESSION_USER = "currentUser";
	public static final String ACCOUNT = "account";
	public static final String MESSAGE = "message";
	public static final String TASK  = "curTask";
	public static final String TASK_META  = "curTaskMeta";
	public static final String TASK_EMPLOYEE  = "employee";
	static final String POST_TITLE = "titleTask";
	static final String POST_BODY = "bodyTask";
	static final String POST_DEADLINE = "taskDeadline";
	static final Integer employeeRole = UserService.employeeRole;
	static final Integer superiorRole = UserService.superiorRole;

	String execute(RequestHandler content);
}
