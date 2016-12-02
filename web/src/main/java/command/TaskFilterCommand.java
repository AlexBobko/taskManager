package command;

import controller.RequestHandler;
import loc.task.vo.Account;
import loc.task.vo.TaskOutFilter;
import managers.PageManager;
import org.apache.log4j.Logger;
import service.TaskService;

import java.util.HashSet;
import java.util.Set;

public class TaskFilterCommand implements ICommand {
    private static Logger log = Logger.getLogger(TaskFilterCommand.class); //log.error(e,e);

    @Override
    public String execute(RequestHandler content) {
        String page = null;
        try {
            Account account = (Account) content.getSessionAttributes().get(ACCOUNT);
            TaskOutFilter taskOutFilter = account.getCurrentTasksFilter();
            int role = account.getUser().getRole();
            try {
                //TODO ошибка при не выбранных статусах
                String[] statuses = (String[]) content.getRequestAttributes().get("include_status");
                Set<Integer> includeStatus = new HashSet<>(statuses.length);
                for (String str : statuses) {
                    includeStatus.add(Integer.parseInt(str));
                }
                taskOutFilter.setIncludeStatus(includeStatus);
            } catch (IllegalArgumentException e) {
                log.error(e, e); //TODO ошибка выбора статуса: обработать, добавить мессагу
//                throw new IllegalArgumentException (e);
            }
            String ask = (String) content.getRequestAttributes().get("ask");
            if (ask == null) {
                taskOutFilter.setAsk(true);
            } else taskOutFilter.setAsk(false);

            taskOutFilter.setSort(Integer.parseInt((String) content.getRequestAttributes().get("sorting_column")));
            taskOutFilter.setTasksPerPage(Integer.parseInt((String) content.getRequestAttributes().get("task_per_page")));

            TaskService.getTaskService().updateTaskList(account);
            if (role == employeeRole) {
                page = PageManager.getProperty("path.page.user");
            } else if (role == superiorRole) {
                page = PageManager.getProperty("path.page.superior");
            }
        } catch (Exception e) {
            log.error(e, e);
        }
        return page;
    }
}