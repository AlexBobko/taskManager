package loc.task.services;

import loc.task.dao.UserDao;
import loc.task.entity.User;
import loc.task.services.exc.TaskServiceException;
import loc.task.services.exc.UserServiceException;
import loc.task.utils.jbcrypt.BCrypt;
import loc.task.vo.Account;
import loc.task.vo.AccountSuperior;
import loc.task.vo.TaskOutFilter;
import lombok.extern.log4j.Log4j;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Log4j
@Service
@Transactional
public class UserService implements IUserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private TaskService taskService;

    public final static Integer employeeRole = 1;
    public final static Integer superiorRole = 2;
    private final static String soul = "dsdf@@";//локальная соль. не менять

    private UserService() {
    }

    public Account getAccount(int userId, String userPassword) throws UserServiceException, TaskServiceException {
        return getAccount(userId, null, userPassword);
    }

    public Account getAccount(String userLogin, String userPassword) throws UserServiceException, TaskServiceException {
        return getAccount(0, userLogin, userPassword);
    }

    //TODO Trans+Session
    private Account getAccount(int userId, String userLogin, String userPassword) throws UserServiceException, TaskServiceException {
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
                    account = createAccount(user);
//                    System.out.println("It matches");
                }
            }
        return account;
    }

    private Account createAccount(User user) throws UserServiceException,TaskServiceException {
        Account account = null;
        TaskService ts = taskService;
        if (user.getRole() == employeeRole) {
            TaskOutFilter currentTasksFilter = ts.getTaskOutFilter(ts.getDefaultEmployeeStatusList(), user.getUserId());
            account = new Account(user, currentTasksFilter, ts.getTasksList(currentTasksFilter, user.getUserId()));
        } else if (user.getRole() == superiorRole) {
            TaskOutFilter currentTasksFilter = ts.getTaskOutFilter(ts.getDefaultSuperiorStatusList()); //общий фильтр
            TaskOutFilter reportTaskFilter = ts.getTaskOutFilter(ts.getSuperiorReportStatusList());//дополнительный фильтр
            account = new AccountSuperior(user, currentTasksFilter, ts.getTasksList(currentTasksFilter),
                    reportTaskFilter, ts.getTasksList(reportTaskFilter));
            //TODO ?? загружается полностью юзер (персонал дата) обленили юзера )))
        }
        return account;
    }

    public List<User> getAllEmployee() throws HibernateException {
            return userDao.getAllEmployee();
    }
}
