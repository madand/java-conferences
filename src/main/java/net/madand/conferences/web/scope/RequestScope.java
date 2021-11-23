package net.madand.conferences.web.scope;

import net.madand.conferences.entity.User;
import org.apache.log4j.Logger;

import javax.servlet.ServletRequest;
import java.util.Optional;

/**
 * Convenience accessors for request attributes.
 */
public class RequestScope {
    private static final String USER = "user";

    private static final Logger log = Logger.getLogger(RequestScope.class);
    private static final ScopeSupport support = new ScopeSupport(log);

    private RequestScope() {}

    public static Optional<User> getUser(ServletRequest request) {
        return Optional.ofNullable((User) request.getAttribute(USER));
    }

    public static void setUser(ServletRequest request, User user) {
        support.setAttributeAndLog(request, USER, user);
    }
}
