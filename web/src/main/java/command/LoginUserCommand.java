package command;

import controller.RequestHandler;
import dto.Account;
import managers.ConfigurationManager;
import managers.MessageManager;
import service.LoginService;

/**
 * Login
 */
public class LoginUserCommand extends AbsCommand {

    private LoginService loginService;

    private static final String LOGIN = "username";
    private static final String PASSWORD = "password";
    // private UserDTO user;
    private String page;
    private StringBuffer message;

    LoginUserCommand() {
    }


    @Override
    public String execute(RequestHandler content) {
        message = new StringBuffer();
        System.out.println("It LoginUserComandImpl");
        Account account = null;
        try {
            String userLogin = (String) content.getRequestAttributes().get(LOGIN);
            String userPassword = (String) content.getRequestAttributes().get(PASSWORD);
            try {
                int userId = Integer.parseInt((String) content.getRequestAttributes().get(LOGIN));
                loginService = new LoginService(userId, userPassword);
            } catch (NumberFormatException e) {
                loginService = new LoginService(userLogin, userPassword);
            }
            account=loginService.getAccount();
            if (account != null) {
                content.getSessionAttributes().put(ACCOUNT, account);
                page = ConfigurationManager.getProperty("path.page.user");
                message.append(MessageManager.getProperty("message.true.login"));
            }else{
                message.append(MessageManager.getProperty("message.login.error"));
                page = ConfigurationManager.getProperty("path.page.login");
            }
        } catch (IllegalArgumentException e) {
            page = ConfigurationManager.getProperty("path.page.login");
            message.append(MessageManager.getProperty("message.login.error"));
        }
        content.getSessionAttributes().put(MESSAGE, message.toString());
        return page;
    }
}