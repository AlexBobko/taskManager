package command;

import controller.RequestHandler;
import dto.Account;
import dto.TaskDTO;
import dto.TaskMetaDTO;
import managers.ConfigurationManager;
import managers.MessageManager;


public class ViewTaskDetailCommand implements ICommand {
	private String page;
	private StringBuffer message;

	@Override
	public String execute(RequestHandler content) {
		message = new StringBuffer();
		try {
			Account account = (Account) content.getSessionAttributes().get(ACCOUNT);
			int taskId = Integer.parseInt((String) content.getRequestAttributes().get(CMD_VALUE));
			TaskMetaDTO meta = account.getTasksMeta().get(taskId);
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
