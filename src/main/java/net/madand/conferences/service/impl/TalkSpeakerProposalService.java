package net.madand.conferences.service.impl;

import net.madand.conferences.db.dao.TalkDao;
import net.madand.conferences.db.dao.TalkSpeakerProposalDao;
import net.madand.conferences.entity.Language;
import net.madand.conferences.entity.TalkSpeakerProposal;
import net.madand.conferences.entity.User;
import net.madand.conferences.service.AbstractService;
import net.madand.conferences.service.ServiceException;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

public class TalkSpeakerProposalService extends AbstractService {
    public TalkSpeakerProposalService(DataSource dataSource) {
        super(dataSource);
    }

    public List<TalkSpeakerProposal> findAllTranslated(Language language) throws ServiceException {
        return callNoTransaction(
                connection -> TalkSpeakerProposalDao.findAll(connection, language),
                "Failed to fetch the talk speaker proposals"
        );
    }

    public List<TalkSpeakerProposal> findAllTranslated(Language language, User speaker) throws ServiceException {
        return callNoTransaction(
                connection -> TalkSpeakerProposalDao.findAll(connection, language, speaker),
                "Failed to fetch the talk speaker proposals"
        );
    }

    public Optional<TalkSpeakerProposal> findOne(int id) throws ServiceException {
        return callNoTransaction(
                connection -> TalkSpeakerProposalDao.findOne(connection, id),
                "Failed to fetch the talk speaker proposal");
    }

    public void create(TalkSpeakerProposal talkSpeakerProposal) throws ServiceException {
        runWithinTransaction(
                connection -> TalkSpeakerProposalDao.insert(connection, talkSpeakerProposal),
                "Failed to save the talk speaker proposal into the database");
    }

    public void delete(TalkSpeakerProposal talkSpeakerProposal) throws ServiceException {
        runWithinTransaction(connection -> TalkSpeakerProposalDao.delete(connection, talkSpeakerProposal),
                "Failed to delete the talk speaker proposal from the database");
    }

    public void accept(TalkSpeakerProposal talkSpeakerProposal) throws ServiceException {
        runWithinTransaction(
                connection -> {
                    TalkDao.setSpeaker(connection, talkSpeakerProposal.getTalkId(), talkSpeakerProposal.getSpeaker().getId());
                    TalkSpeakerProposalDao.delete(connection, talkSpeakerProposal);
                },
                "Failed to accept the talk speaker proposal"
        );
    }
}
