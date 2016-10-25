package controller;

import command.AbsCommand;
import command.CommandList;
import managers.MessageManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * Изоляция запросов для передачи в bsu
 */
public class SessionRequestContent {
	private HashMap<String, Object> requestAttributes;
	private HashMap<String, String[]> requestParametrs;
	private HashMap<String, Object> sessionAttributes;
	CommandList currentCommand;

	public SessionRequestContent() {
		sessionAttributes = new HashMap<>();
		requestAttributes = new HashMap<>();
		requestParametrs = new HashMap<>();
	}

	public CommandList extractValues(HttpServletRequest request) {
		HttpSession session = request.getSession(true);
		Enumeration<String> attributeSessionNames = session.getAttributeNames();
		while (attributeSessionNames.hasMoreElements()) {
			String key;
			key = attributeSessionNames.nextElement();
			sessionAttributes.put(key, session.getAttribute(key));
			}
		// список пришедших элементов (параметров) из запроса
		Enumeration<String> params = request.getParameterNames();
		while (params.hasMoreElements()) {
			String key;
			String value;
			key = params.nextElement();
			value = request.getParameter(key);
			System.out.println("<br/>***<b>key:" + key + "| - value:|" + value + "|-успешно </b>***<br/>");
			//отсеиваем пустые
			if (value != null && !(value.isEmpty())) {
				try {
					//по сути, определелинем типа комманды должен заниматься CommandClient
					currentCommand = CommandList.valueOf(key.toUpperCase());
					requestAttributes.put(AbsCommand.CMD_VALUE, value); //.trim()
					System.out.println("<br/>***<b>currentCommand:" + key + " cmdValue:"+ value  + "- успешно " + "</b>***<br/>");
				} catch (IllegalArgumentException e) {
					requestAttributes.put(key, value);
//					System.out.println("<br/>***<b>key:" + key + "| - value:|" + value + "|-успешно </b>***<br/>");
				}
			}
		}
		return currentCommand;
	}

	// добавление в запрос данных для передачи в jsp
	public HttpServletRequest insertAttributes(HttpServletRequest request) {
		HttpSession session = request.getSession(true);
		if (sessionAttributes == null) {
				session.invalidate();
				request.setAttribute(AbsCommand.MESSAGE, MessageManager.getProperty("message.logout"));
		} else {
			for (Map.Entry<String, Object> entry : sessionAttributes.entrySet()) {
				String key = (String) entry.getKey();
				Object value = (Object) entry.getValue();
				session.setAttribute(key, value);
			}
		}
		for (Map.Entry<String, String[]> entry : requestParametrs.entrySet()) {
			String key = (String) entry.getKey();
			String []value = (String []) entry.getValue();
			session.setAttribute(key, value);
	    }
		return request;
	}

	public HashMap<String, Object> getRequestAttributes() {
		return requestAttributes;
	}

	public void setRequestAttributes(HashMap<String, Object> requestAttributes) {
		this.requestAttributes = requestAttributes;
	}

	public HashMap<String, String[]> getRequestParametrs() {
		return requestParametrs;
	}

	public void setRequestParametrs(HashMap<String, String[]> requestParametrs) {
		this.requestParametrs = requestParametrs;
	}

	public HashMap<String, Object> getSessionAttributes() {
		return sessionAttributes;
	}

	public void setSessionAttributes(HashMap<String, Object> sessionAttributes) {
		this.sessionAttributes = sessionAttributes;
	}

}
//мусор
//Enumeration<String> attributeNames = request.getAttributeNames();
//Enumeration<String> parameterNames = request.getParameterNames();
