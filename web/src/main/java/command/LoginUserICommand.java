package command;

import controller.RequestHandler;
import dto.Account;
import managers.PageManager;
import managers.MessageManager;
import service.LoginService;

/**
 * Login
 */
public class LoginUserICommand implements ICommand {

    private LoginService loginService;

    private static final String LOGIN = "username";
    private static final String PASSWORD = "password";

    LoginUserICommand() {
    }

    @Override
    public String execute(RequestHandler content) {
        String page=null;
        StringBuffer message = new StringBuffer();
//        System.out.println("It LoginUserComandImpl");
        Account account;
        try {
            String userLogin = (String) content.getRequestAttributes().get(LOGIN);
            String userPassword = (String) content.getRequestAttributes().get(PASSWORD);
            LoginService loginService = new LoginService();
            try {
                int userId = Integer.parseInt(userLogin);
                account= loginService.getAccount(userId, userPassword);
            } catch (NumberFormatException e) {
                account= loginService.getAccount(userLogin, userPassword);
            }
            if (account != null) {
                content.getSessionAttributes().put(ACCOUNT, account);
                if (account.getUser().getRole() == 1) {
                    page=PageManager.getProperty("path.page.user");
                }else if (account.getUser().getRole() == 2){
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