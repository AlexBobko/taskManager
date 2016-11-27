package command;

import controller.RequestHandler;
import loc.task.vo.Account;
import managers.PageManager;

public class GoTaskListCommand implements ICommand {
    @Override
    public String execute(RequestHandler content) {
        String page=null;
        try {
            Account account = (Account) content.getSessionAttributes().get(ACCOUNT);
            int role=account.getUser().getRole();
            if (role==employeeRole){

                page=PageManager.getProperty("path.page.user");
            }else if (role==superiorRole){

                page=PageManager.getProperty("path.page.superior");
            }
        } catch (Exception e) {
        }
        return page;
    }
}
