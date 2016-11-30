package command;

import controller.RequestHandler;
import loc.task.vo.Account;
import managers.MessageManager;
import managers.PageManager;
import org.apache.log4j.Logger;
import service.TaskService;

public class TaskGetPageCommand implements ICommand {
    private static Logger log = Logger.getLogger(TaskGetPageCommand.class);
    @Override
    public String execute(RequestHandler content) {
        String page = null;
        StringBuffer message = new StringBuffer();
        try {
            Account account = (Account) content.getSessionAttributes().get(ACCOUNT);
            int pageNumber = Integer.parseInt((String) content.getRequestAttributes().get(CMD_VALUE));
            account.getCurrentTasksFilter().setPage(pageNumber);
            account=TaskService.updateTaskList(account);
            page = PageManager.getProperty("path.page.user");
            content.getSessionAttributes().put(ACCOUNT, account);
        } catch (Exception e) {
            log.error(e, e);
            message = message.append(MessageManager.getProperty("error.illegal.operation"));
        }
        if (page == null) {
            page = PageManager.getProperty("path.page.login");
        }
        content.getSessionAttributes().put(MESSAGE, message.toString());
        return page;
    }
}