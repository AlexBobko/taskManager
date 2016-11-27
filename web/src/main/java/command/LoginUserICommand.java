package command;

import controller.RequestHandler;
import loc.task.vo.Account;
import managers.MessageManager;
import managers.PageManager;
import service.UserService;

/**
 * Login
 */
public class LoginUserICommand implements ICommand {

//    private LoginService loginService;

    private static final String LOGIN = "username";
    private static final String PASSWORD = "password";
    private static final int employeeRole =1;
    private static final int superiorRole =2;

    LoginUserICommand() {
    }

    @Override
    public String execute(RequestHandler content) {

        String page=null;
        StringBuffer message = new StringBuffer();
        Account account;
        try {
            String userLogin = (String) content.getRequestAttributes().get(LOGIN);
            String userPassword = (String) content.getRequestAttributes().get(PASSWORD);
            UserService userService = new UserService();
            try {
                int userId = Integer.parseInt(userLogin);
                account= userService.getAccount(userId, userPassword);
            } catch (NumberFormatException e) {
                account= userService.getAccount(userLogin, userPassword);
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
            page = PageManager.getProperty("path.page.login");
            message.append(MessageManager.getProperty("message.login.error"));
        }
        content.getSessionAttributes().put(MESSAGE, message.toString());
//        content.getRequestAttributes().put(MESSAGE, message.toString());
        return page;
    }
}