package command;

import controller.RequestHandler;
import loc.task.entity.Task;
import loc.task.vo.Account;
import managers.MessageManager;
import managers.PageManager;
import org.apache.log4j.Logger;
import service.TaskService;




// TODO ВНЕСТИ обновление таска в список выводимых тасков! кэш?




public class UpdateTaskCommand implements ICommand {
    private static Logger log = Logger.getLogger(UpdateTaskCommand.class);
    final private Integer newTaskStatus = 2;

    @Override
    public String execute(RequestHandler content) {
        StringBuffer message = new StringBuffer();
        String page = null;
        Account account = null;
        try {
            account = (Account) content.getSessionAttributes().get(ACCOUNT);
            Task task = (Task) content.getSessionAttributes().get(TASK);
//            TaskMetaDTO meta = (TaskMetaDTO) content.getSessionAttributes().get(TASK_META);
            String bodyTask = (String) content.getRequestAttributes().get(POST_BODY);
            TaskService taskService = new TaskService();
            if (taskService.updateTaskBody(account,task,bodyTask, newTaskStatus)) {

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
