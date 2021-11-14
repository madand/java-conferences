package net.madand.conferences.web.filter;

import net.madand.conferences.entity.Language;
import net.madand.conferences.l10n.Languages;
import net.madand.conferences.web.constants.SessionAttributes;
import org.apache.log4j.Logger;
import org.apache.taglibs.standard.lang.jstl.test.PageContextImpl;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.jstl.core.Config;
import java.io.IOException;
import java.util.Collections;
import java.util.Locale;

public class LanguageFilter implements Filter {
    private static final Logger log = Logger.getLogger(LanguageFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        log.trace("Filter started");


        // If user supplied the lang query parameter, set the language.
        final Language newLang = detectSetLanguage(req);
        if (newLang != null) {
            req.getSession().setAttribute(SessionAttributes.LANGUAGE, newLang);
            log.debug("Session language was set from query parameter");
        }

        Language currLang = (Language) req.getSession().getAttribute(SessionAttributes.LANGUAGE);
        if (currLang == null) {
            currLang = detectPreferredLanguage(req);
            req.getSession().setAttribute(SessionAttributes.LANGUAGE, currLang);
            log.debug("Session language was set from the client browser preference");
        }

        log.info("Current session language is: " + currLang);
        // Configure JSTL fmt: to use the current language.
        Config.set(req.getServletContext(), Config.FMT_LOCALE, currLang.getCode());

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
