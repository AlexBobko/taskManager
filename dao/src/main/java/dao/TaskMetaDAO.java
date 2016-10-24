package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import dto.TaskMetaDTO;
import utDao.ManagerSQL;

public class TaskMetaDAO extends AbstractDAO<TaskMetaDTO> {
	private final static String SQL_UPDATE_STATUS = ManagerSQL.getProperty("meta.status.update").replace(META_ALIAS,META_TABLE);
	private final static String SQL_INSERT = ManagerSQL.getProperty("meta.status.insert").replace(META_ALIAS,META_TABLE);
//	private final static String SQL_UPDATE_STATUS = "UPDATE task_meta SET status_id =? WHERE task_id =?";
	
	public TaskMetaDAO(Connection connection) {
		super(connection);
		// TODO Auto-generated constructor stub
	}

	public TaskMetaDAO(TaskMetaDTO entity, Connection connection) {
		super(entity, connection);
		// TODO Auto-generated constructor stub
	}

	public boolean updateStatus(TaskMetaDTO entity) {
		boolean b = false;
		PreparedStatement ps = null;
		System.out.println(SQL_UPDATE_STATUS);
		int rs = 0;
		try {
			ps = getPreparedStatement(SQL_UPDATE_STATUS);
			ps.setInt(1, entity.getStatusId());
			ps.setInt(2, entity.getTaskId());
			rs = ps.executeUpdate();
			b = (rs != 0);
		} catch (SQLException e) {
			e.printStackTrace();
			// TODO SQLException
		} finally {
			close(ps);
		}
		return b;
	}

	
	@Override
	public List<TaskMetaDTO> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<Integer, TaskMetaDTO> findAll(int forId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TaskMetaDTO findEntityById(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean delete(int id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean delete(TaskMetaDTO entity) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int create(TaskMetaDTO entity) {
		//taskId//userId//statusId
		PreparedStatement ps = null;
		System.out.println(SQL_INSERT);
		int rs = 0;
		try {
			ps = getPreparedStatement(SQL_INSERT);
			ps.setInt(1, entity.getTaskId());
			ps.setInt(2, entity.getUserId());
			ps.setInt(3, entity.getStatusId());
			System.out.println(ps.toString());
			rs = ps.executeUpdate();
//			b = (rs != 0);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(ps);
		}
		return rs;
	}

	@Override
	public TaskMetaDTO update(TaskMetaDTO entity) {
		// TODO Auto-generated method stub
		return null;
	}

	
}
