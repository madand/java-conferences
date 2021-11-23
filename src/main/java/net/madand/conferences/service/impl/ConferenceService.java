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
import java.util.Optional;

public class ConferenceService extends AbstractService {
    public ConferenceService(DataSource dataSource) {
        super(dataSource);
    }

    public List<Conference> findAllTranslated(Language language) throws ServiceException {
        return callNoTransaction(
                connection -> ConferenceDao.findAll(connection, language),
                "Error fetching conferences");
    }

    public Optional<Conference> findOne(int id, Language language) throws ServiceException {
        return callNoTransaction(
                connection -> ConferenceDao.findOne(connection, id, language),
                "Error fetching conference");
    }

    public Optional<Conference> findOneWithTranslations(int id, List<Language> languages) throws ServiceException {
        return callNoTransaction(
                connection -> {
                    Optional<Conference> conferenceOptional = ConferenceDao.findOne(connection, id);
                    if (conferenceOptional.isPresent()) {
                        final Conference conference = conferenceOptional.get();
                        for (Language language : languages) {
                            ConferenceTranslationDao.findOne(connection, conference, language)
                                    .ifPresent(conference::addTranslation);
                        }
                    }

                    return conferenceOptional;
                },
                "Error fetching conference");
    }

    public Optional<Conference> findOne(int id) throws ServiceException {
        return callNoTransaction(
                connection -> ConferenceDao.findOne(connection, id),
                "Error fetching conference");
    }

    public void create(Conference conference) throws ServiceException {
        runWithinTransaction(
                connection -> {
                    ConferenceDao.insert(connection, conference);
                    for (ConferenceTranslation translation : conference.getTranslations()) {
                        ConferenceTranslationDao.insert(connection, translation);
                    }
                },
                "Failed to save the conference into the database");
    }

    public void update(Conference conference) throws ServiceException {
        runWithinTransaction(
                connection -> {
                    ConferenceDao.update(connection, conference);
                    for (ConferenceTranslation translation : conference.getTranslations()) {
                        ConferenceTranslationDao.update(connection, translation);
                    }
                },
                "Failed to update the conference in the database");
    }

    public void delete(Conference conference) throws ServiceException {
        runWithinTransaction(connection -> ConferenceDao.delete(connection, conference),
                "Failed to delete the conference from the database");
    }
}
