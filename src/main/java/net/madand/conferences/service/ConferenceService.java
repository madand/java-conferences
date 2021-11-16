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

public class ConferenceService extends AbstractService {
    private static final Logger log = Logger.getLogger(ConferenceService.class);

    public ConferenceService(DataSource dataSource) {
        super(dataSource);
    }

    public List<Conference> findAllTranslated(Language language) throws ServiceException {
        log.trace("findAllTranslated conferences");

        try (Connection conn = getConnection()) {
            return ConferenceDao.findAll(conn, language);
        } catch (SQLException throwables) {
            final String message = "Error fetching conferences list";
            log.error(message, throwables);
            throw new ServiceException(message, throwables);
        }
    }

    public void create(Conference conference, List<ConferenceTranslation> translations) throws ServiceException {
        log.trace("create conference");
        log.debug("Data: " + conference + " " + translations);

        runWithinTransaction(
                connection -> {
                    ConferenceDao.insert(connection, conference);
                    for (ConferenceTranslation translation : translations) {
                        ConferenceTranslationDao.insert(connection, translation);
                    }
                },
                makeDefaultHandler("Error saving conference into the database", log)
        );
    }
}
