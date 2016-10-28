package command;

import controller.RequestHandler;
import dto.Account;
import dto.TaskDTO;
import dto.TaskMetaDTO;
import managers.PageManager;
import managers.MessageManager;


public class ViewTaskDetailCommand implements ICommand {
//TODO перенести в doGET
	@Override
	public String execute(RequestHandler content) {
		StringBuffer message = new StringBuffer();
		String page=null;
		try {
			Account account = (Account) content.getSessionAttributes().get(ACCOUNT);
			int taskId = Integer.parseInt((String) content.getRequestAttributes().get(CMD_VALUE));
			TaskMetaDTO meta = account.getTasksMeta().get(taskId);
			TaskDTO task = account.getCurrentTasks().get(taskId);
//			SimpleDateFormat dateFormat = account.getDateFormat();
			content.getSessionAttributes().put(ACCOUNT, account);
			content.getSessionAttributes().put(TASK, task);
			content.getSessionAttributes().put(TASK_META, meta);
			page = PageManager.getProperty("path.page.task");
			// System.out.println("addNewTask: " + meta.getId());
		} catch (Exception e) {
			message = message.append(MessageManager.getProperty("task.detail.false"));
//			page = PageManager.getProperty("path.page.login");
		}
		content.getSessionAttributes().put(MESSAGE, message.toString());
		return page;
	}

}
