package loc.task.services;

import loc.task.dao.IUserDao;
import loc.task.entity.User;
import loc.task.services.exc.LocServiceException;
import loc.task.utils.jbcrypt.BCrypt;
import loc.task.vo.Account;
import lombok.extern.log4j.Log4j;
import org.hibernate.HibernateException;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Log4j
@Service
//@Transactional(propagation = Propagation.REQUIRED)
public class UserService implements IUserService {

    @Autowired
    private IUserDao userDao;

    @Autowired
    private ITaskService taskService;

    //    public final static Integer employeeRole = 1;
//    public final static Integer superiorRole = 2;
    private final static String soul = "dsdf@@";//локальная соль. не менять

    private UserService() {
    }

    @Override
    public Account getAccount(int userId, String userPassword) throws LocServiceException {
        return getAccount(userId, null, userPassword);
    }

    @Override
    public Account getAccount(String userLogin, String userPassword) throws LocServiceException {
        return getAccount(0, userLogin, userPassword);
    }


    //TODO Trans+Session
//    @Transactional(propagation = Propagation.REQUIRED,transactionManager = "txManager") //,readOnly = true
//    @Transactional(propagation = Propagation.REQUIRES_NEW)
    private Account getAccount(int userId, String userLogin, String userPassword) throws LocServiceException {
//        System.out.println("TRANSACTION ");
        Transaction tx = userDao.getSession().beginTransaction();
        Account account = null;
        User user;
        if (userLogin != null) {
            System.out.println("userLogin: " + userLogin);
            user = userDao.findUserByLogin(userLogin);
            System.out.println("userLoginGet: " + user.getLogin());
        } else {
            user = userDao.get(userId);
        }

        if (user != null) {
            userPassword = "sваываыsd" + soul; //TODO !ХАРДКОД //чтобы не вводить пароль :)
//                userPassword = userPassword + soul; //проконтролить локальную соль Soul
            if (BCrypt.checkpw(userPassword, user.getPasswordHash())) {
                account = taskService.createAccount(user);
                System.out.println("It matches");
            }
        }
        tx.commit();
        return account;
    }

    //TODO Transactional
//    @Transactional(propagation = Propagation.REQUIRED,readOnly = true) //TODO проверить
    public List<User> getAllEmployee() throws HibernateException {
        Transaction tx = userDao.getSession().beginTransaction();
        List<User> users = userDao.getAllEmployee();
        tx.commit();
        return users;
    }
}