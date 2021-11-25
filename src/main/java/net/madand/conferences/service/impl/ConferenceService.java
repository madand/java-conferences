package net.madand.conferences.service.impl;

import net.madand.conferences.db.dao.ConferenceAttendeeDao;
import net.madand.conferences.db.dao.ConferenceDao;
import net.madand.conferences.db.dao.ConferenceTranslationDao;
import net.madand.conferences.entity.Conference;
import net.madand.conferences.entity.ConferenceTranslation;
import net.madand.conferences.entity.Language;
import net.madand.conferences.entity.User;
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
                "Failed to fetch conferences");
    }

    public List<Conference> findAllTranslatedWithAttendee(Language language, Optional<User> user) throws ServiceException {
        return callNoTransaction(
                connection -> ConferenceDao.findAll(connection, language, user),
                "Failed to fetch conferences");
    }

    public Optional<Conference> findOne(int id, Language language) throws ServiceException {
        return callNoTransaction(
                connection -> ConferenceDao.findOne(connection, id, language),
                "Failed to fetch conference");
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
                "Failed to fetch conference");
    }

    public Optional<Conference> findOne(int id) throws ServiceException {
        return callNoTransaction(
                connection -> ConferenceDao.findOne(connection, id),
                "Failed to fetch a conference");
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

    public void addAttendee(Conference conference, User user) throws ServiceException {
        runWithinTransaction(connection -> ConferenceAttendeeDao.insert(connection, conference, user),
                "Failed to add the attendee into the database");
    }

    public void removeAttendee(Conference conference, User user) throws ServiceException {
        runWithinTransaction(connection -> ConferenceAttendeeDao.delete(connection, conference, user),
                "Failed to delete the attendee from the database");
    }
}
