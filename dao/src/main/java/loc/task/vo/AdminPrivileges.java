package loc.task.vo;

import loc.task.entity.Task;

import java.util.HashSet;
import java.util.Set;

public class AdminPrivileges implements Privileges {
    private Set<Task> tasksForApprove = new HashSet<Task>();
    private Set<Task> currentTasks = new HashSet<Task>();


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

    public Set<Task> getTasksForApprove() {
        return tasksForApprove;
    }

    @Override
    public Set<Integer> getIncludeStatus() {
        return null;
    }

    @Override
    public int getPage() {
        return 0;
    }

    @Override
    public void setPage(int page) {

    }

    @Override
    public int getTasksPerPage() {
        return 0;
    }

    @Override
    public void setTasksPerPage(int tasksPerPage) {

    }

    @Override
    public long getTotalCount() {
        return 0;
    }

    @Override
    public void setTotalCount(long totalCount) {

    }

    @Override
    public long getCountPage() {
        return 0;
    }

    @Override
    public void setCountPage(long countPage) {

    }

    @Override
    public int getSort() {
        return 0;
    }

    @Override
    public void setSort(int sort) {

    }

    @Override
    public boolean isAsk() {
        return false;
    }

    @Override
    public void setAsk(boolean ask) {

    }

    public void setTasksForApprove(Set<Task> tasksForApprove) {
        this.tasksForApprove = tasksForApprove;
    }
}
