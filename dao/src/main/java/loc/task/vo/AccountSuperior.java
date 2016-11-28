package loc.task.vo;

import loc.task.entity.Task;
import loc.task.entity.User;

import java.util.*;

public class AccountSuperior extends Account {
    private List<Task> reportTasks = new ArrayList<>();
    private TaskOutFilter reportTaskFilter = new TaskOutFilter();
    private ArrayList<User> employee = new ArrayList<>();

    public AccountSuperior() {
        super();
    }

    public AccountSuperior(User user) {
        super(user);
    }

    public AccountSuperior(User user, TaskOutFilter currentTasksFilter, List<Task> currentTasks,
                           TaskOutFilter reportTaskFilter, List<Task> reportTasks) {
        super(user, currentTasksFilter, currentTasks);
        this.reportTaskFilter = reportTaskFilter;
        this.reportTasks = reportTasks;
    }

    public List<Task> getReportTasks() {
        return reportTasks;
    }

    public void setReportTasks(List<Task> reportTasks) {
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
