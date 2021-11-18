package net.madand.conferences.web.filter;

import net.madand.conferences.entity.Language;
import net.madand.conferences.l10n.Languages;
import net.madand.conferences.web.scope.SessionScope;
import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.jstl.core.Config;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Locale;
import java.util.Optional;

public class LanguageFilter implements Filter {
    private static final String LANGUAGE_COOKIE ="currentLanguage";

    private static final Logger log = Logger.getLogger(LanguageFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.trace("LanguageFilter started");

        final HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        final HttpServletResponse httpServletResponse  = (HttpServletResponse) response;
        final HttpSession session = httpServletRequest.getSession();

        // If user supplied the lang query parameter, set the language.
        final Language newLang = detectLanguageInQuery(httpServletRequest);
        if (newLang != null) {
            SessionScope.setCurrentLanguage(session, newLang);
            log.debug("Session language was set by the query param");
        }

        Language currLang = SessionScope.getCurrentLanguage(session);
        if (currLang == null) {
            currLang = detectPreferredLanguage(httpServletRequest);
            SessionScope.setCurrentLanguage(session, currLang);
        }

        log.info("Current session language is: " + currLang);
        // Configure JSTL fmt: to use the current language.
        Config.set(session, Config.FMT_LOCALE, currLang.getCode());
        // Remember the language in the cookie, in case the session is invalidated.
        saveLanguageInCookie(currLang, httpServletRequest, httpServletResponse);

        chain.doFilter(request, response);
    }

    private Language detectLanguageInQuery(HttpServletRequest request) {
        // Query parameter lang overrides the current values, if it designates valid language.
        String langParam = request.getParameter("lang");
        if (langParam != null) {
            return Languages.getByCode(langParam);
        }

        return null;
    }

    private Language detectPreferredLanguage(HttpServletRequest request) {
        // Check whether there is a language cookie.
        final Language cookieLang = getLanguageFromCookie(request);
        if (cookieLang != null) {
            log.debug("Setting form the cookie");
            return cookieLang;
        }

        // Try to find a match within the browser's preferred languages list.
        for (Locale locale : Collections.list(request.getLocales())) {
            final Language lang = Languages.getByCode(locale.getLanguage());
            if (lang != null) {
                log.debug("Setting form the browser's preferred languages list");
                return lang;
            }
        }

        log.debug("Falling back to the default language");
        return Languages.getDefaultLanguage();
    }

    private Language getLanguageFromCookie(HttpServletRequest request) {
        return Arrays.stream(Optional.ofNullable(request.getCookies()).orElse(new Cookie[0]))
                .filter(cookie -> LANGUAGE_COOKIE.equals(cookie.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .map(Languages::getByCode)
                .orElse(null);
    }

    private void saveLanguageInCookie(Language language, HttpServletRequest request,HttpServletResponse response) {
        Cookie cookie = new Cookie(LANGUAGE_COOKIE, language.getCode());
        cookie.setHttpOnly(true);
        cookie.setPath(request.getContextPath());
        // Remember the selected language for 30 days.
        cookie.setMaxAge(3600 * 24 * 30);
        response.addCookie(cookie);
    }
}
