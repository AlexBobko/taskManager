package bsu.command;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import controller.SessionRequestContent;
import dao.UserDAO;
import dto.Account;
import dto.UserDTO;
import resources.ConfigurationManager;
import resources.MessageManager;
import resources.PoolConnection;
import utils.org.mindrot.jbcrypt.BCrypt;

/** Login */
public class LoginUserCommand extends AbsCommand {
	private static final String LOGIN = "username";
	private static final String PASSWORD = "password";
	// private UserDTO user;
	private String page;
	private StringBuffer message;

	LoginUserCommand() {
	}

	@Override
	public String execute(SessionRequestContent content) {
		message = new StringBuffer();
//		System.out.println("It LoginUserComandImpl");
		Account account = null;
		UserDTO user = new UserDTO();
//		int userId;
		try {
			String userPassword = (String) content.getRequestAttributes().get(PASSWORD);
			// userName = (int) Integer.parseInt((String) content.getRequestAttributes().get(LOGIN));
//			userId = 1; // TODO удалить вход по табельному номеру
			String userLogin = "testik"; // TODO удалить
			Connection connection = null;
			try {
				connection = PoolConnection.getInstance().getConnection();
				user = isLogin(userLogin, userPassword, connection);
//				user = isLogin(userId, userPassword, connection); //через ID
				if (user != null) {
					account = new Account(user,connection);
					page = ConfigurationManager.getProperty("path.page.user");
					message.append(MessageManager.getProperty("message.true.login"));
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (PropertyVetoException e) {
				e.printStackTrace();
			} finally {
				try {
					if (connection != null) {
						connection.close();
					}
				} catch (SQLException e) {
				}
			}
		} catch (Exception e) {
			// e.printStackTrace();
			message.append(MessageManager.getProperty("message.loginerror"));
			page = ConfigurationManager.getProperty("path.page.login");
			user = null; // нужно ли?
		} finally {
			content.getSessionAttributes().put(ACCOUNT, account);
			content.getSessionAttributes().put(MESSAGE, message.toString());
		}
		return page;
	}
	
	private UserDTO isLogin(String userLogin, String pass, Connection connection) {
		UserDAO dao = null;
		UserDTO user = null;
		try {
			dao = new UserDAO(connection);
			user = dao.findEntityById(userLogin);
			if (user != null) {
				pass = "sваываыsd" + "dsdf@@"; // удалить строку
				// pass = pass + "dsdf@@"; //проконтролить соль
				if (BCrypt.checkpw(pass, user.getPassHash())) {
					System.out.println("It matches");
				} else {
					System.out.println("It does not match");
					user = null;
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return user;
	}

	private UserDTO isLogin(int userId, String pass, Connection connection) {
		UserDAO dao = null;
		UserDTO user = null;
		try {
			dao = new UserDAO(connection);
			user = dao.findEntityById(userId);
			if (user != null) {
				pass = "sваываыsd" + "dsdf@@"; // удалить строку
				// pass = pass + "dsdf@@"; //проконтролить соль
				if (BCrypt.checkpw(pass, user.getPassHash())) {
					System.out.println("It matches");
				} else {
					System.out.println("It does not match");
					user = null;
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return user;
	}
}
