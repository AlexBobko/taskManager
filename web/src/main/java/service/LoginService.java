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
    private int userId;
    private String userPassword;
    private String userLogin;

    public LoginService(int userId, String userPassword) {
        this.userId = userId;
        this.userPassword = userPassword;
    }

    public LoginService(String userLogin, String userPassword) {
        this.userLogin = userLogin;
        this.userPassword = userPassword;
    }

    public Account getAccount() {
        Account account = null;
        UserDTO user;
//			userId = 1; // TODO удалить вход по табельному номеру
//			String userLogin = "testik"; // TODO удалить
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
        } catch (IOException e) {
        } catch (SQLException e) {
        } catch (PropertyVetoException e) {
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                //TODO add all e to log
            }
        }
        return account;
    }

    private Account getUserAccount(UserDTO user, Connection connection) {
        TaskDAO dao = new TaskDAO(connection);
        //TODO добавить обработку нулевых результатов
        ArrayList<TreeMap<Integer, ?>> data = dao.findAllByUser(user.getId());
        TreeMap<Integer, TaskDTO> currentTasks = (TreeMap<Integer, TaskDTO>) data.get(0);
        TreeMap<Integer, TaskMetaDTO> tasksMeta = (TreeMap<Integer, TaskMetaDTO>) data.get(1);
        Account account = new Account(user, currentTasks, tasksMeta);
        return account;
    }
}
