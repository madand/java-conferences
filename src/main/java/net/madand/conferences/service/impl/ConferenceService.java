package net.madand.conferences.service.impl;

import net.madand.conferences.db.dao.ConferenceDao;
import net.madand.conferences.db.dao.ConferenceTranslationDao;
import net.madand.conferences.entity.Conference;
import net.madand.conferences.entity.ConferenceTranslation;
import net.madand.conferences.entity.Language;
import net.madand.conferences.service.AbstractService;
import net.madand.conferences.service.ServiceException;

import javax.sql.DataSource;
import java.util.List;

public class ConferenceService extends AbstractService {
    public ConferenceService(DataSource dataSource) {
        super(dataSource);
    }

    public List<Conference> findAllTranslated(Language language) throws ServiceException {
        return callNoTransaction(
                connection -> ConferenceDao.findAll(connection, language),
                "Error fetching conferences"
        );
    }

    public void create(Conference conference, List<ConferenceTranslation> translations) throws ServiceException {
        runWithinTransaction(
                connection -> {
                    ConferenceDao.insert(connection, conference);
                    for (ConferenceTranslation translation : translations) {
                        ConferenceTranslationDao.insert(connection, translation);
                    }
                },
                "Failed to save the conference into the database"
        );
    }
}
