package managers;

import java.util.ResourceBundle;

/**ConfigurationManager*/
public class ConfigurationManager {

	private final static ResourceBundle resourceBundle = ResourceBundle.getBundle("pageconfig");

	private ConfigurationManager() {
	}

	public static String getProperty(String key) {
		return resourceBundle.getString(key);
	}
}
