package command;

import controller.RequestHandler;
import managers.ConfigurationManager;

public class GoTaskListCommand implements ICommand {
    @Override
    public String execute(RequestHandler content) {
        return ConfigurationManager.getProperty("path.page.user");
    }
}
