package dto;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.TreeMap;

import dao.TaskDAO;

public class Account extends Entity {
	private static final long serialVersionUID = 1L;
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd':'HH-mm");
	private int id;
	private UserDTO user;
	private TreeMap<Integer, TaskDTO> currentTasks;
	private TreeMap<Integer, TaskMetaDTO> tasksMeta;
	private int levelAccess = 1;

	public Account() {

	}

	@SuppressWarnings({ "unchecked" })
	public Account(UserDTO user, Connection connection) {
		this.id = user.getId();
		this.user = user;
		TaskDAO dao = new TaskDAO(connection);
		ArrayList<TreeMap<Integer, ?>> data = dao.findAllByUser(user.getId());
		this.currentTasks = (TreeMap<Integer, TaskDTO>) data.get(0);
		this.tasksMeta = (TreeMap<Integer, TaskMetaDTO>) data.get(1);
	}
	// for (Map.Entry<Integer, TaskMetaDTO> meta : tasksMeta.entrySet()) {
	// System.out.println("akk**");
	// System.out.println(meta.getKey());
	// System.out.println(meta.getValue());
	// }

	// long i = 0;
	// for (Map.Entry<Integer, Integer> pair : map.entrySet()) {
	// i += pair.getKey() + pair.getValue();
	// }
	//
	// final long[] i = {0};
	// map.forEach((k, v) -> i[0] += k + v);

	public UserDTO getUser() {
		return user;
	}

	public int getId() {
		return id;
	}

	public TreeMap<Integer, TaskMetaDTO> getTasksMeta() {
		return tasksMeta;
	}

	public void setTasksMeta(TreeMap<Integer, TaskMetaDTO> tasksMeta) {
		this.tasksMeta = tasksMeta;
	}

	public void setUser(UserDTO user) {
		this.user = user;
	}

	public TreeMap<Integer, TaskDTO> getCurrentTasks() {
		return currentTasks;
	}

	public void setCurrentTasks(TreeMap<Integer, TaskDTO> currentTasks) {
		this.currentTasks = currentTasks;
	}

	public int getLevelAccess() {
		return levelAccess;
	}

	// TODO delete
	public void setLevelAccess(int levelAccess) {
		this.levelAccess = levelAccess;
	}

	public SimpleDateFormat getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(SimpleDateFormat dateFormat) {
		this.dateFormat = dateFormat;
	}

}