package command;

import java.text.SimpleDateFormat;

import controller.SessionRequestContent;
import dto.Account;
import dto.TaskDTO;
import dto.TaskMetaDTO;
import resources.ConfigurationManager;
import resources.MessageManager;

/** Task set checking status */
public class TaskInCheckingCommand extends AbsCommand {

	private String page;
	private StringBuffer message;
	private boolean b;

	@Override
	public String execute(SessionRequestContent content) {
		message = new StringBuffer();
		b = false;
		try {
			Account account = (Account) content.getSessionAttributes().get(ACCOUNT);
			int taskId = (int) Integer.parseInt((String) content.getRequestAttributes().get(CMD_VALUE));
			TaskMetaDTO meta = account.getTasksMeta().get(taskId);
			TaskDTO task = account.getCurrentTasks().get(taskId);
			SimpleDateFormat dateFormat = account.getDateFormat();
			meta.setStatusId(4);// устанавливаем статус на проверке
			b = updateTaskMeta(meta, task, dateFormat);
			if (b) {
				page = ConfigurationManager.getProperty("path.page.user");
				message = message.append(MessageManager.getProperty("task.update")).append(meta.getTaskId());
				System.out.println(meta.toString());
			}
			content.getSessionAttributes().put(ACCOUNT, account);
			// System.out.println("addNewTask: " + meta.getId());
		} catch (Exception e) {
			e.printStackTrace();
			message = message.append(MessageManager.getProperty("task.update.false"));
		}
		if (page == null) {
			page = ConfigurationManager.getProperty("path.page.login");
		}
		content.getSessionAttributes().put(MESSAGE, message.toString());
		return page;
	}

	/*
	 * private boolean updateTaskMeta(TaskMetaDTO meta, TaskDTO task, SimpleDateFormat dateFormat) { TaskMetaDAO metaDao = null; TaskDAO taskDao = null;
	 * Connection connection = null; try { connection = PoolConnection.getInstance().getConnection(); connection.setAutoCommit(false); metaDao = new
	 * TaskMetaDAO(connection); b = metaDao.updateStatus(meta); if (b) { Calendar calendar = Calendar.getInstance(); // SimpleDateFormat dateFormat =
	 * Account.dateFormat; // System.out.println("** AddNewTaskComandImpl.convertDate ** date: " + dateFormat.format(bodyDeadline.getTime())); StringBuffer
	 * history = task.getHistory(); history.append(dateFormat.format(calendar.getTime())).append("~status:").append(meta.getStatusId()).append("~~"); taskDao =
	 * new TaskDAO(connection); b = taskDao.updateHistory(task); } if (b) { connection.commit(); } else { connection.rollback(); } } catch (IOException e) {
	 * e.printStackTrace(); } catch (SQLException e) { e.printStackTrace(); } catch (PropertyVetoException e) { e.printStackTrace(); } finally { try { if
	 * (connection != null) { connection.setAutoCommit(true); //актуальность удалить и проверить connection.close(); } } catch (SQLException e) {
	 * e.printStackTrace(); } } return b; }
	 */
}
