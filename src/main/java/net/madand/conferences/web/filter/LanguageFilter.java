package net.madand.conferences.web.filter;

import net.madand.conferences.entity.Language;
import net.madand.conferences.l10n.Languages;
import net.madand.conferences.web.constant.SessionAttributes;
import net.madand.conferences.web.util.SessionHelper;
import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.jstl.core.Config;
import java.io.IOException;
import java.util.Collections;
import java.util.Locale;

public class LanguageFilter implements Filter {
    private static final Logger log = Logger.getLogger(LanguageFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        final HttpServletRequest req = (HttpServletRequest) request;
        final HttpSession session = req.getSession();
        log.trace("Filter started");

        // If user supplied the lang query parameter, set the language.
        final Language newLang = detectSetLanguage(req);
        if (newLang != null) {
            SessionHelper.setCurrentLanguage(session, newLang);
            log.debug("Session language was set from query parameter");
        }

        Language currLang = (Language) session.getAttribute(SessionAttributes.CURRENT_LANGUAGE);
        if (currLang == null) {
            currLang = detectPreferredLanguage(req);
            SessionHelper.setCurrentLanguage(session, currLang);
            log.debug("Session language was set from the client browser preference");
        }

        log.info("Current session language is: " + currLang);
        // Configure JSTL fmt: to use the current language.
        Config.set(session, Config.FMT_LOCALE, currLang.getCode());

        chain.doFilter(request, response);
    }

    private Language detectSetLanguage(HttpServletRequest req) {
        // Query parameter lang overrides current values, if it designates valid language.
        String langParam = req.getParameter("lang");
        if (langParam != null) {
            return Languages.getByCode(langParam);
        }

        return null;
    }

    private Language detectPreferredLanguage(HttpServletRequest req) {
        // Try to find a match with the browser preferred languages list.
        for (Locale locale : Collections.list(req.getLocales())) {
            final Language lang = Languages.getByCode(locale.getLanguage());
            if (lang != null) {
                return lang;
            }
        }

        // Fallback to the default.
        return Languages.getDefaultLanguage();
    }
}
