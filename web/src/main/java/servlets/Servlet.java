package servlets;

import command.CommandList;
import command.ICommand;
import controller.RequestHandler;
import dto.Account;
import managers.MessageManager;
import managers.PageManager;

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
@WebServlet({"/Servlet", "/go", "/user*", "/admin*", "/index.jsp"})

public class Servlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public Servlet() {
        super();
    }
    //TODO ?? doGet сделать отдельный RequestHandler?
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String page=null;
        HttpSession session = request.getSession(true);
        String messageFildName = ICommand.MESSAGE;
        try {
            Account account = (Account) session.getAttribute(ICommand.ACCOUNT);
            int role = account.getUser().getRole();
            if (role == 1) {
                page = PageManager.getProperty("path.page.user");
                session.setAttribute(messageFildName, MessageManager.getProperty("message.hello.user"));
            }else if (role == 2) {
                page = PageManager.getProperty("path.page.superior");
                session.setAttribute(messageFildName, MessageManager.getProperty("message.hello.user"));
            }
        } catch (Exception e) {
            //log add
        }
        if (page==null) {
            page= PageManager.getProperty("path.page.login");
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