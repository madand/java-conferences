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
    private static final String ATTR_DEFAULT_LANGUAGE = "defaultLanguage";
    private static final String ATTR_EXTRA_LANGUAGES = "extraLanguages";

    private static final Logger log = Logger.getLogger(ContextHelper.class);
    private static ServletContext context;

    private ContextHelper() {}

    public static ServletContext getContext() {
        if (context == null) {
            throw new RuntimeException("Context was not set.");
        }
        return context;
    }

    public static void setContext(ServletContext servletContext) {
        context = servletContext;
    }

    public static DataSource getDataSource() {
        final DataSource dataSource = (DataSource) context.getAttribute(ATTR_DATA_SOURCE);
        if (dataSource == null) {
            throw new RuntimeException("ServletContext has no data source.");
        }
        return dataSource;
    }

    public static void setDataSource(DataSource dataSource) {
        context.setAttribute(ATTR_DATA_SOURCE, dataSource);
        log.trace("Set dataSource to: " + dataSource.toString());
    }

    public static Language getDefaultLanguage() {
        final Language defaultLanguage = (Language) context.getAttribute(ATTR_DEFAULT_LANGUAGE);
        if (defaultLanguage == null) {
            throw new RuntimeException("ServletContext has no default language.");
        }
        return defaultLanguage;
    }

    public static void setDefaultLanguage(Language language) {
        context.setAttribute(ATTR_DEFAULT_LANGUAGE, language);
        log.trace("Set defaultLanguage to: " + language);
    }

    public static List<Language> getExtraLanguages() {
        final List<Language> extraLanguages = (List<Language>) context.getAttribute(ATTR_EXTRA_LANGUAGES);
        if (extraLanguages == null) {
            throw new RuntimeException("ServletContext has no extra languages.");
        }
        return extraLanguages;
    }

    public static void setExtraLanguages(List<Language> languages) {
        context.setAttribute(ATTR_EXTRA_LANGUAGES, languages);
        log.trace("Set extraLanguages to: " + languages);
    }
}
