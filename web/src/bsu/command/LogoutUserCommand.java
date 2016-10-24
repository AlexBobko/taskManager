package bsu.command;

import controller.SessionRequestContent;
import managers.ConfigurationManager;

public class LogoutUserCommand extends AbsCommand {
	@Override
	public String execute(SessionRequestContent content) {
		try {
			content.setSessionAttributes(null);
		} catch (Exception e) {
		}
		String page = ConfigurationManager.getProperty("path.page.login");
		/*
		 * message = MessageManager.getProperty("message.logout"); content.getSessionAttributes().put(PARAM_MESSAGE, message);
		 */
		return page;
	}
}
