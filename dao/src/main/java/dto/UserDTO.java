package dto;

public class UserDTO extends Entity {

	/**
	 * User
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private String login;
	private String passHash;

	public UserDTO() {

	}

	public UserDTO(int id, String login, String passHash) {
		this.id = id;
		this.login = login;
		this.passHash = passHash;
	}

	public UserDTO(int id) {
		super(id);
		// TODO Auto-generated constructor stub
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getPassHash() {
		return passHash;
	}

	public void setPassHash(String passHash) {
		this.passHash = passHash;
	}

	@Override
	public String toString() {
		return "UserDTO [id=" + id + ", login=" + login + ", passHash=" + passHash + "]";
	}
	

}
