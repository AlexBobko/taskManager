package loc.task.vo;

import java.util.Set;

//TODO сделать геттеры и сеттеры через аннотации ломбук
public class TaskOutFilter {
    private Set<Integer> includeStatus ; //= new HashSet<>(6)
//    private Set<Task> currentTasks;
    private int page = 1;
    private int tasksPerPage = 3;
    private long totalCount;
    long countPage;
    private int sort = 1;
    boolean ask = true;

    public TaskOutFilter() {
    }

    public TaskOutFilter(Set<Integer> includeStatus) {
        this.includeStatus=includeStatus;
    }

    public TaskOutFilter(long totalCount, Set<Integer> includeStatus, long countPage) {
        this.totalCount=totalCount;
        this.includeStatus=includeStatus;
        this.countPage=countPage;

    }
    public TaskOutFilter(long totalCount, Set<Integer> includeStatus, long countPage,  int tasksPerPage) {
        this.tasksPerPage = tasksPerPage;
        this.totalCount=totalCount;
        this.includeStatus=includeStatus;
        this.countPage=countPage;

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
    public boolean getAsk() {
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

    @Override
    public String toString() {
        return "TaskOutFilter{" +
                "page=" + page +
                ", tasksPerPage=" + tasksPerPage +
                ", totalCount=" + totalCount +
                ", countPage=" + countPage +
                ", sort=" + sort +
                ", ask=" + ask +
                '}';
    }
}
