package loc.task.vo;

import loc.task.entity.Task;

import java.util.Set;

//TODO сделать геттеры и сеттеры через аннотаци
public class EmployeePrivileges implements Privileges {
    private Set<Integer> includeStatus;
    private Set<Task> currentTasks;
    private int page = 1;
    private int tasksPerPage = 12;
    private long totalCount;
    long countPage;
    private int sort = 1;
    boolean ask = true;

    public EmployeePrivileges() {
    }

    public EmployeePrivileges(long totalCount,Set<Integer> includeStatus,long countPage ,Set<Task> currentTasks) {
        this.totalCount=totalCount;
        this.includeStatus=includeStatus;
        this.countPage=countPage;
        this.currentTasks=currentTasks;
    }

    @Override
    public Set<Task> getActiveTasks() {
        return null;
    }

    @Override
    public Set<Task> getArhiveTasks() {
        return null;
    }

    @Override
    public Set<Task> getDeleteTasks() {
        return null;
    }

    public Set<Task> getCurrentTasks() {
        return currentTasks;
    }

    public void setCurrentTasks(Set<Task> currentTasks) {
        this.currentTasks = currentTasks;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTasksPerPage() {
        return tasksPerPage;
    }

    public void setTasksPerPage(int tasksPerPage) {
        this.tasksPerPage = tasksPerPage;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }

    public long getCountPage() {
        return countPage;
    }

    public void setCountPage(long countPage) {
        this.countPage = countPage;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public boolean isAsk() {
        return ask;
    }

    public void setAsk(boolean ask) {
        this.ask = ask;
    }

    public Set<Integer> getIncludeStatus() {
        return includeStatus;
    }

    public void setIncludeStatus(Set<Integer> includeStatus) {
        this.includeStatus = includeStatus;
    }
}
