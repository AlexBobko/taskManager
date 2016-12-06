package loc.task.services;

import loc.task.entity.User;
import loc.task.services.exc.TaskServiceException;
import loc.task.services.exc.UserServiceException;
import loc.task.vo.Account;
import org.hibernate.HibernateException;

import java.util.List;

public interface IUserService {

    Account getAccount(int userId, String userPassword) throws UserServiceException, TaskServiceException;

    Account getAccount(String userLogin, String userPassword) throws UserServiceException, TaskServiceException;

    List<User> getAllEmployee() throws HibernateException;
}
