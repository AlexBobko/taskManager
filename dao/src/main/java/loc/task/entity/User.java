package loc.task.entity;

import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_ONLY, region = "user")
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id",unique = true, nullable = false)
    private int userId;
    @Column
    private int personnelNumber; //табельный номер
    @Column
    private String login;
    @Basic (fetch = FetchType.LAZY)
    @Column
    private String passwordHash;

    @Basic (fetch = FetchType.LAZY)
    @Where(clause = "accountStatus = 1 ")
    @Column
    private int accountStatus = 1; //1,2,3: deleted, block, active

    @Column(name = "role")
    private int role = 1;

    //TODO ?? почему лези не работает?? PersonalData
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL ,fetch = FetchType.LAZY)
    private PersonalData personalData;

    @Where(clause = "status_id < 6 ")
    @ManyToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinTable(name = "user_task",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "task_id"))
    private Set<Task> taskList = new HashSet<Task>();

    public User() {
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getPersonnelNumber() {
        return personnelNumber;
    }

    public void setPersonnelNumber(int personnelNumber) {
        this.personnelNumber = personnelNumber;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public int getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(int accountStatus) {
        this.accountStatus = accountStatus;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public PersonalData getPersonalData() {
        return personalData;
    }

    public void setPersonalData(PersonalData personalData) {
        this.personalData = personalData;
    }

    public Set<Task> getTaskList() {
        return taskList;
    }

    public void setTaskList(Set<Task> taskList) {
        this.taskList = taskList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;

        User user = (User) o;

        if (getUserId() != user.getUserId()) return false;
        if (getPersonnelNumber() != user.getPersonnelNumber()) return false;
        if (getAccountStatus() != user.getAccountStatus()) return false;
        if (getRole() != user.getRole()) return false;
        if (!getLogin().equals(user.getLogin())) return false;
        return getPasswordHash().equals(user.getPasswordHash());

    }

    @Override
    public int hashCode() {
        int result = getUserId();
        result = 31 * result + getPersonnelNumber();
        result = 31 * result + getLogin().hashCode();
        result = 31 * result + getPasswordHash().hashCode();
        result = 31 * result + getAccountStatus();
        result = 31 * result + getRole();
        return result;
    }
}