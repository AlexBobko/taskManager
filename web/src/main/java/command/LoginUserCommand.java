package command;

import controller.PageMapper;
import controller.RequestHandler;
import loc.task.vo.Account;
import lombok.extern.log4j.Log4j;
import managers.MessageManager;
import loc.task.services.UserService;

/**
 * Login
 */
@Log4j
public class LoginUserCommand implements ICommand {

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
            //TODO удалить проверку + проверка ЕХ юзер сервис
            if (account != null) {
                content.getSessionAttributes().put(ACCOUNT, account);
                page= PageMapper.getPageMapper().getTaskListPage(account.getUser().getRole());
                message.append(MessageManager.getProperty("message.true.login"));
            }else message.append(MessageManager.getProperty("message.login.error"));
        } catch (Exception e) {
            log.error(e,e);
            message.append(MessageManager.getProperty("message.login.error"));
            message.append(e);
        }
        content.getSessionAttributes().put(MESSAGE, message.toString());
        return page;
    }
}