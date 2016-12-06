package loc.task.command;

import loc.task.controller.PageMapper;
import loc.task.controller.RequestHandler;
import loc.task.services.ITaskService;
import loc.task.services.TaskService;
import loc.task.services.exc.TaskServiceException;
import loc.task.vo.Account;
import lombok.extern.log4j.Log4j;
import loc.task.managers.MessageManager;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * DEL устанавливаем статус 7
 */
@Log4j
public class TaskDeleteCommand implements ICommand {
    final private Integer newStatus = TaskService.statusTaskDelete;

    @Autowired
    private ITaskService taskService;

    @Override
    public String execute(RequestHandler content) {
        String page = null;
        StringBuffer message = new StringBuffer();
        try {
            Account account = (Account) content.getSessionAttributes().get(ACCOUNT);
            long taskId = Long.parseLong((String) content.getRequestAttributes().get(CMD_VALUE));
            try {
                taskService.updateTaskStatus(account, taskId, newStatus);
                message = message.append(MessageManager.getProperty("task.update")).append(taskId);
            }catch (TaskServiceException e){
                message = message.append(MessageManager.getProperty("task.update.false"));
            }
            page = PageMapper.getPageMapper().getTaskListPage(account.getUser().getRole());
            content.getSessionAttributes().put(ACCOUNT, account);
        } catch (Exception e) {
            log.error(e, e);
            //TODO Exception на UI для простоты ;)
            message = message.append(e);
        }
        content.getSessionAttributes().put(MESSAGE, message.toString());
        return page;
    }
}
