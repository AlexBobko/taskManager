package loc.task.command;

import loc.task.controller.PageMapper;
import loc.task.controller.RequestHandler;
import loc.task.services.ITaskService;
import loc.task.vo.Account;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;

@Log4j
public class TaskListCommand implements ICommand {

    @Autowired
    private ITaskService taskService;
    @Override
    public String execute(RequestHandler content) {
        String page=null;
        try {
            Account account = (Account) content.getSessionAttributes().get(ACCOUNT);
            int role=account.getUser().getRole();
            taskService.updateTaskList(account);
            page= PageMapper.getPageMapper().getTaskListPage(role);
        } catch (Exception e) {
            log.error(e,e);
        }
        return page;
    }
}