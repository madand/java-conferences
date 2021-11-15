package net.madand.conferences.web.util;

import net.madand.conferences.entity.Language;

import javax.servlet.http.HttpSession;

public class SessionHelper {
    private static final String ATTR_CURRENT_LANGUAGE = "currentLanguage";

    private SessionHelper() {}

    public static Language getCurrentLanguage(HttpSession session) {
        final Language language = (Language) session.getAttribute(ATTR_CURRENT_LANGUAGE);
        if (language == null) {
            throw new RuntimeException(String.format("Session has no %s attribute", ATTR_CURRENT_LANGUAGE));
        }
        return language;
    }

    public static void setCurrentLanguage(HttpSession session, Language language) {
        session.setAttribute(ATTR_CURRENT_LANGUAGE, language);
    }
}
