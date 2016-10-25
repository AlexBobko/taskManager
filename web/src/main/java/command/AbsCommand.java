package command;

import controller.SessionRequestContent;
import dao.TaskDAO;
import dao.TaskMetaDAO;
import dto.TaskDTO;
import dto.TaskMetaDTO;
import utDao.PoolConnection;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

/** AbstractCommand */
public abstract class AbsCommand {
	// Attribute name
//	public static final String PARAM_CMD = "cmdValue";
	public static final String CMD_VALUE = "cmdValue";
	public static final String PARAM_SESSION_USER = "currentUser";
	public static final String ACCOUNT = "account";
	public static final String MESSAGE = "message";

	public abstract String execute(SessionRequestContent content);
	
	protected GregorianCalendar convertDate(String postDate) {
		String[] dl = postDate.split("/");
		// GregorianCalendar.set(year, month, date, hrs, min) 10/21/2016
		int year = (int) Integer.parseInt(dl[2]);
		int month = (int) Integer.parseInt(dl[0]);
		int dayOfMonth = (int) Integer.parseInt(dl[1]);
		int hourOfDay = 20;
		int minute = 15;
		GregorianCalendar bodyDeadline = new GregorianCalendar(year, month, dayOfMonth, hourOfDay, minute);
		return bodyDeadline;
	}
	protected boolean updateTaskMeta(TaskMetaDTO meta, TaskDTO task, SimpleDateFormat dateFormat) {
		boolean b = false;
		TaskMetaDAO metaDao;
		TaskDAO taskDao;
		Connection connection = null;
		try {
			connection = PoolConnection.getInstance().getConnection();
			connection.setAutoCommit(false);
			metaDao = new TaskMetaDAO(connection);
			b = metaDao.updateStatus(meta);
			if (b) {
				Calendar calendar = Calendar.getInstance();
//				SimpleDateFormat dateFormat = Account.dateFormat;
				// System.out.println("** AddNewTaskComandImpl.convertDate ** date: " + dateFormat.format(bodyDeadline.getTime()));
				StringBuffer history = task.getHistory();
				history.append(dateFormat.format(calendar.getTime())).append("~status:").append(meta.getStatusId()).append("~~");
				taskDao = new TaskDAO(connection);
				b = taskDao.updateHistory(task);
			}
			if (b) {
				connection.commit();
			} else {
				connection.rollback();
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
					connection.setAutoCommit(true); //актуальность удалить и проверить
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return b;
	}
	
}
