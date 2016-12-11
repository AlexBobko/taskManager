package loc.task.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
//@org.hibernate.annotations.Cache(usage= CacheConcurrencyStrategy.READ_ONLY, region="task")
public class TaskContent implements Serializable {
    private static final long serialVersionUID = 1L; //геттеры

    //TODO ??бд тип: меняю в бд на тип поля TEXT не грузится + меняю на аннотирование через ГЕТ методов и тоже ошибка
    @Column(name = "task_body") //    @Column(name = "task_body") //    (columnDefinition = "text")
    private String body;
    public String getBody() {return body;}

    @Column(name = "task_history")//    @Column(name = "task_history") (columnDefinition = "text")
    private String history=new String();
    public String getHistory() {return history;}

    public TaskContent() {}

    public TaskContent(String body) {
        this.body = body;
    }

    public void setBody(String body) {
        this.body = body;
    }
    public void setHistory(String history) {
        this.history = history;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TaskContent)) return false;

        TaskContent that = (TaskContent) o;

        if (!getBody().equals(that.getBody())) return false;
        return !(getHistory() != null ? !getHistory().equals(that.getHistory()) : that.getHistory() != null);

    }

    @Override
    public int hashCode() {
        int result = getBody().hashCode();
        result = 31 * result + getHistory().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "TaskContent{" +
                "body='" + body + '\'' +
                ", history='" + history + '\'' +
                '}';
    }
}