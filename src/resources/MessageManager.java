package resources;

import java.util.ResourceBundle;

/**All message */
public class MessageManager {

	private final static ResourceBundle resourceBundle = ResourceBundle.getBundle("resources.error");

	private MessageManager() {
	}

	public static String getProperty(String key) {
		return resourceBundle.getString(key);
	}

}