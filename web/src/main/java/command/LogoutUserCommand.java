package command;

import controller.RequestHandler;
import managers.ConfigurationManager;
import managers.MessageManager;

public class LogoutUserCommand implements ICommand {
    @Override
    public String execute(RequestHandler content) {
        try {
            content.setSessionAttributes(null);
        } catch (Exception e) {
        }
        String page = ConfigurationManager.getProperty("path.page.login");
        content.getRequestAttributes().put(MESSAGE, MessageManager.getProperty("message.logout"));
        return page;
    }
}
