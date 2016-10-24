package controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bsu.command.AbsCommand;
import bsu.command.CommandList;
import dto.UserDTO;
import managers.ConfigurationManager;
import managers.MessageManager;

/**
 * Servlet implementation class Servlet
 */
@WebServlet({ "/Servlet", "/go", "/user*", "/admin*", "/index.jspx", "/index.jsp" })

public class Servlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public Servlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String page = ConfigurationManager.getProperty("path.page.login");
		HttpSession session = request.getSession(true);
		String messageFildName = AbsCommand.MESSAGE;	//TODO
		String userFildName = AbsCommand.PARAM_SESSION_USER;	//TODO
		try {
			UserDTO user = (UserDTO) session.getAttribute(userFildName);
			if (user != null) {
				page = ConfigurationManager.getProperty("path.page.user");
				session.setAttribute(messageFildName, MessageManager.getProperty("message.hello.user"));
			}
		} catch (Exception e) {
			session.setAttribute(messageFildName, MessageManager.getProperty("message.need.login"));
		}
		// getServletContext().getRequestDispatcher(page).forward(request, response);
		getServletContext().getRequestDispatcher(page).include(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String page;
		SessionRequestContent content = new SessionRequestContent();
		CommandList currentCommand = content.extractValues(request);
		page = currentCommand.getCurrentCommand().execute(content);
		request = content.insertAttributes(request);
		System.out.println(page);
		// getServletContext().getRequestDispatcher(page).forward(request, response);
		getServletContext().getRequestDispatcher(page).include(request, response);
	}

}

// ошибку авторизации добавить

// PrintWriter out = response.getWriter();
// HttpSession session = request.getSession(true);
// doGet(request, response);

// PrintWriter out = response.getWriter();
// HttpSession session = request.getSession(true);
// doGet(request, response);

// if (page == null) {
//// out.println("<br/>***<b>page = null!! " + " </b>***<br/>");//TODO
// page = ConfigurationManager.getProperty("path.page.user");
//
// }