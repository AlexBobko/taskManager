package command;

import controller.RequestHandler;
import dto.Account;
import dto.TaskDTO;
import dto.TaskMetaDTO;
import managers.ConfigurationManager;
import managers.MessageManager;
import service.TaskService;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class TaskAddCommand extends AbsCommand {
    private static final String POST_TITLE = "titleTask";
    private static final String POST_BODY = "bodyTask";
    private static final String POST_DEADLINE = "taskDeadline";
    private String page;
    private StringBuffer message;

    public TaskAddCommand() {

    }

    @Override
    public String execute(RequestHandler content) {
        message = new StringBuffer();
        Account account = null;
        try {
            account = (Account) content.getSessionAttributes().get(ACCOUNT); // TODO nullpointer
            String titleTask = (String) content.getRequestAttributes().get(POST_TITLE);
            String bodyTask = (String) content.getRequestAttributes().get(POST_BODY);
            String strTaskDeadline = (String) content.getRequestAttributes().get(POST_DEADLINE);
            //TODO добавить проверку на дату: текущая дата+ //10/21/2016
            Pattern p = Pattern.compile("[0-9]{1,2}[/][0-9]{1,2}[/][0-9]{4}]");
            Matcher m = p.matcher(strTaskDeadline);
            if (!m.matches()) {
                message.append(MessageManager.getProperty("message.task.add.false"));
                message.append(MessageManager.getProperty("task.incorrect.deadline"));
                page = ConfigurationManager.getProperty("path.page.add.task");
                return page;
            }
            //TODO назначаем создателя исполнителем, добавить возможность назначать, если создает директор
            int userId = account.getUser().getId();
            TaskService taskService = new TaskService(titleTask, bodyTask, strTaskDeadline, userId);
            TaskDTO newTask = taskService.getCurrentTask();
            TaskMetaDTO newTaskMeta = taskService.getCurrentTaskMeta();
            if (newTask != null && newTaskMeta != null) {
                message.append(MessageManager.getProperty("message.task.add") + newTask.getId());
                account.getCurrentTasks().put(newTask.getId(), newTask);
                account.getTasksMeta().put(newTaskMeta.getTaskId(), newTaskMeta);
                content.getSessionAttributes().put(TASK, newTask);
                content.getSessionAttributes().put(TASK_META, newTaskMeta);
                page = ConfigurationManager.getProperty("path.page.task");
                System.out.println("addNewTask: " + newTask.getId()); //для лога
            } else {
                //TODO ?? пробросить Exception чтобы код не дублировать?
                message.append(MessageManager.getProperty("message.task.add.false"));
                page = ConfigurationManager.getProperty("path.page.add.task");
                return page;
            }
        } catch (Exception e) {
//			e.printStackTrace();
            message.append(MessageManager.getProperty("message.task.add.false"));
            page = ConfigurationManager.getProperty("path.page.add.task");
        } finally {
            content.getSessionAttributes().put(ACCOUNT, account);
            content.getSessionAttributes().put(MESSAGE, message.toString());//TODO переложить собщение в атрибуты
        }
        return page;
    }
}
