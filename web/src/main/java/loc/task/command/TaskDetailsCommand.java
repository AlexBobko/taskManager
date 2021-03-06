package loc.task.command;

import loc.task.controller.PageMapper;
import loc.task.controller.RequestHandler;
import loc.task.entity.Task;
import loc.task.services.ITaskService;
import loc.task.services.exc.TaskServiceException;
import loc.task.vo.Account;
import lombok.extern.log4j.Log4j;
import loc.task.managers.MessageManager;
import org.springframework.beans.factory.annotation.Autowired;

@Log4j
public class TaskDetailsCommand implements ICommand {
	@Autowired
	private ITaskService taskService;
	@Override
	public String execute(RequestHandler content) {
		StringBuffer message = new StringBuffer();
		String page=null;
		try {
			Account account = (Account) content.getSessionAttributes().get(ACCOUNT);
			Long taskId = Long.parseLong((String) content.getRequestAttributes().get(CMD_VALUE));
			try {
				Task task = taskService.getTask(account, taskId);
				if (task!=null) {
					content.getSessionAttributes().put(TASK, task);
					account.setCurrentTasks(null); //чистим коллекции, чтобы не таскать на страницу
					account.getUser().setTaskList(null);
					page = PageMapper.getPageMapper().getTaskDetailsPage(account.getUser().getRole());
					content.getSessionAttributes().put(ACCOUNT, account);
				}
			}catch (TaskServiceException e){
				message = message.append(MessageManager.getProperty("task.detail.false"));
			//TODO не удалось получить детали таска, пользователя оставить на той же странице
//				System.out.println("TaskDetailsCommand не удалось получить детали таска");
				page = PageMapper.getPageMapper().getTaskListPage(account.getUser().getRole());
			}
		} catch (Exception e) {
			log.error(e,e);
			message = message.append(MessageManager.getProperty("task.detail.false"));
		}
		content.getSessionAttributes().put(MESSAGE, message.toString());
		return page;
	}
}
