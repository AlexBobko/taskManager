package command;

import controller.PageMapper;
import controller.RequestHandler;
import loc.task.vo.Account;
import lombok.extern.log4j.Log4j;
import loc.task.service.TaskService;
@Log4j
public class TaskListCommand implements ICommand {
    @Override
    public String execute(RequestHandler content) {
        String page=null;
        try {
            Account account = (Account) content.getSessionAttributes().get(ACCOUNT);
            int role=account.getUser().getRole();
            TaskService.getTaskService().updateTaskList(account);
            page= PageMapper.getPageMapper().getTaskListPage(role);
        } catch (Exception e) {
            log.error(e,e);
        }
        return page;
    }
}