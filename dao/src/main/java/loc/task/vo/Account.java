package loc.task.vo;

import loc.task.entity.Task;
import loc.task.entity.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class Account {
    private static final long serialVersionUID = 1L;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd':'HH-mm");
    private User user;

    private List<Task> currentTasks = new ArrayList<>();
    private TaskOutFilter currentTasksFilter = new TaskOutFilter();

    private Integer id;
    private Integer levelAccess;

    public Account() {
    }
    public Account(User user) {
        this.user=user;
        this.id=user.getUserId();
        this.levelAccess=user.getRole();
    }
    public Account(User user,TaskOutFilter currentTasksFilter,List<Task> currentTasks) {
        this.user=user;
        this.id=user.getUserId();
        this.levelAccess=user.getRole();
        this.currentTasksFilter=currentTasksFilter;
        this.currentTasks=currentTasks;
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getLevelAccess() {
        return levelAccess;
    }

    public SimpleDateFormat getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(SimpleDateFormat dateFormat) {
        this.dateFormat = dateFormat;
    }

    public List<Task> getCurrentTasks() {
        return currentTasks;
    }

    public void setCurrentTasks(List<Task> currentTasks) {
        this.currentTasks = currentTasks;
    }

    public TaskOutFilter getCurrentTasksFilter() {
        return currentTasksFilter;
    }

    public void setCurrentTasksFilter(TaskOutFilter currentTasksFilter) {
        this.currentTasksFilter = currentTasksFilter;
    }
}