package command;

import controller.RequestHandler;
import loc.task.entity.Task;
import loc.task.vo.Account;
import managers.MessageManager;
import managers.PageManager;
import org.apache.log4j.Logger;
import service.TaskService;




// TODO ВНЕСТИ обновление таска в список выводимых тасков! кэш?




public class TaskUpdateCommand implements ICommand {
    private static Logger log = Logger.getLogger(TaskUpdateCommand.class);


    @Override
    public String execute(RequestHandler content) {
        StringBuffer message = new StringBuffer();
        String page = null;
        Account account = null;
        try {
            account = (Account) content.getSessionAttributes().get(ACCOUNT);
            Task task = (Task) content.getSessionAttributes().get(TASK);
            String bodyTask = (String) content.getRequestAttributes().get(POST_BODY);
            if (TaskService.getTaskService().updateTaskBody(account,task,bodyTask, TaskService.statusTaskNew)) {
                System.out.println("1" + task);

                page = PageManager.getProperty("path.page.task");
                message = message.append(MessageManager.getProperty("task.update")).append(task.getTaskId());
//                System.out.println(meta.toString());
            }
            content.getSessionAttributes().put(ACCOUNT, account);
            // System.out.println("addNewTask: " + meta.getId());
        } catch (Exception e) {
            log.error(e,e);
            message = message.append(MessageManager.getProperty("task.update.false"));
        }
        content.getSessionAttributes().put(MESSAGE, message.toString());
        return page;
    }
}
