package command;

import controller.RequestHandler;
import managers.PageManager;

/**Go to task add page*/
public class GoAddCommand implements ICommand {
    @Override
    public String execute(RequestHandler content) {
        return PageManager.getProperty("path.page.add.task");//TODO закинуть в сессию список исполнителей
    }
}
