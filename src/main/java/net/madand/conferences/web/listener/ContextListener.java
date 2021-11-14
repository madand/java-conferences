package net.madand.conferences.web.listener;

import net.madand.conferences.db.dao.LanguageDao;
import net.madand.conferences.entity.Language;
import net.madand.conferences.l10n.Languages;
import net.madand.conferences.web.constants.ServletContextAttributes;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class ContextListener implements ServletContextListener {
    private static Logger log;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext sc = sce.getServletContext();

        // Order matters! Later methods expect the functioning logger and DataSource.
        initLog4J(sc);
        initDataSource(sc);
        initLanguages(sc);
    }

    /**
     * Initializes Log4j framework.
     */
    private void initLog4J(ServletContext servletContext) {
        logToSysOut("Log4J initialization started");

        try {
            PropertyConfigurator.configure(servletContext.getRealPath("WEB-INF/log4j.properties"));
            log = Logger.getLogger(ContextListener.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize Log4j", e);
        }

        logToSysOut("Log4J initialization finished.");
    }

    private void initDataSource(ServletContext servletContext) {
        log.trace("DataSource initialization started");

        try {
            Context ctx = (Context) new InitialContext().lookup("java:comp/env");
            servletContext.setAttribute(ServletContextAttributes.DATA_SOURCE, ctx.lookup("jdbc/conferences"));
        } catch (NamingException e) {
            log.error("DataSource initialization failed", e);
            throw new RuntimeException("Failed to initialize the Data Source.", e);
        }

        log.info("DataSource initialization finished");
    }

    /**
     * Load the languages from DB and cache them for the whole app lifetime.
     *
     * @param servletContext
     */
    private void initLanguages(ServletContext servletContext) {
        log.trace("Languages initialization started");

        final DataSource dataSource = (DataSource) servletContext.getAttribute(ServletContextAttributes.DATA_SOURCE);
        try (Connection connection = dataSource.getConnection()) {
            final List<Language> languages = LanguageDao.findAll(connection);
            languages.forEach(Languages::add);
            servletContext.setAttribute(ServletContextAttributes.LANGUAGES, languages);
        } catch (SQLException throwables) {
            log.error("Languages query failed", throwables);
            throw new RuntimeException("Failed to initialize the languages", throwables);
        }

        log.info("Languages initialization finished");
    }

    private void logToSysOut(String msg) {
        System.out.printf("[%s] %s%n", getClass().getSimpleName(), msg);
    }
}
