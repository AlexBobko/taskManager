package loc.task.managers;

import lombok.extern.log4j.Log4j;

import java.util.*;

@Log4j

public class StatusManager {

    private static StatusManager statusManager = null;
    private Map<Integer, String> status = new HashMap<>();
    static Locale locale = new Locale("ru", "RU");
    private final static ResourceBundle resourceBundle = ResourceBundle.getBundle("status", locale);

    public StatusManager(Locale locale) {
        this.locale = locale;
        Enumeration<String> keys = resourceBundle.getKeys();
        this.status = new HashMap<>(7);
        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            Integer k = Integer.parseInt(key);
            this.status.put(k, getProperty(key));
//            System.out.println(k + " - " +  getProperty(key));
        }
    }

    StatusManager() {
    }
    public Map<Integer,String> getStatusList(Locale locale) {
        this.locale = locale;
        Enumeration<String> keys = resourceBundle.getKeys();
        this.status = new HashMap<>(7);
        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            Integer k = Integer.parseInt(key);
            this.status.put(k, getProperty(key));
//            System.out.println(k + " - " +  getProperty(key));
        }
        return status;
    }

    public static String getProperty(String key) {
        return resourceBundle.getString(key);
    }
    public String getProperty(String key,Locale locale) {
        this.locale = locale;
        return resourceBundle.getString(key);
    }

    private static StatusManager getStatusManager() {
        if (statusManager == null) {
            statusManager = new StatusManager();
        }
        return statusManager;
    }

    public Map<Integer, String> getStatus() {
        return this.status;
    }
}