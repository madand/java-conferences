package net.madand.conferences.web.util;

import net.madand.conferences.entity.Language;
import org.apache.log4j.Logger;

import javax.servlet.ServletContext;
import javax.sql.DataSource;
import java.util.List;

/**
 * Convenience accessors for context attributes that should be available for the whole application lifetime.
 */
public class ContextHelper {
    private static final String ATTR_DATA_SOURCE = "dataSource";
    private static final String ATTR_LANGUAGES = "languages";
    private static final String ATTR_DEFAULT_LANGUAGE = "defaultLanguage";

    private static Logger log = Logger.getLogger(ContextHelper.class);

    private ContextHelper() {}

    public static DataSource getDataSource(ServletContext servletContext) {
        final DataSource dataSource = (DataSource) servletContext.getAttribute(ATTR_DATA_SOURCE);
        if (dataSource == null) {
            throw new RuntimeException("ServletContext has no data source.");
        }
        return dataSource;
    }

    public static void setDataSource(ServletContext servletContext, DataSource dataSource) {
        servletContext.setAttribute(ATTR_DATA_SOURCE, dataSource);
        log.trace("Set dataSource to: " + dataSource.toString());
    }

    public static List<Language> getLanguages(ServletContext servletContext) {
        final List<Language> extraLanguages = (List<Language>) servletContext.getAttribute(ATTR_LANGUAGES);
        if (extraLanguages == null) {
            throw new RuntimeException("ServletContext has no languages.");
        }
        return extraLanguages;
    }

    public static void setLanguages(ServletContext servletContext, List<Language> languages) {
        servletContext.setAttribute(ATTR_LANGUAGES, languages);
        log.trace("Set languages to: " + languages);
    }

    public static Language getDefaultLanguage(ServletContext servletContext) {
        final Language defaultLanguage = (Language) servletContext.getAttribute(ATTR_DEFAULT_LANGUAGE);
        if (defaultLanguage == null) {
            throw new RuntimeException("ServletContext has no default language.");
        }
        return defaultLanguage;
    }

    public static void setDefaultLanguage(ServletContext servletContext, Language language) {
        servletContext.setAttribute(ATTR_DEFAULT_LANGUAGE, language);
        log.trace("Set defaultLanguage to: " + language);
    }
}
