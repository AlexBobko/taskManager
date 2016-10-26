package dao;

import dto.Entity;
import utDao.ManagerSQL;
import utDao.PoolConnection;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

/**DAO (all SQL: resources.sql.properties)*/

public abstract class AbstractDAO<T extends Entity> {
	protected T entity;
	protected final static String USER_TABLE = ManagerSQL.getProperty("user.table");
	protected final static String TASK_TABLE = ManagerSQL.getProperty("task.table");
	protected final static String META_TABLE = ManagerSQL.getProperty("meta.table");
	protected final static String USERS_ALIAS = "$users"; //users table
	protected final static String TASK_ALIAS = "$tasks"; //tasks table
	protected final static String META_ALIAS = "$meta";//task_meta table

	// protected static String table = "";
	// protected static String SQL_INSERT = ManagerSQL.getProperty("insert.".concat(table));
	// protected static String SQL_SELECT = ManagerSQL.getProperty("select.".concat(table));
	// protected static String SQL_DELETE = ManagerSQL.getProperty("delete.".concat(table));

	protected Connection connection;

	public AbstractDAO() {
	}

	public AbstractDAO(T entity, Connection connection) {
		this.connection = connection;
		this.entity = entity;
	}

	public AbstractDAO(Connection connection) {

		this.connection = connection;

	}

	AbstractDAO(boolean getConnect) {
		if (getConnect) {
			try {
				connection = PoolConnection.getInstance().getConnection();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (PropertyVetoException e) {
				e.printStackTrace();
			}
		}
	}

	public abstract List<T> findAll();

	public abstract Map<Integer, T> findAll(int forId);

	public abstract T findEntityById(int id);

	public abstract boolean delete(int id);

	public abstract boolean delete(T entity);

	public abstract int create(T entity);

	public abstract T update(T entity);

	protected PreparedStatement getPreparedStatement(String sql) {
		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
		} catch (SQLException e) {
			e.printStackTrace();
			// TODO добавить в лог
		}
		return ps;
	}

	public void close(Statement st) {
		try {
			if (st != null) {
				st.close();
			}
		} catch (SQLException e) {
			// лог о невозможности закрытия Statement
		}
	}

}