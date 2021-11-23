package net.madand.conferences.web.filter;

import net.madand.conferences.web.scope.SessionScope;
import net.madand.conferences.web.util.URLManager;
import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Filter that loads the user entity from the database and saves it in the request scope. This is done only if a user
 * is currently logged in (user ID attribute is seen the session).
 */
public class PreviousURLFilter implements Filter {
    private static final Logger log = Logger.getLogger(PreviousURLFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        chain.doFilter(request, response);

        log.trace("PreviousURLFilter started");

        final HttpServletRequest httpRequest = (HttpServletRequest) request;
        if (((HttpServletResponse) response).getStatus() != HttpServletResponse.SC_OK) {
            return;
        }

        final HttpSession session = httpRequest.getSession();
        if ("GET".equals(httpRequest.getMethod()) && httpRequest.getParameter("lang") == null) {
            SessionScope.setPreviousUrl(session,
                    URLManager.buildURLPreserveQuery(httpRequest.getServletPath(), httpRequest));
        }
    }
}
