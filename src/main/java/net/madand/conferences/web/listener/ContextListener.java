package net.madand.conferences.web.listener;

import net.madand.conferences.entity.Language;
import net.madand.conferences.l10n.Languages;
import net.madand.conferences.service.ServiceException;
import net.madand.conferences.service.ServiceFactory;
import net.madand.conferences.service.impl.LanguageService;
import net.madand.conferences.web.scope.ContextScope;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.jsp.jstl.core.Config;
import javax.sql.DataSource;
import java.util.List;

public class ContextListener implements ServletContextListener {
    private static final String JDBC_CONFERENCES = "jdbc/conferences";
    private static Logger log;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext sc = sce.getServletContext();

        // Order matters! Later methods expect the functioning logger and DataSource.
        initLog4J(sc);
        initServiceFactory(sc);
        initLanguages(sc);
    }

    /**
     * Initializes Log4j framework.
     */
    private void initLog4J(ServletContext servletContext) {
        logToSysOut("initLog4J started");

        try {
            PropertyConfigurator.configure(servletContext.getRealPath("WEB-INF/log4j.properties"));
            log = Logger.getLogger(ContextListener.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize Log4j", e);
        }

        log.info("initLog4J finished.");
    }

    private void initServiceFactory(ServletContext servletContext) {
        log.trace("initServiceFactory started");

        try {
            Context ctx = (Context) new InitialContext().lookup("java:comp/env");
            final DataSource dataSource = (DataSource) ctx.lookup(JDBC_CONFERENCES);
            if (dataSource == null) {
                throw new IllegalStateException("Lookup for " + JDBC_CONFERENCES + " returned NULL. Valid data source expected.");
            }
            ContextScope.setServiceFactory(servletContext, new ServiceFactory(dataSource));
        } catch (NamingException | IllegalStateException e) {
            log.error("DataSource initialization failed", e);
            throw new RuntimeException("Failed to initialize the Data Source.", e);
        }

        log.info("initServiceFactory finished");
    }

    /**
     * Load the languages from DB and cache them for the whole app lifetime.
     *
     * @param servletContext
     */
    private void initLanguages(ServletContext servletContext) {
        log.trace("initLanguages started");

        final LanguageService languageService = ContextScope.getServiceFactory(servletContext).getLanguageService();
        try {
            final List<Language> languages = languageService.findAll();
            languages.forEach(Languages::add);
            ContextScope.setLanguages(servletContext, languages);

            // Set the default language on the context level.
            final Language defaultLanguage = Languages.getDefaultLanguage();
            ContextScope.setDefaultLanguage(servletContext, defaultLanguage);
            Config.set(servletContext, Config.FMT_LOCALE, defaultLanguage.getCode());
            Config.set(servletContext, Config.FMT_TIME_ZONE, "Europe/Kiev");
        } catch (ServiceException e) {
            log.error("Languages query failed", e);
            throw new RuntimeException("Failed to initialize the languages", e);
        }

        log.info("initLanguages finished");
    }

    private void logToSysOut(String msg) {
        System.out.printf("[%s] %s%n", getClass().getSimpleName(), msg);
    }
}
