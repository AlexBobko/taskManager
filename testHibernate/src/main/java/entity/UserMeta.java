package entity;

public class UserMeta {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private String login;
    private String name;
    private String email;
    private String position;
//    private Calendar birthday;

    public UserMeta() {
    }

    public UserMeta(Integer id, String login, String name, String email, String position) {
        this.id = id;
        this.login = login;
        this.name = name;
        this.email = email;
        this.position = position;
    }

    public Integer getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPosition() {
        return position;
    }



    public void setId(Integer id) {
        this.id = id;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPosition(String position) {
        this.position = position;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserMeta)) return false;

        UserMeta userMeta = (UserMeta) o;

        if (!getId().equals(userMeta.getId())) return false;
        if (!getLogin().equals(userMeta.getLogin())) return false;
        if (!getName().equals(userMeta.getName())) return false;
        if (!getEmail().equals(userMeta.getEmail())) return false;
        return getPosition().equals(userMeta.getPosition());

    }

    @Override
    public int hashCode() {
        int result = getId().hashCode();
        result = 31 * result + getLogin().hashCode();
        result = 31 * result + getName().hashCode();
        result = 31 * result + getEmail().hashCode();
        result = 31 * result + getPosition().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "UserMeta{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", position='" + position + '\'' +
                '}';
    }
}

/**
 * INSERT INTO  `test_project`.`usersmeta` (
 `ID` ,
 `user_login` ,
 `user_email` ,
 `user_position` ,
 `user_name` ,
 `user_birthday`
 )
 VALUES (
 NULL ,  'Testik',  'testik@gmail.com',  'Инженер',  'Василевский Дмитрий Владимирович',  '1985-06-15'
 );
 */