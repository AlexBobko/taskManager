package utDao;

import java.util.ResourceBundle;

public class ManagerSQL {
	private final static ResourceBundle resourceBundle = ResourceBundle.getBundle("resources.sql");

	public ManagerSQL() {
	}

	public static String getProperty(String key) {
		return resourceBundle.getString(key);
	}

}
