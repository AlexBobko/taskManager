package command;

import controller.PageMapper;
import controller.RequestHandler;
import loc.task.vo.Account;
import lombok.extern.log4j.Log4j;
import managers.MessageManager;
import loc.task.service.TaskService;

/**
 * DEL устанавливаем статус 7
 */
@Log4j
public class TaskDeleteCommand implements ICommand {
    final private Integer newStatus = TaskService.statusTaskDelete;

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
