package command;

import controller.PageMapper;
import controller.RequestHandler;
import loc.task.vo.Account;
import lombok.extern.log4j.Log4j;
import managers.MessageManager;
import managers.PageManager;
import loc.task.service.TaskService;

/**
 * назначить время приема 5
 */
//TODO !! дату приема вписать еще одно поле??
@Log4j
public class TaskInReportingCommand implements ICommand {
    final private Integer newStatus = TaskService.statusTaskReport;

    @Override
    public String execute(RequestHandler content) {
        String page = null;
        StringBuffer message = new StringBuffer();
        try {
            Account account = (Account) content.getSessionAttributes().get(ACCOUNT);
            long taskId = Long.parseLong((String) content.getRequestAttributes().get(CMD_VALUE));
            if (TaskService.getTaskService().updateTaskStatus(account, taskId, newStatus)) {
                message = message.append(MessageManager.getProperty("task.update")).append(taskId);
            }
            page= PageMapper.getPageMapper().getTaskListPage(account.getUser().getRole());
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
