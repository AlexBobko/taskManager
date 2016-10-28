package command;

import controller.RequestHandler;
import dto.Account;
import dto.TaskDTO;
import dto.TaskMetaDTO;
import managers.PageManager;
import managers.MessageManager;
import service.TaskService;

import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class TaskAddCommand implements ICommand {
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
                responsiblePersonId =account.getUser().getId();
            }

            TaskService taskService = new TaskService();
            GregorianCalendar taskDeadline = taskService.convertDate(strTaskDeadline);
            TaskDTO newTask=new TaskDTO (titleTask, bodyTask, taskDeadline);
            TaskMetaDTO newTaskMeta =new TaskMetaDTO(0, responsiblePersonId, 1);
            if (taskService.addNewTask(newTask,newTaskMeta)){
                message.append(MessageManager.getProperty("message.task.add") + newTask.getId());
                account.getCurrentTasks().put(newTask.getId(), newTask);
                account.getTasksMeta().put(newTaskMeta.getTaskId(), newTaskMeta);
                content.getSessionAttributes().put(TASK, newTask);
                content.getSessionAttributes().put(TASK_META, newTaskMeta);
                page = PageManager.getProperty("path.page.task");
                System.out.println("addNewTask: " + newTask.getId()); //для лога
            } else {
                //TODO ?? пробросить Exception чтобы код не дублировать?
                message.append(MessageManager.getProperty("message.task.add.false"));
                page = PageManager.getProperty("path.page.add.task");
                return page;
            }
        } catch (Exception e) {
//			e.printStackTrace();
            message.append(MessageManager.getProperty("message.task.add.false"));
            page = PageManager.getProperty("path.page.add.task");
        } finally {
            content.getSessionAttributes().put(ACCOUNT, account);
            content.getSessionAttributes().put(MESSAGE, message.toString());//TODO переложить все собщения в атрибуты
        }
        return page;
    }
}
