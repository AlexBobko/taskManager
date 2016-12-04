package servlets;

import command.CommandList;
import command.ICommand;
import controller.PageMapper;
import controller.RequestHandler;
import loc.task.vo.Account;
import lombok.extern.log4j.Log4j;
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
@Log4j
public class Servlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public Servlet() {
        super();
    }

    //TODO ?? doGet сделать отдельный RequestHandler?
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String page = null;
        HttpSession session = request.getSession(true);
        String messageFieldName = ICommand.MESSAGE;
        try {
            Account account = (Account) session.getAttribute(ICommand.ACCOUNT);
            page = PageMapper.getPageMapper().getTaskListPage(account.getUser().getRole());
            session.setAttribute(messageFieldName, MessageManager.getProperty("message.hello.user"));
        } catch (Exception e) {
            log.error(e, e);
        }
        if (page == null) {
            page = PageManager.getProperty("path.page.login");
            session.setAttribute(messageFieldName, MessageManager.getProperty("message.need.login"));
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
        if (page == null) {
            System.out.println("***<b>page = null!! " + " </b>***");
            page = PageManager.getProperty("path.page.login");
        }
        // getServletContext().getRequestDispatcher(page).forward(request, response);
        getServletContext().getRequestDispatcher(page).include(request, response);
    }

}