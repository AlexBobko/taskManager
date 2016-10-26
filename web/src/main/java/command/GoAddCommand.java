package command;

import controller.RequestHandler;
import managers.ConfigurationManager;

/**Перенести в гет*/
public class GoAddCommand implements ICommand {
    @Override
    public String execute(RequestHandler content) {
        return ConfigurationManager.getProperty("path.page.add.task");
    }
}
