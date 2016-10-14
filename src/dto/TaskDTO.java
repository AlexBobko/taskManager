package dto;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import resources.ManagerStatus;
import resources.MessageManager;

public class TaskDTO extends Entity implements Serializable {

	private static final long serialVersionUID = 1L;
	private int id;
	private String title;
	private GregorianCalendar dateCreation /* = new GregorianCalendar() */;
	private String body;
//	private int status; //переписать под TaskStatusDTO
	private GregorianCalendar deadline /* = new GregorianCalendar() */;
	private StringBuffer history;
//	private int executorId;

	public TaskDTO() {
	}

	public TaskDTO(int id, String title, GregorianCalendar dateCreation, String body, GregorianCalendar deadline, StringBuffer history) {
		// super();
		this.id = id;
		this.title = title;
		this.dateCreation = dateCreation;
		this.body = body;
//		this.status = status;
		this.deadline = deadline;
		this.history = history;
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd':'HH-mm");
		history.append(dateFormat.format(dateCreation.getTime())).append("~status:").append(1).append("~~");
//		this.executorId = executorId;
	}

	public TaskDTO(String title, String body, GregorianCalendar deadline) {
		// super(); // this.id = 0;
		this(0, title, new GregorianCalendar(), body, deadline, new StringBuffer());
		this.dateCreation.setTime(new Date());
		// SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd':'HH-mm-ss");
		// System.out.println("** public TaskDTO ** date: " + dateFormat.format(deadline.getTime()));
	}

	public String[] getStringHistory (){
		//TODO механизм вывода поправить
//		StringBuffer outHistory = new StringBuffer(history);
		String outHistory = history.toString();
		int i=1;
		while (i<7) {
			outHistory=outHistory.replace("status:"+i, ManagerStatus.getProperty("status."+i)) ;
			i++;
		}
//		outHistory = new [] ;
		String [] arr = outHistory.split("~~"); 
//		outHistory=outHistory.replace("~~","\n\r<br />");
//		outHistory=outHistory.replace("~","-");
//		status.6
		
		System.out.println("**outHistory**");
		System.out.println(outHistory);
		return arr;
	}
	
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Calendar getDateCreation() {
		return dateCreation;
	}

	public void setDateCreation(GregorianCalendar dateCreation) {
		this.dateCreation = dateCreation;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public GregorianCalendar getDeadline() {
		return deadline;
	}

	public void setDeadline(GregorianCalendar deadline) {
		this.deadline = deadline;
	}

	public StringBuffer getHistory() {
		return history;
	}

	public void setHistory(StringBuffer history) {
		this.history = history;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "TaskDTO [id=" + id + ", title=" + title + ", dateCreation=" + dateCreation + ", body=" + body + ", deadline=" + deadline + ", history=" + history
				+ "]";
	}
	

}
