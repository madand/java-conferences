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
    private static final String FLASH_MESSAGE = "flashMessage";
    private static final String FLASH_TYPE = "flashType";
    public static final String PREVIOUS_URL = "previousURL";

    private static final Logger log = Logger.getLogger(SessionScope.class);
    private static final ScopeHelper support = new ScopeHelper(log);

    private SessionScope() {}

    public static Language getCurrentLanguage(HttpSession session) {
        // It's normal to might have return null here.
        return (Language) session.getAttribute(CURRENT_LANGUAGE);
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

    public static void removeCurrentUserId(HttpSession session) {
        support.removeAttributeAndLog(session, CURRENT_USER_ID);
    }

    public static void setFlashMessage(HttpSession session, String message, String type) {
        session.setAttribute(FLASH_MESSAGE, message);
        session.setAttribute(FLASH_TYPE, type);
    }

    public static String getPreviousUrl(HttpSession session, String defaultUrl) {
        String url = (String) session.getAttribute(PREVIOUS_URL);
        if (url == null || url.isEmpty()) {
            return defaultUrl;
        }
        return url;
    }

    public static void setPreviousUrl(HttpSession session, String url) {
        support.setAttributeAndLog(session, PREVIOUS_URL, url);
    }
}
