package command;

import controller.RequestHandler;
import dto.Account;
import managers.PageManager;

public class GoTaskListCommand implements ICommand {
    @Override
    public String execute(RequestHandler content) {
        String page=null;
        try {
            Account account = (Account) content.getSessionAttributes().get(ACCOUNT);
            int role=account.getUser().getRole();
            if (role==1){
                page=PageManager.getProperty("path.page.user");
            }else if (role==2){
                page=PageManager.getProperty("path.page.superior");
            }
        } catch (Exception e) {
        }
        return page;
    }
}
