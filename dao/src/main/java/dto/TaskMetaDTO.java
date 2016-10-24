package dto;

public class TaskMetaDTO extends Entity {

	private static final long serialVersionUID = 1L;
	private int taskId;
	private int userId;
	private int statusId;

	public TaskMetaDTO(int id) {
		super(id);

	}
	
	public TaskMetaDTO(int taskId, int userId, int statusId) {
//		super();
		this.taskId = taskId;
		this.userId = userId;
		this.statusId = statusId;
	}

	public int getTaskId() {
		return taskId;
	}

	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getStatusId() {
		return statusId;
	}

	public void setStatusId(int statusId) {
		this.statusId = statusId;
	}

	@Override
	public String toString() {
		return "TaskMetaDTO [taskId=" + taskId + ", userId=" + userId + ", statusId=" + statusId + "]";
	}

}

// public TaskStatusDTO() {
// super();
//// TODO Auto-generated constructor stub
// }
// public TaskStatusDTO(int id) {
// super(id);
//// TODO Auto-generated constructor stub
// }
