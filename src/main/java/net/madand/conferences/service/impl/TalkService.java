package net.madand.conferences.service.impl;

import net.madand.conferences.db.dao.TalkDao;
import net.madand.conferences.db.dao.TalkTranslationDao;
import net.madand.conferences.entity.Conference;
import net.madand.conferences.entity.Language;
import net.madand.conferences.entity.Talk;
import net.madand.conferences.entity.TalkTranslation;
import net.madand.conferences.service.AbstractService;
import net.madand.conferences.service.ServiceException;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

public class TalkService extends AbstractService {
    public TalkService(DataSource dataSource) {
        super(dataSource);
    }

    public List<Talk> findAllTranslated(Conference conference, Language language) throws ServiceException {
        return callNoTransaction(
                connection -> TalkDao.findAll(connection, conference, language),
                "Failed to fetch talks"
        );
    }

    public Optional<Talk> findOneWithTranslations(int id, List<Language> languages) throws ServiceException {
        return callNoTransaction(
                connection -> {
                    Optional<Talk> talkOptional = TalkDao.findOne(connection, id);
                    if (talkOptional.isPresent()) {
                        final Talk talk = talkOptional.get();
                        for (Language language : languages) {
                            TalkTranslationDao.findOne(connection, talk, language)
                                    .ifPresent(talk::addTranslation);
                        }
                    }

                    return talkOptional;
                },
                "Failed to fetch talks");
    }

    public Optional<Talk> findOne(int id) throws ServiceException {
        return callNoTransaction(
                connection -> TalkDao.findOne(connection, id),
                "Failed to fetch talk");
    }

    public Optional<Talk> findOne(int id, Language language) throws ServiceException {
        return callNoTransaction(
                connection -> TalkDao.findOne(connection, id, language),
                "Failed to fetch talk");
    }

    public void create(Talk talk) throws ServiceException {
        runWithinTransaction(
                connection -> {
                    TalkDao.insert(connection, talk);
                    for (TalkTranslation translation : talk.getTranslations()) {
                        TalkTranslationDao.insert(connection, translation);
                    }
                },
                "Failed to save the talk into the database");
    }

    public void update(Talk talk) throws ServiceException {
        runWithinTransaction(
                connection -> {
                    TalkDao.update(connection, talk);
                    for (TalkTranslation translation : talk.getTranslations()) {
                        TalkTranslationDao.update(connection, translation);
                    }
                },
                "Failed to update the talk in the database");
    }

    public void delete(Talk talk) throws ServiceException {
        runWithinTransaction(connection -> TalkDao.delete(connection, talk),
                "Failed to delete the talk from the database");
    }
}
