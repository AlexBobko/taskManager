package loc.task.command;

import loc.task.controller.PageMapper;
import loc.task.controller.RequestHandler;
import loc.task.entity.Task;
import loc.task.services.ITaskService;
import loc.task.services.exc.TaskServiceException;
import loc.task.vo.Account;
import lombok.extern.log4j.Log4j;
import loc.task.managers.MessageManager;
import org.springframework.beans.factory.annotation.Autowired;

@Log4j
public class TaskUpdateCommand implements ICommand {

    @Autowired
    private ITaskService taskService;

    @Override
    public String execute(RequestHandler content) {
        StringBuffer message = new StringBuffer();
        String page = null;
        Account account;
        try {
            account = (Account) content.getSessionAttributes().get(ACCOUNT);
            Task task = (Task) content.getSessionAttributes().get(TASK);
            String bodyTask = (String) content.getRequestAttributes().get(POST_BODY);
            try {
                taskService.updateTaskBody(account, task, bodyTask);
            } catch (TaskServiceException e) {
                message = message.append(MessageManager.getProperty("task.update.false"));
                content.getSessionAttributes().put(POST_BODY, bodyTask); //TODO вывести на UI
            }
            page = PageMapper.getPageMapper().getTaskDetailsPage(account.getUser().getRole());
            //TODO DEL MSG
//                System.out.println("loc.task.command update task:" + task.getTaskId());
            message = message.append(MessageManager.getProperty("task.update")).append(task.getTaskId());

            content.getSessionAttributes().put(ACCOUNT, account);
        } catch (Exception e) {
            log.error(e, e);
            message = message.append(MessageManager.getProperty("task.update.false"));

            message = message.append(e);
        }
        content.getSessionAttributes().put(MESSAGE, message.toString());
        return page;
    }
}