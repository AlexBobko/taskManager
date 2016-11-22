package command;

import controller.RequestHandler;
import loc.task.db.exceptions.DaoException;
import loc.task.entity.Task;
import loc.task.vo.Account;
import managers.MessageManager;
import managers.PageManager;
import org.apache.log4j.Logger;
import service.TaskService;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class TaskAddCommand implements ICommand {
    private static Logger log = Logger.getLogger(TaskAddCommand.class);
    private String page;
    private StringBuffer message;

    public TaskAddCommand() {

    }

    @Override
    public String execute(RequestHandler content) {
        message = new StringBuffer();
        Account account = null;
        try {
            account = (Account) content.getSessionAttributes().get(ACCOUNT);
            String titleTask = (String) content.getRequestAttributes().get(POST_TITLE);
            String bodyTask = (String) content.getRequestAttributes().get(POST_BODY);
            String strTaskDeadline = (String) content.getRequestAttributes().get(POST_DEADLINE);
            //TODO добавить проверку на дату: текущая дата+ //10/21/2016
            Pattern p = Pattern.compile("[0-9]{1,2}[/][0-9]{1,2}[/][0-9]{4}");
            Matcher m = p.matcher(strTaskDeadline);
            if (!m.matches()) {
                message.append(MessageManager.getProperty("message.task.add.false"));
                message.append(MessageManager.getProperty("task.incorrect.deadline"));
                page = PageManager.getProperty("path.page.add.task");
                return page;
            }
            int responsiblePersonId;
            if (account.getUser().getRole()==2){
                responsiblePersonId= Integer.parseInt((String) content.getRequestAttributes().get(TASK_EMPLOYEE));
            }else {
                responsiblePersonId = 0;
            }
            try {
                TaskService taskService = new TaskService();
                Task newTask = taskService.addNewTask(account, responsiblePersonId, titleTask, bodyTask, strTaskDeadline);
                message.append(MessageManager.getProperty("message.task.add") + newTask.getTaskId());
                content.getSessionAttributes().put(TASK, newTask);
                page = PageManager.getProperty("path.page.task");
                System.out.println("addNewTask: " + newTask.getTaskId()); //для лога
            }catch (DaoException e)
            {
                log.error(e, e);
            }
        } catch (Exception e) {
            log.error(e, e);
            message.append(MessageManager.getProperty("message.task.add.false"));
            page = PageManager.getProperty("path.page.add.task");
        } finally {
            content.getSessionAttributes().put(ACCOUNT, account);
            content.getSessionAttributes().put(MESSAGE, message.toString());
        }
        return page;
    }
}
