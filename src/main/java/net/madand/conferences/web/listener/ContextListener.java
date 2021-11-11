package net.madand.conferences.web.listener;

import net.madand.conferences.db.dao.LanguageDao;
import net.madand.conferences.entity.Language;
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
import java.util.ArrayList;
import java.util.List;

public class ContextListener implements ServletContextListener {
    private static Logger log;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext sc = sce.getServletContext();
        initLog4J(sc);
        initDataSource(sc);
        initLanguages(sc);
    }

    /**
     * Initializes log4j framework.
     *
     * @param servletContext
     */
    private void initLog4J(ServletContext servletContext) {
        logToSysOut("Log4J initialization started");
        try {
            PropertyConfigurator.configure(servletContext.getRealPath("WEB-INF/log4j.properties"));
            log = Logger.getLogger(ContextListener.class);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        logToSysOut("Log4J initialization finished");
    }

    private void initDataSource(ServletContext servletContext) {
        log.trace("DataSource initialization started");
        try {
            Context ctx = (Context) new InitialContext().lookup("java:comp/env");
            DataSource ds = (DataSource) ctx.lookup("jdbc/conferences");
            servletContext.setAttribute("dataSource", ds);
        } catch (NamingException e) {
            log.error("DataSource initialization failed", e);
            throw new RuntimeException("Cannot setup the data source. App cannot function.", e);
        }
        log.info("DataSource initialization finished");
    }

    private void initLanguages(ServletContext servletContext) {
        log.trace("Languages initialization started");
        try {
            DataSource ds = (DataSource) servletContext.getAttribute("dataSource");
            Connection connection = ds.getConnection();
            List<Language> allLanguages = LanguageDao.findAll(connection);
//            connection.close();

            List<Language> extraLanguages = new ArrayList<>();
            for (Language language : allLanguages) {
                if (language.isDefault()) {
                    servletContext.setAttribute("defaultLanguage", language);
                    log.trace("Set defaultLanguage attribute to: " + language);
                } else {
                    extraLanguages.add(language);
                }
            }
            log.trace("Set extraLanguages attribute to: " + extraLanguages);
            servletContext.setAttribute("extraLanguages", extraLanguages);
        } catch (SQLException throwables) {
            log.error("Languages query failed", throwables);
            throw new RuntimeException("Cannot setup the data source. App cannot function.", throwables);
        }
        log.info("Languages initialization finished");
    }

    private void logToSysOut(String msg) {
        System.out.printf("[%s] %s%n", getClass().getSimpleName(), msg);
    }
}
