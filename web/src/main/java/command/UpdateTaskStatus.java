package bsu;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import dao.TaskDAO;
import dao.TaskMetaDAO;
import dto.TaskDTO;
import dto.TaskMetaDTO;
import resources.PoolConnection;

public class UpdateTaskStatus {
	

	
	private boolean updateTaskMeta(TaskMetaDTO meta, TaskDTO task, SimpleDateFormat dateFormat) {
		boolean b = false;
		TaskMetaDAO metaDao = null;
		TaskDAO taskDao = null;
		Connection connection = null;
		try {
			connection = PoolConnection.getInstance().getConnection();
			connection.setAutoCommit(false);
			metaDao = new TaskMetaDAO(connection);
			b = metaDao.updateStatus(meta);
			if (b) {
				Calendar calendar = Calendar.getInstance();
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
