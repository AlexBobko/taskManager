package loc.task.vo;

import loc.task.entity.Task;

import java.util.Set;

public interface Privileges {
    Set<Task> getCurrentTasks();

    Set<Task> getActiveTasks();

    Set<Task> getArhiveTasks();

    Set<Task> getDeleteTasks();

    Set<Integer> getIncludeStatus();

    int getPage();

    void setPage(int page);

    int getTasksPerPage();

    void setTasksPerPage(int tasksPerPage);

    long getTotalCount();

    void setTotalCount(long totalCount);

    long getCountPage();

    void setCountPage(long countPage);

    int getSort();

    void setSort(int sort);

    boolean isAsk();

    void setAsk(boolean ask);
//    Set<Integer> getIncludeStatus();
//    setIncludeStatus(Set<Integer> includeStatus);

}
