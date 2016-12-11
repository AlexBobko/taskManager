package loc.task.entity;

import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "personal_data") //++
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_ONLY, region = "user")
public class PersonalData implements Serializable {
    private static final long serialVersionUID = 1L; //геттеры
    @Id
    @Column(name = "user_id")
    @GenericGenerator(name = "gen",
            strategy = "foreign",
            parameters = @org.hibernate.annotations.Parameter(name = "property", value = "user")
    )
//    @Column (name = "user_id", updatable = false, insertable = false)
    @GeneratedValue(generator = "gen")
    private int userId;
    @Column(nullable = false,name = "F_name")
    private String name;
    @Column(nullable = false,name = "F_surname")
    private String surname;
    @Column(nullable = false,name = "F_position")
    private String position;
    @OneToOne (fetch = FetchType.LAZY)
    @PrimaryKeyJoinColumn
    private User user;

    public PersonalData() {
    }

    public PersonalData(String name, String surname, String position, int userId) {
        this.name = name;
        this.surname = surname;
        this.position = position;
        this.userId = userId;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PersonalData)) return false;

        PersonalData that = (PersonalData) o;

        if (getUserId() != that.getUserId()) return false;
        if (!getName().equals(that.getName())) return false;
        if (!getSurname().equals(that.getSurname())) return false;
        return getPosition().equals(that.getPosition());

    }

    @Override
    public int hashCode() {
        int result = getUserId();
        result = 31 * result + getName().hashCode();
        result = 31 * result + getSurname().hashCode();
        result = 31 * result + getPosition().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "PersonalData{" +
                "position='" + position + '\'' +
                ", surname='" + surname + '\'' +
                ", name='" + name + '\'' +
                ", userId=" + userId +
                '}';
    }
}
