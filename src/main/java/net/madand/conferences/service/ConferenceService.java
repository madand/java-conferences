package net.madand.conferences.service;

import net.madand.conferences.db.dao.ConferenceDao;
import net.madand.conferences.db.dao.ConferenceTranslationDao;
import net.madand.conferences.entity.Conference;
import net.madand.conferences.entity.ConferenceTranslation;
import net.madand.conferences.entity.Language;
import org.apache.log4j.Logger;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class ConferenceService {
    private static final Logger log = Logger.getLogger(ConferenceService.class);

    private DataSource dataSource;

    public ConferenceService(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<Conference> findAllTranslated(Language language) throws ServiceException {
        log.trace("Begin findAllTranslated conferences");

        try (Connection conn = getConnection()) {
            return ConferenceDao.findAll(conn, language);
        } catch (SQLException throwables) {
            final String message = "Error fetching conferences list";
            log.error(message, throwables);
            throw new ServiceException(message, throwables);
        }
    }

    public void create(Conference conference, List<ConferenceTranslation> translations) throws ServiceException {
        log.trace("Begin create conference");
        log.debug("Data: " + conference + " " + translations);

        try (Connection conn = getConnection()) {
            conn.setAutoCommit(false);

            ConferenceDao.insert(conn, conference);
            for (ConferenceTranslation translation : translations) {
                ConferenceTranslationDao.insert(conn, translation);
            }

            conn.commit();
        } catch (SQLException throwables) {
            log.error("Error during inserts", throwables);
            throw new ServiceException("Error saving conference into the database", throwables);
        }
    }

    private Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
