package service;

import loc.task.db.UserDao;
import loc.task.db.exceptions.DaoException;
import loc.task.entity.Task;
import loc.task.entity.User;
import loc.task.vo.*;
import org.apache.log4j.Logger;
import utils.org.mindrot.jbcrypt.BCrypt;

import java.util.HashSet;
import java.util.Set;

public class UserService {

    private static Logger log = Logger.getLogger(UserService.class);
    private static UserDao userDao = null;

    //TODO переделать на синглтон

    public Account getAccount(int userId, String userPassword) {
        return getAccount(userId, null, userPassword);
    }

    public Account getAccount(String userLogin, String userPassword) {
        return getAccount(0, userLogin, userPassword);
    }

    private Account getAccount(int userId, String userLogin, String userPassword) {
        Account account = null;
        User user;
        try {
            if (userLogin != null) {
                user = getUserDao().findEntityByLogin(userLogin);
            } else {
                user = getUserDao().get(userId);
            }
            if (user != null) {
//                    pass = "sваываыsd" + "dsdf@@"; //чтобы не вводить пароль :)
                userPassword = userPassword + "dsdf@@"; //проконтролить локальную соль Soul
                if (BCrypt.checkpw(userPassword, user.getPasswordHash())) {
//                        System.out.println("It matches");

                    Privileges privileges= getUserPrivileges (user);
                    account = new Account(user);
                    account.setPrivileges(privileges);
                } else {
                    user = null;
                }
            }
        }catch (DaoException e)
        {
            log.error(e, e);
        }
        return account;
    }


    private Privileges getUserPrivileges(User user) {
        //Default settints
        int tasksPerPage = 12;
        long countPage;
        long totalCount;
        Set<Integer> includeStatus = new HashSet<>();
        Set<Task> currentTasks = new HashSet<>();
        int i = 1;
        Privileges privileges = null;
        switch (user.getRole()) {
            case 2:
                privileges = new DirectorPrivileges();
                break;
            case 3:
                privileges = new AdminPrivileges();
                break;
            default:
                i = 1;
                while (i < 7) {
                    includeStatus.add(i++);
                }
                totalCount = TaskService.getCountTask(includeStatus, user.getUserId());
                i = 0;
                for (Task task : user.getTaskList()) {
                    i++;
                    currentTasks.add(task);
                    if (i == tasksPerPage) {
                        break;
                    }
                }
                countPage = totalCount / (long) tasksPerPage;
                if (totalCount % (long) tasksPerPage > 0) {
                    countPage++;
                }
                privileges = new EmployeePrivileges(totalCount,includeStatus,countPage,currentTasks);
                break;
        }
         return privileges;
    }

    public User getUser(int userId) throws DaoException{
        return getUserDao().get(userId);
    }

    public static UserDao getUserDao() {
        if (userDao == null) {
            userDao = new UserDao();
        }
        return userDao;
    }
}
