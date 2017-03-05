package loc.task.managers;

import java.util.Locale;
import java.util.Map;

public interface IStatusManager {
    Map<Integer, String> getStatusList(Locale locale);

    String getProperty(String key, Locale locale);
}
