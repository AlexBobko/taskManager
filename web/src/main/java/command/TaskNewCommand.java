package command;

import controller.PageMapper;
import controller.RequestHandler;
import loc.task.vo.Account;
import lombok.extern.log4j.Log4j;

/**Go to task add page*/
@Log4j
public class TaskNewCommand implements ICommand {
    @Override
    public String execute(RequestHandler content) {
        //TODO закинуть в сессию список исполнителей
        String page = null;
        try {
            Account account = (Account) content.getSessionAttributes().get(ACCOUNT);
            page = PageMapper.getPageMapper().getNewTaskPage(account.getUser().getRole());

        }catch (Exception e)
        {
            log.error(e,e);
        }
        return page;
    }
}