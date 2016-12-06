package filters;

import managers.PageManager;
import org.hibernate.Session;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Servlet Filter implementation class InFilter
 */
@WebFilter("/*")
public class InFilter implements Filter {
    public InFilter() {
    }

    /**
     * @see Filter#destroy()
     */
    public void destroy() {
    }

    /**
     * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
     */
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        response.setContentType("text/html;charset=utf-8");        // Encoding page
        request.setCharacterEncoding("UTF-8"); // Encoding Form Data
        HttpServletRequest httpRequest = (HttpServletRequest) request;
//		final static Locale locale = new Locale("ru", "RU");
        HttpSession session = httpRequest.getSession(true);
        if (session.isNew()) {
            String page = PageManager.getProperty("path.page.login");
            httpRequest.getRequestDispatcher(page).forward(request, response);
        }
        chain.doFilter(request, response);

        Session sessionHibernate = HibernateUtil.getHibernateUtil().getSession();
        if (sessionHibernate.isOpen()) {

            HibernateUtil.getHibernateUtil().printStats(8);
            System.out.println("sessionHibernate: " + sessionHibernate.getStatistics());
            System.out.println("sessionHibernate.clear ");
            sessionHibernate.clear();
            HibernateUtil.getHibernateUtil().printStats(9);

            System.out.println("sessionHibernate: " + sessionHibernate.getStatistics());
            sessionHibernate.close();
            HibernateUtil.getHibernateUtil().removeSession();
            System.out.println("*****sessionHibernate.close + null*******");
        }
    }

    /**
     * @see Filter#init(FilterConfig)
     */
    public void init(FilterConfig fConfig) throws ServletException {
    }

}
