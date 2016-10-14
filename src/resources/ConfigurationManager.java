package resources;

import java.util.ResourceBundle;

/**ConfigurationManager*/
public class ConfigurationManager {

	private final static ResourceBundle resourceBundle = ResourceBundle.getBundle("resources/pageconfig");

	private ConfigurationManager() {
	}

	public static String getProperty(String key) {
		return resourceBundle.getString(key);
	}
}