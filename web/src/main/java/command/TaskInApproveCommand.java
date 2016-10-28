package command;

import controller.RequestHandler;
import dto.Account;
import dto.TaskDTO;
import dto.TaskMetaDTO;
import managers.PageManager;
import managers.MessageManager;
import service.TaskService;

import java.text.SimpleDateFormat;

/** Set task status 2 */
public class TaskInApproveCommand implements ICommand {

	private String page;
	private StringBuffer message;
	private boolean b;

	@Override
	public String execute(RequestHandler content) {
		message = new StringBuffer();
		b = false;
		try {
			Account account = (Account) content.getSessionAttributes().get(ACCOUNT);
			int taskId = Integer.parseInt((String) content.getRequestAttributes().get(CMD_VALUE));
			TaskMetaDTO meta = account.getTasksMeta().get(taskId);
			TaskDTO task = account.getCurrentTasks().get(taskId);
			SimpleDateFormat dateFormat = account.getDateFormat();
			meta.setStatusId(2);// устанавливаем статус #send a document for approval
			TaskService taskService=new TaskService();
			b = taskService.updateTaskMeta(task, meta, dateFormat);
			if (b) {
				page = PageManager.getProperty("path.page.user");
				message = message.append(MessageManager.getProperty("task.update")).append(meta.getTaskId());
				System.out.println(meta.toString());
			}
			content.getSessionAttributes().put(ACCOUNT, account);
			// System.out.println("addNewTask: " + meta.getId());
		} catch (Exception e) {
//			e.printStackTrace();
			message = message.append(MessageManager.getProperty("task.update.false"));
		}
		if (page == null) {
			page = PageManager.getProperty("path.page.login");
		}
		content.getSessionAttributes().put(MESSAGE, message.toString());
		return page;
	}

}
