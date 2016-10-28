package command;

import controller.RequestHandler;
import dto.Account;
import dto.TaskDTO;
import dto.TaskMetaDTO;
import managers.MessageManager;
import managers.PageManager;
import service.TaskService;

import java.text.SimpleDateFormat;

/**
 * set TaskInProduction 3
 */
public class TaskInProductionCommand implements ICommand {
    private String page;
    private StringBuffer message;

    @Override
    public String execute(RequestHandler content) {
        boolean b;
        message = new StringBuffer();
        try {
            Account account = (Account) content.getSessionAttributes().get(ACCOUNT);
            if (account.getUser().getRole() != 2) {
                message = message.append(MessageManager.getProperty("error.illegal.operation"));
                return PageManager.getProperty("path.page.user");
            }
            int taskId = Integer.parseInt((String) content.getRequestAttributes().get(CMD_VALUE));
            TaskMetaDTO meta = account.getTasksMeta().get(taskId);
            TaskDTO task = account.getCurrentTasks().get(taskId);
            SimpleDateFormat dateFormat = account.getDateFormat();
            meta.setStatusId(3);// устанавливаем статус - в работе
            TaskService taskService = new TaskService();
            b = taskService.updateTaskMeta(task, meta, dateFormat);
            if (b) {
                page = PageManager.getProperty("path.page.superior");
                message = message.append(MessageManager.getProperty("task.update")).append(meta.getTaskId());
//                System.out.println(meta.toString());
            }
            content.getSessionAttributes().put(ACCOUNT, account);
        } catch (Exception e) {
//            e.printStackTrace();
            message = message.append(MessageManager.getProperty("task.update.false"));
        }
        content.getSessionAttributes().put(MESSAGE, message.toString());
        return page;
    }
}