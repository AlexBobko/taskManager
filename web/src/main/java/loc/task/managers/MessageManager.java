package loc.task.managers;

import java.util.ResourceBundle;

/**All message */
public class MessageManager {

	private final static ResourceBundle resourceBundle = ResourceBundle.getBundle("error");

	private MessageManager() {
	}

	public static String getProperty(String key) {
		return resourceBundle.getString(key);
	}

}