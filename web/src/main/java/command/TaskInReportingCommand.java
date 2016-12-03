package command;

import controller.RequestHandler;
import loc.task.vo.Account;
import managers.MessageManager;
import managers.PageManager;
import org.apache.log4j.Logger;
import service.TaskService;

/** назначить время приема 5 */
//TODO !! дату приема вписать
public class TaskInReportingCommand implements ICommand {
	private static Logger log = Logger.getLogger(TaskInReportingCommand.class);
	final private Integer newStatus = TaskService.statusTaskReport;
	@Override
	public String execute(RequestHandler content) {
		String page = null;
		StringBuffer message = new StringBuffer();
		try {
			Account account = (Account) content.getSessionAttributes().get(ACCOUNT);
			long taskId = Long.parseLong((String) content.getRequestAttributes().get(CMD_VALUE));
			if (account.getUser().getRole() == employeeRole) {
				page = PageManager.getProperty("path.page.user");
			} else if ((account.getUser().getRole() == superiorRole)) {
				page = PageManager.getProperty("path.page.superior");
				if (TaskService.getTaskService().updateTaskStatus(account, taskId, newStatus)) {
					message = message.append(MessageManager.getProperty("task.update")).append(taskId);
				}
			}
			content.getSessionAttributes().put(ACCOUNT, account);
		} catch (Exception e) {
			log.error(e, e);
			message = message.append(MessageManager.getProperty("task.update.false"));
		}
		if (page == null) {
			page = PageManager.getProperty("path.page.login");
		}
		content.getSessionAttributes().put(MESSAGE, message.toString());
		return page;
	}
}
