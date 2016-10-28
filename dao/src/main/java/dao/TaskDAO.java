package dao;

import dto.TaskDTO;
import dto.TaskMetaDTO;
import dto.UserDTO;
import utDao.ManagerSQL;

import java.sql.*;
import java.util.*;

public class TaskDAO extends AbstractDAO<TaskDTO> {
    // private final static String PREFIX = "task"; // resources.sql.properties
    private final static String SQL_INSERT = ManagerSQL.getProperty("task.insert").replace(TASK_ALIAS, TASK_TABLE);
    private final static String SQL_UPDATE = ManagerSQL.getProperty("task.update").replace(TASK_ALIAS, TASK_TABLE);
    //	private final static String SQL_INSERT_ASSIGN = ManagerSQL.getProperty("user.task.insert").replace(META_ALIAS, META_TABLE);
//	private final static String SQL_SELECT = ManagerSQL.getProperty("task.insert").replace(TASK_ALIAS, TASK_TABLE);
    private final static String SQL_UPDATE_HISTORY = ManagerSQL.getProperty("task.update.history").replace(TASK_ALIAS, TASK_TABLE);
    private final static String SQL_DELETE = ManagerSQL.getProperty("task.insert").replace(TASK_ALIAS, TASK_TABLE);
    private final static String SQL_SELECT_FOR_ID = ManagerSQL.getProperty("task.select.current.id").replace(TASK_ALIAS, TASK_TABLE).replace(META_ALIAS, META_TABLE);
    private final static String SQL_SELECT_FOR_SUPERIOR = ManagerSQL.getProperty("task.select.current.superior").replace(TASK_ALIAS, TASK_TABLE).replace(META_ALIAS, META_TABLE);
    // boolean b = false;

    public TaskDAO() {
        super();
    }

    public TaskDAO(TaskDTO entity, Connection connection) {
        this.connection = connection;
        this.entity = entity;
    }

    public TaskDAO(boolean getConnect) {
        super(getConnect);
        // TODO Auto-generated constructor stub
    }

    public TaskDAO(Connection connection) {
        super(connection);
        // TODO Auto-generated constructor stub
    }

    public boolean updateHistory(TaskDTO entity) {
        boolean b = false;
        PreparedStatement ps = null;
        try {
            ps = getPreparedStatement(SQL_UPDATE_HISTORY);
            System.out.println(SQL_UPDATE_HISTORY);
            ps.setString(1, entity.getHistory().toString());
            ps.setInt(2, entity.getId());
            int rs = ps.executeUpdate();
            b = (rs != 0);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(ps);
        }
        return b;
    }


// TODO наначение исполнителя (поправить)
//	public boolean assignUser(int taskId, int userId) {
//		boolean b = false;
//		PreparedStatement ps = null;
//		int rs = 0;
//		int statusId = 1;// status create (new)
//		System.out.println("*-*taskId " + taskId + "userId " + userId);
//		try {
//			ps = getPreparedStatement(SQL_INSERT_ASSIGN);
//			System.out.println(SQL_INSERT_ASSIGN);
//			ps.setInt(1, taskId);
//			ps.setInt(2, userId);
//			ps.setInt(3, statusId);
//			rs = ps.executeUpdate();
//			b = (rs != 0);
//		} catch (SQLException e) {
//			e.printStackTrace();
//
//		} finally {
//			close(ps);
//		}
//		// b = (rs != 0);
//		return b;
//	}


    @Override
    public List<TaskDTO> findAll() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Map<Integer, TaskDTO> findAll(int forId) {
        // TODO Auto-generated method stub
        return null;
    }

    //таски для исполнителя
    public ArrayList<TreeMap<Integer, ?>> findAllByUser(int currentUserId) {
        TreeMap<Integer, TaskDTO> tasks = new TreeMap<>();
        TreeMap<Integer, TaskMetaDTO> meta = new TreeMap<>();
        ArrayList<TreeMap<Integer, ?>> tasksMeta = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet result;
        try {
            // System.out.println(SQL_SELECT_FOR_ID);
            ps = getPreparedStatement(SQL_SELECT_FOR_ID);
            ps.setInt(1, currentUserId);
            result = ps.executeQuery();
            int userId;
            while (result.next()) {
                int id = result.getInt("task_id");
                userId = result.getInt("user_id");
                String title = result.getString("title");
                GregorianCalendar dateCreation = new GregorianCalendar();
                dateCreation.setTime(result.getTimestamp("date_creation"));
                String body = result.getString("body");
                int status = result.getInt("status_id");
                GregorianCalendar deadline = new GregorianCalendar();
                deadline.setTime(result.getTimestamp("deadline"));
                // dateCreation.setTime(result.getTimestamp("deadline"));
                StringBuffer history = new StringBuffer(result.getString("history"));
                tasks.put(id, new TaskDTO(id, title, dateCreation, body, deadline, history));
                meta.put(id, new TaskMetaDTO(id, userId, status));
//				System.out.println("id " + id + "userId " + userId +  "status " + status);
            }
            result.close();
            tasksMeta.add(tasks);
            tasksMeta.add(meta);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(ps);
        }
        return tasksMeta;
    }

