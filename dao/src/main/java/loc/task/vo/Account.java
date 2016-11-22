package loc.task.vo;

import loc.task.entity.User;

import java.text.SimpleDateFormat;

public class Account {
    private static final long serialVersionUID = 1L;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd':'HH-mm");
    private User user;

    //	private TreeMap<Integer, TaskMetaDTO> tasksMeta;
    private Integer id;
    private Integer levelAccess;
    private Privileges privileges;

    public Account() {
    }
    public Account(User user) {
        this.user=user;
        this.id=user.getUserId();
        this.levelAccess=user.getRole();
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

    public Privileges getPrivileges() {
        return privileges;
    }

    public void setPrivileges(Privileges privileges) {
        this.privileges = privileges;
    }
}