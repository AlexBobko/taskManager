package loc.task.service;

import loc.task.service.exc.UserServiceException;
import loc.task.vo.Account;

public interface IUserService {

    Account getAccount(int userId, String userPassword) throws UserServiceException;

    Account getAccount(String userLogin, String userPassword) throws UserServiceException;
}
