package bsu.command;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.GregorianCalendar;

import controller.SessionRequestContent;
import dao.TaskDAO;
import dao.TaskMetaDAO;
import dto.Account;
import dto.TaskDTO;
import dto.TaskMetaDTO;
import resources.ConfigurationManager;
import resources.MessageManager;
import resources.PoolConnection;

public class TaskAddCommand extends AbsCommand {
	private static final String POST_TITLE = "titleTask";
	private static final String POST_BODY = "bodyTask";
	private static final String POST_DEADLINE = "taskDeadline";
	private String page;
	private StringBuffer message;

	public TaskAddCommand() {

	}

	@Override
	public String execute(SessionRequestContent content) {
		message = new StringBuffer();
		// page = ConfigurationManager.getProperty("path.page.login");
		page = ConfigurationManager.getProperty("path.page.user");
		Account account = null;
		try {
			account = (Account) content.getSessionAttributes().get(ACCOUNT); // TODO nullpointer
			String titleTask = (String) content.getRequestAttributes().get(POST_TITLE);
			String bodyTask = (String) content.getRequestAttributes().get(POST_BODY);
			String strTaskDeadline = (String) content.getRequestAttributes().get(POST_DEADLINE);
			GregorianCalendar taskDeadline = convertDate(strTaskDeadline);
			TaskDTO newTask = new TaskDTO(titleTask, bodyTask, taskDeadline);
			// назначаем создателя исполнителем
			int userId = account.getUser().getId();
			TaskMetaDTO newTaskMeta = new TaskMetaDTO(0, userId, 1); // int taskId, int userId, int statusId
			if (addNewTask(newTask, newTaskMeta)) {
				message.append(MessageManager.getProperty("message.task.add") + newTask.getId());
				account.getCurrentTasks().put(newTask.getId(), newTask);
				account.getTasksMeta().put(newTaskMeta.getTaskId(), newTaskMeta);
				System.out.println("addNewTask: " + newTask.getId());
			} else {
				message.append(MessageManager.getProperty("message.task.addfalse"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			message.append(MessageManager.getProperty("message.task.addfalse"));
		}
		content.getSessionAttributes().put(ACCOUNT, account);
		content.getSessionAttributes().put(MESSAGE, message.toString());
		return page;
	}

	private boolean addNewTask(TaskDTO newTask, TaskMetaDTO newTaskMeta) {
		boolean b = false;
		int id = 0;
		TaskDAO taskDao = null;
		TaskMetaDAO metaDao = null;
		Connection connection = null;
		try {
			connection = PoolConnection.getInstance().getConnection();
			connection.setAutoCommit(false);
			taskDao = new TaskDAO(connection);
			id = taskDao.create(newTask);
			newTaskMeta.setTaskId(id);
			if (id != 0) {
				metaDao = new TaskMetaDAO(connection);
				if (metaDao.create(newTaskMeta) != 0) {
					connection.commit();
					b = true;
					System.out.println("new TaskDAO id: " + id);
				}
			} else {
				connection.rollback();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.setAutoCommit(true);// TODO (почитать) необходимость закрытия
					connection.close();
				}
			} catch (SQLException e) {
			}
		}
		return b;
		// b = (rs != 0);
	}
}
