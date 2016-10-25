package controller;

import command.AbsCommand;
import command.CommandList;
import managers.MessageManager;
import service.LoginService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * Изоляция запросов для передачи в bsu
 */
public class RequestHandler {
    private HashMap<String, Object> requestAttributes;
    private HashMap<String, String[]> requestParameters;
    private HashMap<String, Object> sessionAttributes;
    CommandList currentCommand;

    public RequestHandler() {
        sessionAttributes = new HashMap<>();
        requestAttributes = new HashMap<>();
        requestParameters = new HashMap<>();
    }

    public CommandList extractValues(HttpServletRequest request) {
        HttpSession session = request.getSession(true);
        Enumeration<String> attributeSessionNames = session.getAttributeNames();
        Enumeration<String> parameterNames = request.getParameterNames();

        while (attributeSessionNames.hasMoreElements()) {
            String key;
            key = attributeSessionNames.nextElement();
            sessionAttributes.put(key, session.getAttribute(key));
        }


        while (parameterNames.hasMoreElements()) {
            String key;
            String value;
            key = parameterNames.nextElement();
            value = request.getParameter(key);

            System.out.println("<br/>***<b>key:" + key + "| - value:|" + value + "|-успешно </b>***<br/>");
            //отсеиваем пустые значения
            if (value != null && !(value.isEmpty())) {
                LoginService loginService =new LoginService();
                try {
                    //TODO ?? вынести определение типа комманды в CommandClient или зачем гонять 2 раза цикл
                    currentCommand = CommandList.valueOf(key.toUpperCase());
                    requestAttributes.put(AbsCommand.CMD_VALUE, value); //.trim()

                    System.out.println("<br/>***<b>currentCommand:" + key + " cmdValue:" + value + "- успешно " + "</b>***<br/>");
                } catch (IllegalArgumentException e) {
                    requestAttributes.put(key, value);
//					System.out.println("<br/>***<b>key:" + key + "| - value:|" + value + "|-успешно </b>***<br/>");
                }
            }
        }
        return currentCommand;
    }

    //возвращаем Request
    public HttpServletRequest insertInRequest(HttpServletRequest request) {
        HttpSession session = request.getSession(true);
        if (sessionAttributes == null) {
//				session.invalidate();
            request.setAttribute(AbsCommand.MESSAGE, MessageManager.getProperty("message.logout"));
        } else {
            for (Map.Entry<String, Object> entry : sessionAttributes.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                session.setAttribute(key, value);
            }
        }
        if (requestParameters != null) {
            for (Map.Entry<String, String[]> entry : requestParameters.entrySet()) {
                String key = entry.getKey();
                String[] value = entry.getValue();
                session.setAttribute(key, value);
            }
        }
        return request;
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