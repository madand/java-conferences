package net.madand.conferences.web.scope;

import net.madand.conferences.entity.User;
import org.apache.log4j.Logger;

import javax.servlet.ServletRequest;

/**
 * Convenience accessors for request attributes.
 */
public class RequestScope {
    private static final String USER = "user";

    private static Logger log = Logger.getLogger(RequestScope.class);
    private static final ScopeHelper support = new ScopeHelper(log);

    private RequestScope() {}

    public static User getUser(ServletRequest request) {
        return (User) request.getAttribute(USER);
    }

    public static void setUser(ServletRequest request, User user) {
        support.setAttributeAndLog(request, USER, user);
    }
}
