package loc.task.dao;

import loc.task.entity.User;

import java.util.List;

public interface IUserDao extends Dao<User> {

    User findUserByLogin(String userLogin);

    List<User> getAllEmployee();
}