    //current Task from SUPERIOR и испольнителя
    public ArrayList<TreeMap<Integer, ?>> findAllBySuperior(UserDTO user) {
        System.out.println("user.getRole() DAO");
        System.out.println(user.getRole());

        if (user.getRole() == 1) {
            return findAllByUser(user.getId());
        } else if (user.getRole() != 2) {
            return null;
        }
        TreeMap<Integer, TaskDTO> tasks = new TreeMap<>();
        TreeMap<Integer, TaskMetaDTO> meta = new TreeMap<>();
        ArrayList<TreeMap<Integer, ?>> tasksMeta = new ArrayList<>();
        Statement st = null;
        ResultSet result;
        try {
            st = connection.createStatement();
            System.out.println(SQL_SELECT_FOR_SUPERIOR);//лог
            result = st.executeQuery(SQL_SELECT_FOR_SUPERIOR);
            int userId;
            while (result.next()) {
                int id = result.getInt("task_id");
                userId = result.getInt("user_id");
                String title = result.getString("title");
                GregorianCalendar dateCreation = new GregorianCalendar();
                dateCreation.setTime(result.getTimestamp("date_creation"));
                String body = result.getString("body");
                int status = result.getInt("status_id");
                GregorianCalendar deadline = new GregorianCalendar();
                deadline.setTime(result.getTimestamp("deadline"));
                StringBuffer history = new StringBuffer(result.getString("history"));
                tasks.put(id, new TaskDTO(id, title, dateCreation, body, deadline, history));
                meta.put(id, new TaskMetaDTO(id, userId, status));
//				System.out.println("id " + id + "userId " + userId +  "status " + status);
            }
            result.close();
            tasksMeta.add(tasks);
            tasksMeta.add(meta);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(st);
        }
        return tasksMeta;
    }

    @Override
    public TaskDTO findEntityById(int id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean delete(int id) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean delete(TaskDTO entity) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public int create(TaskDTO entity) {
        PreparedStatement ps = null;
        int rs = 0;
        try {
            ps = getPreparedStatement(SQL_INSERT);
            ps.setInt(1, entity.getId());
            ps.setString(2, entity.getTitle());
            java.util.Date dateCreation = (java.util.Date) entity.getDateCreation().getTime();
            java.sql.Timestamp sqlDateCreation = new java.sql.Timestamp(dateCreation.getTime());
            ps.setTimestamp(3, sqlDateCreation);
            ps.setString(4, entity.getBody());
            java.util.Date dateDeadline = (java.util.Date) entity.getDeadline().getTime();
            java.sql.Timestamp sqlDateDeadline = new java.sql.Timestamp(dateDeadline.getTime());
            ps.setTimestamp(5, sqlDateDeadline);
            ps.setString(6, entity.getHistory().toString());
            rs = ps.executeUpdate();
            if (rs != 0) {
                ResultSet result;
                result = ps.getGeneratedKeys();
                System.out.println(SQL_INSERT); //в лог отладки
//				System.out.println(result);
                if (result.next()) {
                    rs = result.getInt(1);
//					System.out.println(rs);
                    entity.setId(rs);
                }
                result.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(ps);
        }
        return rs;// b = (rs != 0);
    }


    public TaskDTO update(TaskDTO entity) {
        PreparedStatement ps = null;
        int rs = 0;
        try {
            ps = getPreparedStatement(SQL_UPDATE);
            ps.setInt(1, entity.getId());
            ps.setInt(7, entity.getId());
            ps.setString(2, entity.getTitle());
            java.util.Date dateCreation = (java.util.Date) entity.getDateCreation().getTime();
            java.sql.Timestamp sqlDateCreation = new java.sql.Timestamp(dateCreation.getTime());
            ps.setTimestamp(3, sqlDateCreation);

            ps.setString(4, entity.getBody());
            // ps.setInt(5, entity.getStatus());
            java.util.Date dateDeadline = (java.util.Date) entity.getDeadline().getTime();
            java.sql.Timestamp sqlDateDeadline = new java.sql.Timestamp(dateDeadline.getTime());
            ps.setTimestamp(5, sqlDateDeadline);
            ps.setString(6, entity.getHistory().toString());
            rs = ps.executeUpdate();
            if (rs != 0) {
                return entity;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(ps);
        }

        return null;//b = (rs != 0);
    }


}
