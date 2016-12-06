package loc.task.managers;

import lombok.extern.log4j.Log4j;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

@Log4j
//@Repository(ResourceBundle)
public class StatusManager {
    private static StatusManager statusManager = null;
    private Map<Integer, String> status =null;
    private final static ResourceBundle resourceBundle = ResourceBundle.getBundle("status");

    private StatusManager() {
        Enumeration<String> keys = resourceBundle.getKeys();
        this.status = new HashMap<>(7);
        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            Integer k = Integer.parseInt(key);
            this.status.put(k, getProperty(key));
//            System.out.println(k + " - " +  getProperty(key));
        }
    }
    public static String getProperty(String key) {
        return resourceBundle.getString(key);
    }
    private static StatusManager getStatusManager() {
        if (statusManager == null) {
            statusManager = new StatusManager();
        }
        return statusManager;
    }
    public static Map<Integer, String> getStatus() {
        Map<Integer,String> status = getStatusManager().status;
        return status;
    }
}