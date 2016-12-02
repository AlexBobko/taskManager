package command;

import controller.RequestHandler;
import loc.task.vo.Account;
import managers.MessageManager;
import managers.PageManager;
import org.apache.log4j.Logger;
import service.UserService;

/**
 * Login
 */
public class LoginUserCommand implements ICommand {

    private static Logger log = Logger.getLogger(LoginUserCommand.class);

    private static final String LOGIN = "username";
    private static final String PASSWORD = "password";
    private static final int employeeRole =1;
    private static final int superiorRole =2;

    LoginUserCommand() {
    }

    @Override
    public String execute(RequestHandler content) {

        String page=null;
        StringBuffer message = new StringBuffer();
        Account account;
        try {
            String userLogin = (String) content.getRequestAttributes().get(LOGIN);
            String userPassword = (String) content.getRequestAttributes().get(PASSWORD);
            try {
                int userId = Integer.parseInt(userLogin);
                account= UserService.getUserService().getAccount(userId, userPassword);
            } catch (NumberFormatException e) {
                account= UserService.getUserService().getAccount(userLogin, userPassword);
            }
            if (account != null) {
                content.getSessionAttributes().put(ACCOUNT, account);
                if (account.getUser().getRole() == employeeRole) {
                    page=PageManager.getProperty("path.page.user");
                }else if (account.getUser().getRole() == superiorRole){
                    page=PageManager.getProperty("path.page.superior");
                }
                message.append(MessageManager.getProperty("message.true.login"));
            }else{
                message.append(MessageManager.getProperty("message.login.error"));
                page = PageManager.getProperty("path.page.login");
            }
        } catch (IllegalArgumentException e) {
            log.error(e,e);
            page = PageManager.getProperty("path.page.login");
            message.append(MessageManager.getProperty("message.login.error"));
        }
        content.getSessionAttributes().put(MESSAGE, message.toString());
//        content.getRequestAttributes().put(MESSAGE, message.toString());
        return page;
    }
}