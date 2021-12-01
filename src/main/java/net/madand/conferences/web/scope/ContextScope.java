package net.madand.conferences.web.scope;

import net.madand.conferences.entity.Language;
import net.madand.conferences.l10n.LocalizationHelper;
import net.madand.conferences.service.ServiceFactory;
import org.apache.log4j.Logger;

import javax.servlet.ServletContext;
import java.util.List;

/**
 * Convenience accessors for context attributes that should be available for the whole application lifetime.
 */
public class ContextScope {
    private static final String SERVICE_FACTORY = "serviceFactory";
    private static final String LANGUAGES = "languages";
    private static final String DEFAULT_LANGUAGE = "defaultLanguage";
    private static final String LOCALIZATION_HELPER = "localizationHelper";

    private static final Logger log = Logger.getLogger(ContextScope.class);
    private static final ScopeSupport support = new ScopeSupport(log);

    private ContextScope() {}

    public static ServiceFactory getServiceFactory(ServletContext servletContext) {
        return (ServiceFactory) support.getAttributeOrThrow(servletContext, SERVICE_FACTORY);
    }

    public static void setServiceFactory(ServletContext servletContext, ServiceFactory serviceFactory) {
        support.setAttributeAndLog(servletContext, SERVICE_FACTORY, serviceFactory);
    }

    public static List<Language> getLanguages(ServletContext servletContext) {
        return (List<Language>) support.getAttributeOrThrow(servletContext, LANGUAGES);
    }

    public static void setLanguages(ServletContext servletContext, List<Language> languages) {
        support.setAttributeAndLog(servletContext, LANGUAGES, languages);
    }

    public static Language getDefaultLanguage(ServletContext servletContext) {
        return (Language) support.getAttributeOrThrow(servletContext, DEFAULT_LANGUAGE);
    }

    public static void setDefaultLanguage(ServletContext servletContext, Language language) {
        support.setAttributeAndLog(servletContext, DEFAULT_LANGUAGE, language);
    }

    public static LocalizationHelper getLocalizationHelper(ServletContext servletContext) {
        return (LocalizationHelper) support.getAttributeOrThrow(servletContext, LOCALIZATION_HELPER);
    }

    public static void setLocalizationHelper(ServletContext servletContext, LocalizationHelper localizationHelper) {
        support.setAttributeAndLog(servletContext, LOCALIZATION_HELPER, localizationHelper);
    }
}
