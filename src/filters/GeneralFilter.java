package filters;

import servlets.AccesoServlet;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(filterName = "GeneralFilter", urlPatterns = "/inicio/*")
public class GeneralFilter implements Filter {
    public void destroy() {
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        Object obj = ((HttpServletRequest) request).getSession().getAttribute(AccesoServlet.USER_LOGGED);
        if (obj == null) {
            ((HttpServletResponse) response).sendRedirect(((HttpServletRequest) request).getContextPath());
        } else
            chain.doFilter(request, response);
    }

    public void init(FilterConfig config) throws ServletException {
    }
}
