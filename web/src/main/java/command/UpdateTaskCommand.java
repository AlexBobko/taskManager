package command;

import controller.RequestHandler;
import dto.Account;
import dto.TaskDTO;
import dto.TaskMetaDTO;
import managers.PageManager;
import managers.MessageManager;
import service.TaskService;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class UpdateTaskCommand implements ICommand {
    @Override
    public String execute(RequestHandler content) {
        StringBuffer message = new StringBuffer();
        String page = null;
        boolean b = false;
        Account account = null;
        try {
            account = (Account) content.getSessionAttributes().get(ACCOUNT);
            TaskDTO task = (TaskDTO) content.getSessionAttributes().get(TASK);
            TaskMetaDTO meta = (TaskMetaDTO) content.getSessionAttributes().get(TASK_META);
            String bodyTask = (String) content.getRequestAttributes().get(POST_BODY);
            SimpleDateFormat dateFormat = account.getDateFormat();
            int userId = account.getUser().getId();
            meta.setStatusId(2);// устанавливаем статус на проверке
            String currentTaskBody = task.getBody();
            Calendar calendar = Calendar.getInstance();
            currentTaskBody = currentTaskBody.concat("\n\r").concat(dateFormat.format(calendar.getTime())).concat(" user:" + userId).concat(bodyTask);
            task.setBody(currentTaskBody);
            TaskService taskService = new TaskService();
            b = taskService.updateTaskBody(task, meta, dateFormat);
            if (b) {
                page = PageManager.getProperty("path.page.task");
                message = message.append(MessageManager.getProperty("task.update")).append(meta.getTaskId());
                System.out.println(meta.toString());
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
