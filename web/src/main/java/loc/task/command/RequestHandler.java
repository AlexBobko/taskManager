package loc.task.command;

import lombok.extern.log4j.Log4j;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Enumeration;
import java.util.HashMap;

/**
 * получение элементов запроса (остатки)
 */
@Log4j
public class RequestHandler {
    private HashMap<String, Object> requestAttributes;
    private HashMap<String, String[]> requestParameters;
    private HashMap<String, Object> sessionAttributes;
    static final String CMD_VALUE = "cmdValue";

    public RequestHandler() {
        sessionAttributes = new HashMap<>();
        requestAttributes = new HashMap<>();
        requestParameters = new HashMap<>();
    }

    public CommandList extractValues(HttpServletRequest request) {
        CommandList currentCommand = null;
        HttpSession session = request.getSession(true);

        Enumeration<String> attributeSessionNames = session.getAttributeNames();
        while (attributeSessionNames.hasMoreElements()) {
            String key = attributeSessionNames.nextElement();
            sessionAttributes.put(key, session.getAttribute(key));
        }

        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String key = parameterNames.nextElement();

            //TODO хардкод
            if (key.equals("include_status")) {
                String[] statuses = request.getParameterValues(key);
                requestAttributes.put(key, statuses);
                continue;
            }

            String value = request.getParameter(key);
            //String[] statuses = request.getParameterValues();
            System.out.println(RequestHandler.class + ":*1**key: " + key + "| - value:|" + value + "|-успешно </b>***<br/>");
            //отсеиваем пустые значения
            if (value != null && !(value.isEmpty())) {
                try {
                    currentCommand = CommandList.valueOf(key.toUpperCase());
                    requestAttributes.put(CMD_VALUE, value); //.trim()
//                    System.out.println("<br/>***<b>currentCommand:" + key + " cmdValue:" + value + "- успешно " + "</b>***<br/>");
                } catch (IllegalArgumentException e) {
                    requestAttributes.put(key, value);
//					System.out.println("<br/>***<b>key:" + key + "| - value:|" + value + "|-успешно </b>***<br/>");
                }
            }
        }
        return currentCommand;
    }


    public HashMap<String, Object> getRequestAttributes() {
        return requestAttributes;
    }

    public HashMap<String, String[]> getRequestParameters() {
        return requestParameters;
    }

    public HashMap<String, Object> getSessionAttributes() {
        return sessionAttributes;
    }

    public void setSessionAttributes(HashMap<String, Object> sessionAttributes) {
        this.sessionAttributes = sessionAttributes;
    }
}