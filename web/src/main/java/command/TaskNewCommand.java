package command;

import controller.PageMapper;
import controller.RequestHandler;
import loc.task.entity.User;
import loc.task.services.IUserService;
import loc.task.vo.Account;
import loc.task.vo.AccountSuperior;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Go to task add page
 */
@Log4j
public class TaskNewCommand implements ICommand {
    @Autowired
    private IUserService userService;

    @Override
    public String execute(RequestHandler content) {
        //TODO закинуть в сессию список исполнителей
        String page = null;
        try {
            Account account = (Account) content.getSessionAttributes().get(ACCOUNT);
            if (account instanceof AccountSuperior) {
//                account = (AccountSuperior) account;
                System.out.println("**account instanceof AccountSuperior**");
                System.out.println("**account instanceof AccountSuperior**");
                ((AccountSuperior) account).setEmployee(userService.getAllEmployee());
                for (User user : ((AccountSuperior) account).getEmployee()) {
                    System.out.println("user.getUserId()" + user.getUserId() + ": " + user.getLogin());
                }
            }
            page = PageMapper.getPageMapper().getNewTaskPage(account.getUser().getRole());
//            employee
        } catch (Exception e) {
            log.error(e, e);
        }
        return page;
    }
}