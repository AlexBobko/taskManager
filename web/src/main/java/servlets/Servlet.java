package servlets;

import command.ICommand;
import command.CommandList;
import controller.RequestHandler;
import dto.UserDTO;
import managers.PageManager;
import managers.MessageManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;


/**
 * Servlet implementation class Servlet
 */
@WebServlet({"/Servlet", "/go", "/user*", "/admin*", "/index.jspx", "/index.jsp"})

public class Servlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public Servlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String page = PageManager.getProperty("path.page.login");
        HttpSession session = request.getSession(true);
        String messageFildName = ICommand.MESSAGE;    //TODO
        String userFildName = ICommand.PARAM_SESSION_USER;    //TODO
        try {
            UserDTO user = (UserDTO) session.getAttribute(userFildName);
            if (user != null) {
                page = PageManager.getProperty("path.page.user");
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
        RequestHandler content = new RequestHandler();
        CommandList currentCommand = content.extractValues(request);
        page = currentCommand.getCurrentCommand().execute(content);
        request = content.insertInRequest(request);
//        System.out.println(page);
        if (page == null) {
            // out.println("<br/>***<b>page = null!! " + " </b>***<br/>");
            page = PageManager.getProperty("path.page.login");
        }
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
