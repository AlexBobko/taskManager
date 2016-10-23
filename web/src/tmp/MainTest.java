package tmp;

import java.sql.Connection;
import java.util.Date;
import java.util.GregorianCalendar;

import dao.AbstractDAO;
import dao.TaskDAO;
import dao.UserDAO;
import dto.TaskDTO;
import dto.UserDTO;
import resources.PoolConnection;
import utils.org.mindrot.jbcrypt.BCrypt;

public class MainTest {

	public MainTest() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		// insertUser();
		
		addNewTask();
		
//		System.out.println(task.getDateCreation());

		// isLogin(userId, pass);

		// String time=DateFormat.getDateTimeInstance().format(new Date());
		// TODO Auto-generated constructor stub
		
		
		
		
		
		
	}

	//ready
	private static void addNewTask() {
		String title = "new task";
		String body =  "new body task";
		GregorianCalendar deadline = new GregorianCalendar();
		deadline.setTime(new Date());
		
		TaskDTO currentDto = new TaskDTO(title, body, deadline);
		System.out.println(deadline);
		AbstractDAO<TaskDTO> setCurrentDao = null;
		// UserDAO setUserDao = null;
		
		//юзать коннект из ДАО  
		Connection connect = null;
		try {
			connect = PoolConnection.getInstance().getConnection();
			setCurrentDao = new TaskDAO(connect);
			int b = setCurrentDao.create(currentDto);
			System.out.println("new TaskDAO " + b);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	//работает
	private static void insertUser() {
		int userId = 0;
		String login = "vasya";
		String pass = "pass" + "dsdf@@";
		String hashedFirstAdd = BCrypt.hashpw(pass, BCrypt.gensalt());
		
		UserDTO currentDto = new UserDTO(userId, login, hashedFirstAdd);
		AbstractDAO<UserDTO> setUserDao = null;
		// UserDAO setUserDao = null;
		Connection connect = null;
		try {
			connect = PoolConnection.getInstance().getConnection();
			setUserDao = new UserDAO(connect);
			int b = setUserDao.create(currentDto);
			System.out.println("new UserDAO " + b);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	// ready
	private static void isLogin(int userId, String pass) {
		UserDAO getUserDao = null;
		Connection connect = null;
		UserDTO user = null;
		// System.out.println(111);
		try {
			connect = PoolConnection.getInstance().getConnection();
			getUserDao = new UserDAO(connect);
			user = getUserDao.findEntityById(userId);
			if (user != null) {
				System.out.println(user);
				// String pass = "sваываыsd" + "dsdf@@";
				// String hashedFirstAdd = BCrypt.hashpw(pass, BCrypt.gensalt());
				// System.out.println(hashedFF);
				if (BCrypt.checkpw(pass, user.getPassHash()))
					System.out.println("It matches");
				else
					System.out.println("It does not match");
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

}

/*
 * System.out.println(crypt); System.out.println(salt);
 * 
 * // Hash a password for the first time // String hashed = BCrypt.hashpw(pass, BCrypt.gensalt()); String hashed = BCrypt.hashpw(pass, salt); String hashedT =
 * "$2a$12$aA0UZNW76Ue2eAivFWRPQOphtzFTclucnQrAGq1NtbGglhCm/La3W";
 * 
 * // gensalt's log_rounds parameter determines the complexity // the work factor is 2**log_rounds, and the default is 10 //String hashed = BCrypt.hashpw(pass,
 * BCrypt.gensalt(12));
 * 
 * // Check that an unencrypted password matches one that has // previously been hashed System.out.println(hashed); //System.out.println(salt); if
 * (BCrypt.checkpw(pass, hashedT)) System.out.println("It matches"); else System.out.println("It does not match");
 */
