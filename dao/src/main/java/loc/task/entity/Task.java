package loc.task.entity;

import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table//(name = "task")
@org.hibernate.annotations.Cache(usage=CacheConcurrencyStrategy.READ_ONLY, region="task")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_id")
    private long taskId;

    @Column (name = "status_id")
    private int statusId = 1;   // 7 с удалением

    @Temporal(TemporalType.TIMESTAMP )
    @Column(name = "date_creation",length = 19)
    private Date dateCreation;

    @Column(name = "title")
    private String title;

    @Temporal(TemporalType.TIMESTAMP )
    @Column(name = "deadline",length = 19)
    private Date deadline;

    @Basic(fetch = FetchType.LAZY)
    @Embedded
    private TaskContent content;


    @ManyToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinTable(name = "user_task",
            joinColumns = @JoinColumn(name = "task_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> personList = new HashSet<User>();


    public Task() {
    }

    public Task(int statusId, Date dateCreation, String title, Date deadline, TaskContent content, Set<User> personList) {
        this.statusId = statusId;
        this.dateCreation = dateCreation;
        this.title = title;
        this.deadline = deadline;
        this.content = content;
        this.personList = personList;
    }

    public long getTaskId() {
        return taskId;
    }

    public void setTaskId(long taskId) {
        this.taskId = taskId;
    }

    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public TaskContent getContent() {
        return content;
    }

    public void setContent(TaskContent content) {
        this.content = content;
    }

    public Set<User> getPersonList() {
        return personList;
    }

    public void setPersonList(Set<User> personList) {
        this.personList = personList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Task)) return false;

        Task task = (Task) o;

        if (getTaskId() != task.getTaskId()) return false;
        if (getStatusId() != task.getStatusId()) return false;
        if (!getDateCreation().equals(task.getDateCreation())) return false;
        if (!getTitle().equals(task.getTitle())) return false;
        if (!getDeadline().equals(task.getDeadline())) return false;
        return getContent().equals(task.getContent());

    }

    @Override
    public int hashCode() {
        int result = (int) (getTaskId() ^ (getTaskId() >>> 32));
        result = 31 * result + getStatusId();
        result = 31 * result + getDateCreation().hashCode();
        result = 31 * result + getTitle().hashCode();
        result = 31 * result + getDeadline().hashCode();
        result = 31 * result + getContent().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Task{" +
                "taskId=" + taskId +
                ", statusId=" + statusId +
                ", dateCreation=" + dateCreation +
                ", title='" + title + '\'' +
                '}';
    }
}