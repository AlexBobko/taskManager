package command;

import controller.PageMapper;
import controller.RequestHandler;
import loc.task.entity.Task;
import loc.task.vo.Account;
import managers.MessageManager;
import org.apache.log4j.Logger;
import loc.task.service.TaskService;

public class TaskUpdateCommand implements ICommand {
    private static Logger log = Logger.getLogger(TaskUpdateCommand.class);

    @Override
    public String execute(RequestHandler content) {
        StringBuffer message = new StringBuffer();
        String page = null;
        Account account;
        try {
            account = (Account) content.getSessionAttributes().get(ACCOUNT);
            Task task = (Task) content.getSessionAttributes().get(TASK);
            String bodyTask = (String) content.getRequestAttributes().get(POST_BODY);
            if (TaskService.getTaskService().updateTaskBody(account,task,bodyTask)) {
                page = PageMapper.getPageMapper().getTaskDetailsPage(account.getUser().getRole());
                //TODO DEL MSG
//                System.out.println("command update task:" + task.getTaskId());
                message = message.append(MessageManager.getProperty("task.update")).append(task.getTaskId());
            }
            content.getSessionAttributes().put(ACCOUNT, account);
        } catch (Exception e) {
            log.error(e,e);
            message = message.append(MessageManager.getProperty("task.update.false"));
        }
        content.getSessionAttributes().put(MESSAGE, message.toString());
        return page;
    }
}