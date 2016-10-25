package dto;

import java.text.SimpleDateFormat;
import java.util.TreeMap;

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
	public Account(UserDTO user, TreeMap<Integer, TaskDTO> currentTasks, TreeMap<Integer, TaskMetaDTO> tasksMeta) {
		this.id = user.getId();
		this.user = user;
		this.currentTasks = currentTasks;
		this.tasksMeta = tasksMeta ;
	}

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