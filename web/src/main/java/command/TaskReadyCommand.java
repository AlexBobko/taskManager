package command;

import controller.PageMapper;
import controller.RequestHandler;
import loc.task.vo.Account;
import lombok.extern.log4j.Log4j;
import managers.MessageManager;
import service.TaskService;

/**
 * set task status 6 (ready)
 */
@Log4j
public class TaskReadyCommand implements ICommand {
    final private Integer newStatus = TaskService.statusTaskReady;

    @Override
    public String execute(RequestHandler content) {
        String page = null;
        StringBuffer message = new StringBuffer();
        try {
            Account account = (Account) content.getSessionAttributes().get(ACCOUNT);
            long taskId = Long.parseLong((String) content.getRequestAttributes().get(CMD_VALUE));
            page= PageMapper.getPageMapper().getTaskListPage(account.getUser().getRole());
            if (TaskService.getTaskService().updateTaskStatus(account, taskId, newStatus)) {
                message = message.append(MessageManager.getProperty("task.update")).append(taskId);
            }
            content.getSessionAttributes().put(ACCOUNT, account);
        } catch (Exception e) {
            log.error(e, e);
            message = message.append(MessageManager.getProperty("task.update.false"));
        }
        content.getSessionAttributes().put(MESSAGE, message.toString());
        return page;
    }
}
