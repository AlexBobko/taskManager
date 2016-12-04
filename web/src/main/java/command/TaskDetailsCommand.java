package command;

import controller.RequestHandler;
import loc.task.entity.Task;
import loc.task.vo.Account;
import lombok.extern.log4j.Log4j;
import managers.MessageManager;
import managers.PageManager;
import loc.task.service.TaskService;
@Log4j
public class TaskDetailsCommand implements ICommand {
	@Override
	public String execute(RequestHandler content) {
		StringBuffer message = new StringBuffer();
		String page=null;
		try {
			Account account = (Account) content.getSessionAttributes().get(ACCOUNT);
			Long taskId = Long.parseLong((String) content.getRequestAttributes().get(CMD_VALUE));
			Task task = TaskService.getTaskService().getTask(account,taskId);
			if (task!=null) {
				content.getSessionAttributes().put(TASK, task);
				account.setCurrentTasks(null); //чистим коллекции, чтобы не таскать в сессию
				account.getUser().setTaskList(null);
				page = PageManager.getProperty("path.page.task");
				content.getSessionAttributes().put(ACCOUNT, account);
			}
		} catch (Exception e) {
			log.error(e,e);
			message = message.append(MessageManager.getProperty("task.detail.false"));
//			page = PageManager.getProperty("path.page.login");
		}

		content.getSessionAttributes().put(MESSAGE, message.toString());
		return page;
	}
}
