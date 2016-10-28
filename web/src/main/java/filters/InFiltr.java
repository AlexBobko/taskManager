package filters;

import managers.PageManager;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;



/**
 * Servlet Filter implementation class InFiltr
 */
@WebFilter("/*")
public class InFiltr implements Filter {
	public InFiltr() {
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
		response.setContentType("text/html;charset=utf-8"); 		// Encoding page
		request.setCharacterEncoding("UTF-8"); // Encoding Form Data
		HttpServletRequest httpRequest = (HttpServletRequest) request;
//		final static Locale locale = new Locale("ru", "RU");
		HttpSession session = httpRequest.getSession(true);
		if ((boolean) session.isNew()) {
			String page = PageManager.getProperty("path.page.login");
			httpRequest.getRequestDispatcher(page).forward(request, response); 
		} 
		chain.doFilter(request, response);
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
	}

}
