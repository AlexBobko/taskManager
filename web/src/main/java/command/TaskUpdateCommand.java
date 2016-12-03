package command;

import controller.RequestHandler;
import loc.task.entity.Task;
import loc.task.vo.Account;
import managers.MessageManager;
import managers.PageManager;
import org.apache.log4j.Logger;
import service.TaskService;

// TODO ВНЕСТИ обновление таска в список выводимых тасков! кэш?

public class TaskUpdateCommand implements ICommand {
    private static Logger log = Logger.getLogger(TaskUpdateCommand.class);

    @Override
    public String execute(RequestHandler content) {
        StringBuffer message = new StringBuffer();
        String page = null;
        Account account = null;
        try {
            account = (Account) content.getSessionAttributes().get(ACCOUNT);
            Task task = (Task) content.getSessionAttributes().get(TASK);
            String bodyTask = (String) content.getRequestAttributes().get(POST_BODY);
            if (TaskService.getTaskService().updateTaskBody(account,task,bodyTask)) {

                System.out.println("command update task:" + task.getTaskId());

                page = PageManager.getProperty("path.page.task");
                message = message.append(MessageManager.getProperty("task.update")).append(task.getTaskId());
            }
            content.getSessionAttributes().put(ACCOUNT, account);
        } catch (Exception e) {
            log.error(e,e);
            message = message.append(MessageManager.getProperty("task.update.false"));
        }
        content.getSessionAttributes().put(MESSAGE, message.toString());
        return page;
    }
}
