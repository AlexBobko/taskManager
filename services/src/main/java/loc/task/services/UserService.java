package loc.task.services;

import loc.task.dao.IUserDao;
import loc.task.entity.User;
import loc.task.services.exc.TaskServiceException;
import loc.task.utils.jbcrypt.BCrypt;
import loc.task.vo.Account;
import lombok.extern.log4j.Log4j;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Log4j
@Service
//@Transactional
public class UserService implements IUserService {

    @Autowired
    private IUserDao userDao;

    @Autowired
    private ITaskService taskService;

    public final static Integer employeeRole = 1;
    public final static Integer superiorRole = 2;
    private final static String soul = "dsdf@@";//локальная соль. не менять

    private UserService() {
    }
    @Override
    public Account getAccount(int userId, String userPassword) throws TaskServiceException {
        return getAccount(userId, null, userPassword);
    }
    @Override
    public Account getAccount(String userLogin, String userPassword) throws TaskServiceException {
        return getAccount(0, userLogin, userPassword);
    }

    //TODO Trans+Session
    @Transactional(propagation = Propagation.REQUIRED,readOnly = true)
    private Account getAccount(int userId, String userLogin, String userPassword) throws TaskServiceException {
        Account account = null;
        User user;
        if (userLogin != null) {
            System.out.println("userLogin: " + userLogin);
            user = userDao.findUserByLogin(userLogin);
        } else {
            user = userDao.get(userId);
        }
        if (user != null) {
            userPassword = "sваываыsd" + soul; //TODO !ХАРДКОД //чтобы не вводить пароль :)
//                userPassword = userPassword + soul; //проконтролить локальную соль Soul
            if (BCrypt.checkpw(userPassword, user.getPasswordHash())) {
                account = taskService.createAccount(user);
//                    System.out.println("It matches");
            }
        }
        return account;
    }


    @Transactional(propagation = Propagation.REQUIRED,readOnly = true) //TODO проверить
    public List<User> getAllEmployee() throws HibernateException {
        return userDao.getAllEmployee();
    }
}
