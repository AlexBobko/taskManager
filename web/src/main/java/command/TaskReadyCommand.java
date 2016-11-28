package command;

import controller.RequestHandler;
import loc.task.vo.Account;
import managers.MessageManager;
import managers.PageManager;
import org.apache.log4j.Logger;
import service.TaskService;


/**
 * set task status 6 (ready)
 */
public class TaskReadyCommand implements ICommand {
    private static Logger log = Logger.getLogger(TaskReadyCommand.class);
    final private Integer newStatus = 6;

    @Override
    public String execute(RequestHandler content) {
        String page = null;
        StringBuffer message = new StringBuffer();
        try {
            Account account = (Account) content.getSessionAttributes().get(ACCOUNT);
            long taskId = Long.parseLong((String) content.getRequestAttributes().get(CMD_VALUE));
            if (TaskService.updateTask(account, taskId, newStatus)) {
                page = PageManager.getProperty("path.page.user");
                message = message.append(MessageManager.getProperty("task.update")).append(taskId);
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
