package loc.task.vo;

import loc.task.entity.Task;
import loc.task.entity.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class AccountSuperior extends Account {
    private Map<Long, Task> reportTasks = new HashMap<>();
    private TaskOutFilter reportTaskFilter = new TaskOutFilter();
    private ArrayList<User> employee = new ArrayList<>();

    public AccountSuperior() {
        super();
    }

    public AccountSuperior(User user) {
        super(user);
    }

    public AccountSuperior(User user, TaskOutFilter currentTasksFilter, Map<Long, Task> currentTasks,
                           TaskOutFilter reportTaskFilter, Map<Long, Task> reportTasks) {
        super(user, currentTasksFilter, currentTasks);
        this.reportTaskFilter = reportTaskFilter;
        this.reportTasks = reportTasks;
    }

    public Map<Long, Task> getReportTasks() {
        return reportTasks;
    }

    public void setReportTasks(TreeMap<Long, Task> reportTasks) {
        this.reportTasks = reportTasks;
    }

    public TaskOutFilter getReportTaskFilter() {
        return reportTaskFilter;
    }

    public void setReportTaskFilter(TaskOutFilter reportTaskFilter) {
        this.reportTaskFilter = reportTaskFilter;
    }

    public ArrayList<User> getEmployee() {
        return employee;
    }

    public void setEmployee(ArrayList<User> employee) {
        this.employee = employee;
    }
}
