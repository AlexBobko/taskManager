package loc.task.command;

import loc.task.controller.RequestHandler;
import loc.task.managers.PageManager;
import loc.task.managers.MessageManager;

public class LogoutUserCommand implements ICommand {
    @Override
    public String execute(RequestHandler content) {
        try {
            content.setSessionAttributes(null);
        } catch (Exception e) {
        }
        String page = PageManager.getProperty("path.page.login");
        content.getRequestAttributes().put(MESSAGE, MessageManager.getProperty("message.logout"));
        return page;
    }
}
