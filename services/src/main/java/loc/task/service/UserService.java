package loc.task.service;

import loc.task.db.UserDao;
import loc.task.db.exceptions.DaoException;
import loc.task.entity.User;
import loc.task.service.exc.UserServiceException;
import loc.task.util.HibernateUtil;
import loc.task.utils.jbcrypt.BCrypt;
import loc.task.vo.Account;
import loc.task.vo.AccountSuperior;
import loc.task.vo.TaskOutFilter;
import lombok.extern.log4j.Log4j;
import org.hibernate.Transaction;


@Log4j
public class UserService implements IUserService{
    private static UserService userService = null;
    private static UserDao userDao = UserDao.getUserDao();
    public final static Integer employeeRole = 1;
    public final static Integer superiorRole = 2;

    private UserService() {
    }

    private static synchronized UserService getInstance() {
        if (userService == null) {
            userService = new UserService();
        }

        return userService;
    }

    public static UserService getUserService() {
        if (userService == null) {
            return getInstance();
        }
        return userService;
    }

    public Account getAccount(int userId, String userPassword) throws UserServiceException {
        return getAccount(userId, null, userPassword);
    }

    public Account getAccount(String userLogin, String userPassword) throws UserServiceException {
        return getAccount(0, userLogin, userPassword);
    }

    //TODO Trans+Session
    private Account getAccount(int userId, String userLogin, String userPassword) throws UserServiceException {
        Transaction transaction = HibernateUtil.getHibernateUtil().getSession().beginTransaction();
        Account account = null;
        User user = null;
        try {
            if (userLogin != null) {
                System.out.println("userLogin: " + userLogin);
                user = userDao.findEntityByLogin(userLogin);
            } else {
                user = userDao.get(userId);
            }
            if (user != null) {
                userPassword = "sваываыsd" + "dsdf@@"; //TODO !ХАРДКОД //чтобы не вводить пароль :)
//                userPassword = userPassword + "dsdf@@"; //проконтролить локальную соль Soul
                if (BCrypt.checkpw(userPassword, user.getPasswordHash())) {
                    account = createAccount(user);
//                    System.out.println("It matches");
                }
            }
            transaction.commit();
        } catch (DaoException e) {
            log.error(e, e);
            throw new UserServiceException(e);
        }
        return account;
    }

    private Account createAccount(User user) throws DaoException {
        Account account = null;
        TaskService ts = TaskService.getTaskService();
        if (user.getRole() == employeeRole) {
            TaskOutFilter currentTasksFilter = ts.getTaskOutFilter(ts.getDefaultEmployeeStatusList(), user.getUserId());
            account = new Account(user, currentTasksFilter, ts.getTasksList(currentTasksFilter, user.getUserId()));
        } else if (user.getRole() == superiorRole) {

            //TODO могут быть проблемы с LAZY userID  можно еще размер мапки ограничить
            TaskOutFilter currentTasksFilter = ts.getTaskOutFilter(ts.getDefaultSuperiorStatusList()); //общий фильтр


            TaskOutFilter reportTaskFilter = ts.getTaskOutFilter(ts.getSuperiorReportStatusList());//дополнительный фильтр

            account = new AccountSuperior(user, currentTasksFilter, ts.getTasksList(currentTasksFilter),
                    reportTaskFilter, ts.getTasksList(reportTaskFilter));

            //TODO ?? загружается полностью юзер обленили юзера )))
        }
        return account;
    }
}
