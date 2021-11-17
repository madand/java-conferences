package net.madand.conferences.web.filter;

import net.madand.conferences.entity.User;
import net.madand.conferences.service.ServiceException;
import net.madand.conferences.service.impl.UserService;
import net.madand.conferences.web.scope.ContextScope;
import net.madand.conferences.web.scope.RequestScope;
import net.madand.conferences.web.scope.SessionScope;
import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Optional;

/**
 * Filter that loads the user entity from the database and saves it in the request scope. This is done only if a user
 * is currently logged in (user ID attribute is seen the session).
 */
public class LoadUserFilter implements Filter {
    private static final Logger log = Logger.getLogger(LoadUserFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.trace("LoadUserFilter started");

        final HttpSession session = ((HttpServletRequest) request).getSession();
        Integer userId = SessionScope.getCurrentUserId(session);

        if (userId == null) {
            log.debug("No user is logged in. Bailing out.");
            chain.doFilter(request, response);
            return;
        }

        UserService userService = ContextScope.getServiceFactory(request.getServletContext()).getUserService();

        Optional<User> user;
        try {
            user = userService.findById(userId);
        } catch (ServiceException e) {
            log.error("Failed to load a user with ID=" + userId + ". Invalidating the session.", e);
            // Logout the user, just in case.
            session.invalidate();
            chain.doFilter(request, response);
            return;
        }

        if (!user.isPresent()) {
            log.debug("User not was not found in the database. Invalidating the session.");
            session.invalidate();
            chain.doFilter(request, response);
            return;
        }

        RequestScope.setUser(request, user.get());
        log.debug("request.user was set to: " + user.get());

        chain.doFilter(request, response);
    }
}
