package service;

import dao.TaskDAO;
import dao.UserDAO;
import dto.Account;
import dto.TaskDTO;
import dto.TaskMetaDTO;
import dto.UserDTO;
import utDao.PoolConnection;
import utils.org.mindrot.jbcrypt.BCrypt;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.TreeMap;

public class LoginService {

    //TODO переделать на синглтон? синхронизация методов (параметров)?
    public Account getAccount(int userId, String userPassword) {
        return getAccount(userId, null, userPassword);
    }

    public Account getAccount(String userLogin, String userPassword) {
        return getAccount(0, userLogin, userPassword);
    }

    private Account getAccount(int userId, String userLogin, String userPassword) {
        Account account = null;
        UserDTO user;
        Connection connection = null;
        try {
            connection = PoolConnection.getInstance().getConnection();
            UserDAO dao = new UserDAO(connection);
            if (userLogin != null) {
                user = dao.findEntityByLogin(userLogin);
            } else {
                user = dao.findEntityById(userId);
            }
            if (user != null) {
//                    pass = "sваываыsd" + "dsdf@@"; // удалить строку
                userPassword = userPassword + "dsdf@@"; //проконтролить соль
                if (BCrypt.checkpw(userPassword, user.getPassHash())) {
//                        System.out.println("It matches");
                    account = getUserAccount(user, connection);
                }
            }
        //TODO log add e
        } catch (IOException e) {
        } catch (SQLException e) {
        } catch (PropertyVetoException e) {
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
            }
        }
        return account;
    }

    private Account getUserAccount(UserDTO user, Connection connection) {
        TaskDAO dao = new TaskDAO(connection);
        ArrayList<TreeMap<Integer, ?>> data = dao.findAllBySuperior(user);
        TreeMap<Integer, TaskDTO> currentTasks = (TreeMap<Integer, TaskDTO>) data.get(0);
        TreeMap<Integer, TaskMetaDTO> tasksMeta = (TreeMap<Integer, TaskMetaDTO>) data.get(1);
        return new Account(user, currentTasks, tasksMeta);
    }
}
