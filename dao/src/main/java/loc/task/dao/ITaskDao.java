package loc.task.dao;

import loc.task.entity.Task;

import java.util.List;
import java.util.Set;

public interface ITaskDao extends Dao<Task> {
    Task getTaskToUser(long taskId, int userId);

    List<Task> getTasks(int firstResult, int tasksPerPage, Set<Integer> includeStatus,
                        int sort, boolean ask, Set<Integer> usersId);

    long getCountTask(Set<Integer> includeStatus, Integer userId);

    long getCountTask(Set<Integer> includeStatus);

    void replicate(Task task);
}
