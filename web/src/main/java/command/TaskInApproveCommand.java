package command;

import controller.RequestHandler;
import loc.task.vo.Account;
import managers.MessageManager;
import managers.PageManager;
import org.apache.log4j.Logger;
import service.TaskService;

/**
 * Set task status 2
 */
public class TaskInApproveCommand implements ICommand {
    private static Logger log = Logger.getLogger(TaskInApproveCommand.class);
    final private Integer newStatus = 2;

    @Override
    public String execute(RequestHandler content) {
        String page = null;
        StringBuffer message = new StringBuffer();
        try {
            Account account = (Account) content.getSessionAttributes().get(ACCOUNT);
            long taskId = Long.parseLong((String) content.getRequestAttributes().get(CMD_VALUE));
            if (account.getUser().getRole() == employeeRole) {
                if (TaskService.getTaskService().updateTask(account, taskId, newStatus)) {
                    page = PageManager.getProperty("path.page.user");
                    message = message.append(MessageManager.getProperty("task.update")).append(taskId);
                }
            } else if (account.getUser().getRole() == superiorRole) {

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