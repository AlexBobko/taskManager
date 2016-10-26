package command;

import controller.RequestHandler;
import dto.Account;
import dto.TaskDTO;
import dto.TaskMetaDTO;
import managers.ConfigurationManager;
import managers.MessageManager;
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
            page = ConfigurationManager.getProperty("path.page.login"); // перенести??
            Account account = (Account) content.getSessionAttributes().get(ACCOUNT);
            int taskId = Integer.parseInt((String) content.getRequestAttributes().get(CMD_VALUE));
            TaskMetaDTO meta = account.getTasksMeta().get(taskId);
            TaskDTO task = account.getCurrentTasks().get(taskId);
            SimpleDateFormat dateFormat = account.getDateFormat();
            meta.setStatusId(3);// устанавливаем статус - в работе
            TaskService taskService=new TaskService();
            b = taskService.updateTaskMeta(task, meta, dateFormat);
            if (b) {
                page = ConfigurationManager.getProperty("path.page.user");
                message = message.append(MessageManager.getProperty("task.update")).append(meta.getTaskId());
//                System.out.println(meta.toString());
            }
            content.getSessionAttributes().put(ACCOUNT, account);
            // System.out.println("addNewTask: " + meta.getId());
        } catch (Exception e) {
            e.printStackTrace();
            message = message.append(MessageManager.getProperty("task.update.false"));
        }
        content.getSessionAttributes().put(MESSAGE, message.toString());
        return page;
    }
}