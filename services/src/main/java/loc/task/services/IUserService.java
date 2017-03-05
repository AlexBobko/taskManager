package loc.task.services;

import loc.task.entity.User;
import loc.task.services.exc.LocServiceException;
import loc.task.vo.Account;
import org.hibernate.HibernateException;

import java.util.List;

public interface IUserService {
    Integer employeeRole = 1;
    Integer superiorRole = 2;
    Account getAccount(int userId, String userPassword) throws LocServiceException;

    Account getAccount(String userLogin, String userPassword) throws LocServiceException;

    List<User> getAllEmployee() throws HibernateException;
}
