package command;

import controller.RequestHandler;
import loc.task.vo.Account;
import managers.PageManager;
import org.apache.log4j.Logger;
import service.TaskService;

public class TaskListCommand implements ICommand {
    private static Logger log = Logger.getLogger(TaskListCommand.class);
    @Override
    public String execute(RequestHandler content) {
        String page=null;
        try {
            Account account = (Account) content.getSessionAttributes().get(ACCOUNT);
            int role=account.getUser().getRole();
//            TaskService taskService = new TaskService();
            TaskService.getTaskService().updateTaskList(account);
            if (role==employeeRole){
                page=PageManager.getProperty("path.page.user");
            }else if (role==superiorRole){
                page=PageManager.getProperty("path.page.superior");
            }
        } catch (Exception e) {
            log.error(e,e);
        }
        return page;
    }
}