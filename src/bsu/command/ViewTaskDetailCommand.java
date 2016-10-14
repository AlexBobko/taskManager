package bsu.command;

import controller.SessionRequestContent;
import dto.Account;
import dto.TaskDTO;
import dto.TaskMetaDTO;
import resources.ConfigurationManager;
import resources.MessageManager;

public class ViewTaskDetailCommand extends AbsCommand {

	final public static String TASK  = "curTask"; 
	final public static String TASK_META  = "curTaskMeta"; 
	private String page;
	private StringBuffer message;

	@Override
	public String execute(SessionRequestContent content) {
		message = new StringBuffer();
		try {
			Account account = (Account) content.getSessionAttributes().get(ACCOUNT);
			int taskId = (int) Integer.parseInt((String) content.getRequestAttributes().get(CMD_VALUE));
			TaskMetaDTO meta = account.getTasksMeta().get(taskId);
//			TaskDTO task = new TaskDTO (); 
			TaskDTO task = account.getCurrentTasks().get(taskId);
//			SimpleDateFormat dateFormat = account.getDateFormat();
			content.getSessionAttributes().put(ACCOUNT, account);
			content.getSessionAttributes().put(TASK, task);
			content.getSessionAttributes().put(TASK_META, meta);
			page = ConfigurationManager.getProperty("path.page.task");
			// System.out.println("addNewTask: " + meta.getId());
		} catch (Exception e) {
			e.printStackTrace();
			message = message.append(MessageManager.getProperty("task.detail.false"));
			page = ConfigurationManager.getProperty("path.page.user");
		}
		content.getSessionAttributes().put(MESSAGE, message.toString());
		return page;
	}

}
