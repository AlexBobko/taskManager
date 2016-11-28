package command;

import controller.RequestHandler;
import loc.task.vo.Account;
import managers.PageManager;
import org.apache.log4j.Logger;
import service.TaskService;

public class GoTaskListCommand implements ICommand {
    private static Logger log = Logger.getLogger(GoTaskListCommand.class);
    @Override
    public String execute(RequestHandler content) {
        String page=null;
        try {
            Account account = (Account) content.getSessionAttributes().get(ACCOUNT);
            int role=account.getUser().getRole();
//            TaskService taskService = new TaskService();
            TaskService.updateTaskList(account);
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