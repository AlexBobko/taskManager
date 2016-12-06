package command;

import controller.PageMapper;
import controller.RequestHandler;
import loc.task.services.ITaskService;
import loc.task.services.exc.TaskServiceException;
import loc.task.vo.Account;
import lombok.extern.log4j.Log4j;
import managers.MessageManager;
import loc.task.services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Task set checking status 4
 */
@Log4j
public class TaskInReviewCommand implements ICommand {

    @Autowired
    private ITaskService taskService;
    final private Integer newStatus = TaskService.statusTaskReview;
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
