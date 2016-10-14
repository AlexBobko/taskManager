package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.ConcurrentMap;

import dto.UserDTO;
import resources.ManagerSQL;

public class UserDAO extends AbstractDAO<UserDTO> {
	// private final static String TABLE = ManagerSQL.getProperty(PREFIX.concat(".table"));
	private final static String SQL_INSERT = ManagerSQL.getProperty("user.insert").replace(USERS_ALIAS, USER_TABLE);
	private final static String SQL_SELECT = ManagerSQL.getProperty("user.select").replace(USERS_ALIAS, USER_TABLE);
	private final static String SQL_SELECT_LOGIN = ManagerSQL.getProperty("user.select.login").replace(USERS_ALIAS, USER_TABLE);
	private final static String SQL_DELETE = ManagerSQL.getProperty("user.delete").replace(USERS_ALIAS, USER_TABLE);
	boolean b = false;

	public UserDAO(boolean b) {
		super(b);
	}

	public UserDAO(Connection connection) {
		super(connection);
		// TODO Auto-generated constructor stub
	}

	public UserDAO() {
		super();
		System.out.println(SQL_INSERT);
		System.out.println(SQL_SELECT);
		System.out.println(SQL_DELETE);

	}

	@Override
	public UserDTO findEntityById(int id) {
		UserDTO user = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			System.out.println(SQL_SELECT);
			// System.out.println(TABLE);
			ps = getPreparedStatement(SQL_SELECT);
			// ps.setString(1, TABLE);
			ps.setInt(1, id);
			// System.out.println(ps);
			rs = ps.executeQuery();
			while (rs.next()) {
				user = new UserDTO(rs.getInt(1), rs.getString(2), rs.getString(3));
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
			// TODO SQLException
		} finally {
			close(ps);
		}
		return user;
	}

	public UserDTO findEntityById(String login) {
		UserDTO user = null;
		PreparedStatement ps = null;
		try {
			// System.out.println(SQL_SELECT);
			// System.out.println(TABLE);
			ps = getPreparedStatement(SQL_SELECT_LOGIN);
			// ps.setString(1, TABLE);
			ps.setString(1, login);
			// System.out.println(ps);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				user = new UserDTO(rs.getInt(1), rs.getString(2), rs.getString(3));
				System.out.println("***findEntityById id:" + user.getId() + " log:" + user.getLogin() + "pas:" + user.getPassHash());
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
			// TODO SQLException
		} finally {
			close(ps);
		}
		return user;
	}

	@Override
	public ConcurrentMap<Integer, UserDTO> findAll(int userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<UserDTO> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean delete(int id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean delete(UserDTO entity) {
		int id = entity.getId();
		delete(id);
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int create(UserDTO entity) {
		UserDTO user = entity;
		PreparedStatement ps = null;
		int rs = 0;
		try {
			// System.out.println(SQL_INSERT);
			ps = getPreparedStatement(SQL_INSERT);
			ps.setInt(1, user.getId());
			ps.setString(2, user.getLogin());
			ps.setString(3, user.getPassHash());
			rs = ps.executeUpdate();
			if (rs != 0) {
				ResultSet result;
				result = ps.getGeneratedKeys();
				if (result.next()) {
					rs = result.getInt(1);
				}
				result.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			// TODO SQLException
		} finally {
			close(ps);
		}
		// b = (rs != 0);
		return rs;
	}

	@Override
	public UserDTO update(UserDTO entity) {
		// TODO Auto-generated method stub
		return null;
	}

//	public PreparedStatement selectPreparedStatement() {
//		PreparedStatement st = getPreparedStatement(SQL_SELECT);
//		return st;
//	}

}
