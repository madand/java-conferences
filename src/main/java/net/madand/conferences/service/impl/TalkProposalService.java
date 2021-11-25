package net.madand.conferences.service.impl;

import net.madand.conferences.db.dao.TalkProposalDao;
import net.madand.conferences.db.dao.TalkProposalTranslationDao;
import net.madand.conferences.entity.*;
import net.madand.conferences.service.AbstractService;
import net.madand.conferences.service.ServiceException;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

public class TalkProposalService extends AbstractService {
    public TalkProposalService(DataSource dataSource) {
        super(dataSource);
    }

    public List<TalkProposal> findAllTranslated(Language language) throws ServiceException {
        return callNoTransaction(
                connection -> TalkProposalDao.findAll(connection, language),
                "Failed to fetch talk proposals"
        );
    }

    public List<TalkProposal> findAllTranslated(User speaker, Language language) throws ServiceException {
        return callNoTransaction(
                connection -> TalkProposalDao.findAll(connection, language),
                "Failed to fetch talk proposals"
        );
    }

    public Optional<TalkProposal> findOneWithTranslations(int id, List<Language> languages) throws ServiceException {
        return callNoTransaction(
                connection -> {
                    Optional<TalkProposal> talkOptional = TalkProposalDao.findOne(connection, id);
                    if (talkOptional.isPresent()) {
                        final TalkProposal talk = talkOptional.get();
                        for (Language language : languages) {
                            TalkProposalTranslationDao.findOne(connection, talk, language)
                                    .ifPresent(talk::addTranslation);
                        }
                    }

                    return talkOptional;
                },
                "Failed to fetch talk proposals");
    }

    public Optional<TalkProposal> findOne(int id) throws ServiceException {
        return callNoTransaction(
                connection -> TalkProposalDao.findOne(connection, id),
                "Failed to fetch talk");
    }

    public void create(TalkProposal talk) throws ServiceException {
        runWithinTransaction(
                connection -> {
                    TalkProposalDao.insert(connection, talk);
                    for (TalkProposalTranslation translation : talk.getTranslations()) {
                        TalkProposalTranslationDao.insert(connection, translation);
                    }
                },
                "Failed to save the talk proposal into the database");
    }

    public void update(TalkProposal talk) throws ServiceException {
        runWithinTransaction(
                connection -> {
                    TalkProposalDao.update(connection, talk);
                    for (TalkProposalTranslation translation : talk.getTranslations()) {
                        TalkProposalTranslationDao.update(connection, translation);
                    }
                },
                "Failed to update the talk proposal in the database");
    }

    public void delete(TalkProposal talk) throws ServiceException {
        runWithinTransaction(connection -> TalkProposalDao.delete(connection, talk),
                "Failed to delete the talk proposal from the database");
    }
}
