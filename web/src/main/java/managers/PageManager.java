package managers;

import java.util.ResourceBundle;

/**PageManager*/
public class PageManager {

	private final static ResourceBundle resourceBundle = ResourceBundle.getBundle("pageconfig");

	private PageManager() {
	}

	public static String getProperty(String key) {
		return resourceBundle.getString(key);
	}
}
