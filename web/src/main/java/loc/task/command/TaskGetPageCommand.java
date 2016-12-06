package loc.task.command;

import loc.task.controller.PageMapper;
import loc.task.controller.RequestHandler;
import loc.task.services.ITaskService;
import loc.task.vo.Account;
import loc.task.managers.MessageManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

public class TaskGetPageCommand implements ICommand {
    @Autowired
    private ITaskService taskService;

    private static Logger log = Logger.getLogger(TaskGetPageCommand.class);
    @Override
    public String execute(RequestHandler content) {
        String page = null;
        StringBuffer message = new StringBuffer();
        try {
            Account account = (Account) content.getSessionAttributes().get(ACCOUNT);
            int pageNumber = Integer.parseInt((String) content.getRequestAttributes().get(CMD_VALUE));
            account.getCurrentTasksFilter().setPage(pageNumber);
            account=taskService.updateTaskList(account);
            page= PageMapper.getPageMapper().getTaskListPage(account.getUser().getRole());
            content.getSessionAttributes().put(ACCOUNT, account);
        } catch (Exception e) {
            log.error(e, e);
            message = message.append(MessageManager.getProperty("error.illegal.operation"));
        }
        content.getSessionAttributes().put(MESSAGE, message.toString());
        return page;
    }
}