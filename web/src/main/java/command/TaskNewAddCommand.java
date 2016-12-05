package command;

import controller.RequestHandler;
import loc.task.entity.Task;
import loc.task.service.TaskService;
import loc.task.service.exc.TaskServiceException;
import loc.task.vo.Account;
import lombok.extern.log4j.Log4j;
import managers.MessageManager;
import managers.PageManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Log4j
public class TaskNewAddCommand implements ICommand {
    public TaskNewAddCommand() {

    }

    @Override
    public String execute(RequestHandler content) {
        String page = PageManager.getProperty("path.page.add.task");
        StringBuffer message = new StringBuffer();
        Account account = null;
        //TODO (ТЗ) вернуть в сессию уже введенные данные
        try {
            account = (Account) content.getSessionAttributes().get(ACCOUNT);
            String titleTask = (String) content.getRequestAttributes().get(POST_TITLE);
            String bodyTask = (String) content.getRequestAttributes().get(POST_BODY);
            //текущая дата формат 10/21/2016
            String strTaskDeadline = (String) content.getRequestAttributes().get(POST_DEADLINE);
            int responsiblePersonId;
            if (account.getUser().getRole() == 2) {
                responsiblePersonId = Integer.parseInt((String) content.getRequestAttributes().get(TASK_EMPLOYEE));
            } else {
                responsiblePersonId = 0;
            }
            Date bodyDeadline;
            try {
                String uiDateFormat = "MM/dd/yyyy HH:mm:ss";
                int defaultHour = 20;
                int defaultMinute = 15;
                int defaultSecond = 0; //00??
                bodyDeadline = new SimpleDateFormat(uiDateFormat).
                        parse(strTaskDeadline.concat(" " + defaultHour + ":" + defaultMinute + ":" + defaultSecond));
                if (bodyDeadline.getTime() < (new Date().getTime())) {
                    throw new Exception(MessageManager.getProperty("task.incorrect.deadline"));
                }
                try {
                    Task newTask = TaskService.getTaskService().addNewTask(account, responsiblePersonId, titleTask, bodyTask, bodyDeadline);
                    message.append(MessageManager.getProperty("message.task.add") + newTask.getTaskId());
                    content.getSessionAttributes().put(TASK, newTask);
                    page = PageManager.getProperty("path.page.task");
                    System.out.println("addNewTask: " + newTask.getTaskId()); //для лога
                } catch (TaskServiceException e) {
                    throw new Exception(e);
                }
            } catch (ParseException e) {
                throw new Exception(e);
            }
        } catch (Exception e) { //NumberFormatException, NullPointerException
            log.error(e, e);
            message.append(MessageManager.getProperty("task.incorrect.deadline"));
            message.append(MessageManager.getProperty("message.task.add.false"));
        } finally {
            content.getSessionAttributes().put(ACCOUNT, account);
            content.getSessionAttributes().put(MESSAGE, message.toString());
        }
        return page;
    }
}
