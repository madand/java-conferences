package net.madand.conferences.web.scope;

import net.madand.conferences.entity.Language;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpSession;

/**
 * Convenience accessors for session attributes.
 */
public class SessionScope {
    private static final String CURRENT_LANGUAGE = "currentLanguage";
    private static final String CURRENT_USER_ID = "currentUserId";

    private static Logger log = Logger.getLogger(SessionScope.class);
    private static final ScopeHelper support = new ScopeHelper(log);

    private SessionScope() {}

    public static Language getCurrentLanguage(HttpSession session) {
        return (Language) support.getAttributeOrThrow(session, CURRENT_LANGUAGE);
    }

    public static void setCurrentLanguage(HttpSession session, Language language) {
        support.setAttributeAndLog(session, CURRENT_LANGUAGE, language);
    }

    public static Integer getCurrentUserId(HttpSession session) {
        // It's normal to might have return null here.
        return (Integer) session.getAttribute(CURRENT_USER_ID);
    }

    public static void setCurrentUserId(HttpSession session, Integer currentUserId) {
        support.setAttributeAndLog(session, CURRENT_USER_ID, currentUserId);
    }
}
