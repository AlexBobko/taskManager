package dto;

public class UserDTO extends Entity {

	/**
	 * User
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private String login;
	private String passHash;
	private int role;

	public UserDTO() {

	}

	public UserDTO(int id, String login, String passHash) {
		this.id = id;
		this.login = login;
		this.passHash = passHash;
		this.role = 1;
	}
	public UserDTO(int id, String login, String passHash, int role) {
		this.id = id;
		this.login = login;
		this.passHash = passHash;
		this.role = role;
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

	public int getRole() {
		return role;
	}

	public void setRole(int role) {
		this.role = role;
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
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof UserDTO)) return false;

		UserDTO userDTO = (UserDTO) o;

		if (getId() != userDTO.getId()) return false;
		if (getRole() != userDTO.getRole()) return false;
		if (!getLogin().equals(userDTO.getLogin())) return false;
		return getPassHash().equals(userDTO.getPassHash());

	}

	@Override
	public int hashCode() {
		int result = getId();
		result = 31 * result + getLogin().hashCode();
		result = 31 * result + getPassHash().hashCode();
		result = 31 * result + getRole();
		return result;
	}

	@Override
	public String toString() {
		return "UserDTO [id=" + id + ", login=" + login + ", passHash=" + passHash + "]";
	}
	

}
