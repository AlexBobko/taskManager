package command;

import java.text.SimpleDateFormat;

import controller.SessionRequestContent;
import dto.Account;
import dto.TaskDTO;
import dto.TaskMetaDTO;
import resources.ConfigurationManager;
import resources.MessageManager;

/** назначить время приема */
public class TaskInPayCommand extends AbsCommand {
	private String page;
	private StringBuffer message;
	private boolean b;

	@Override
	public String execute(SessionRequestContent content) {
		message = new StringBuffer();
		b = false;
		try {
			Account account = (Account) content.getSessionAttributes().get(ACCOUNT);
			int taskId = (int) Integer.parseInt((String) content.getRequestAttributes().get(CMD_VALUE));
			TaskMetaDTO meta = account.getTasksMeta().get(taskId);
			TaskDTO task = account.getCurrentTasks().get(taskId);
			SimpleDateFormat dateFormat = account.getDateFormat();
			meta.setStatusId(5);// устанавливаем статус #send a document for approval
			b = updateTaskMeta(meta, task, dateFormat);
			if (b) {
				page = ConfigurationManager.getProperty("path.page.user");
				message = message.append(MessageManager.getProperty("task.update")).append(meta.getTaskId());
				System.out.println(meta.toString());
			}
			content.getSessionAttributes().put(ACCOUNT, account);
			// System.out.println("addNewTask: " + meta.getId());
		} catch (Exception e) {
			e.printStackTrace();
			message = message.append(MessageManager.getProperty("task.update.false"));
		}
		if (page == null) {
			page = ConfigurationManager.getProperty("path.page.login");
		}
		content.getSessionAttributes().put(MESSAGE, message.toString());
		return page;
	}
}
