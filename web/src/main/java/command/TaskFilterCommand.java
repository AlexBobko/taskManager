package command;

import controller.PageMapper;
import controller.RequestHandler;
import loc.task.services.TaskService;
import loc.task.vo.Account;
import loc.task.vo.TaskOutFilter;
import lombok.extern.log4j.Log4j;

import java.util.HashSet;
import java.util.Set;

@Log4j
public class TaskFilterCommand implements ICommand {

    @Override
    public String execute(RequestHandler content) {
        String page = null;
        try {
            Account account = (Account) content.getSessionAttributes().get(ACCOUNT);
            TaskOutFilter taskOutFilter = account.getCurrentTasksFilter();
            try {
                //TODO (spring) ошибка при не выбранных статусах, перекинуть все строки в константы
                String[] statuses = (String[]) content.getRequestAttributes().get("include_status");
                Set<Integer> includeStatus = new HashSet<>(statuses.length);
                for (String str : statuses) {
                    includeStatus.add(Integer.parseInt(str));
                }
                taskOutFilter.setIncludeStatus(includeStatus);
            } catch (IllegalArgumentException e) {
                log.error(e, e);
                //(spring) ошибка выбора статуса: обработать, добавить мессагу
//                throw new IllegalArgumentException (e);
            }
            String ask = (String) content.getRequestAttributes().get("ask");
            if (ask == null) {
                taskOutFilter.setAsk(true);
            } else taskOutFilter.setAsk(false);

            taskOutFilter.setSort(Integer.parseInt((String) content.getRequestAttributes().get("sorting_column")));
            taskOutFilter.setTasksPerPage(Integer.parseInt((String) content.getRequestAttributes().get("task_per_page")));
            TaskService.getTaskService().updateTaskList(account); //TODO (spring) Serv ex

            page= PageMapper.getPageMapper().getTaskListPage(account.getUser().getRole());
        } catch (Exception e) {
            log.error(e, e);
        }
        return page;
    }
}