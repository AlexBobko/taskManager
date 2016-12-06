package loc.task.command;

import loc.task.controller.PageMapper;
import loc.task.controller.RequestHandler;
import loc.task.services.IUserService;
import loc.task.vo.Account;
import lombok.extern.log4j.Log4j;
import loc.task.managers.MessageManager;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Login
 */
@Log4j
public class LoginUserCommand implements ICommand {

    @Autowired
    private IUserService userService;

    @Override
    public String execute(RequestHandler content) {
        String page=null;
        StringBuffer message = new StringBuffer();
        Account account;
        System.out.println("login1");
        try {
            String userLogin = (String) content.getRequestAttributes().get(LOGIN);
            String userPassword = (String) content.getRequestAttributes().get(PASSWORD);
            System.out.println("login2");
            try {
                int userId = Integer.parseInt(userLogin);
                account= userService.getAccount(userId, userPassword);
            } catch (NumberFormatException e) {
                System.out.println("login3");
                account= userService.getAccount(userLogin, userPassword);
                System.out.println("login4");
            }

            //TODO удалить проверку + проверка ЕХ юзер сервис
            if (account != null) {
                content.getSessionAttributes().put(ACCOUNT, account);
                page= PageMapper.getPageMapper().getTaskListPage(account.getUser().getRole());
                message.append(MessageManager.getProperty("message.true.login"));
            }else message.append(MessageManager.getProperty("message.login.error"));
            System.out.println("login4");
        } catch (Exception e) {
            log.error(e,e);
            message.append(MessageManager.getProperty("message.login.error"));
            message.append(e);
        }
        content.getSessionAttributes().put(MESSAGE, message.toString());
        return page;
    }
}