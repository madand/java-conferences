package net.madand.conferences.service.impl;

import net.madand.conferences.db.dao.TalkDao;
import net.madand.conferences.db.dao.TalkSpeakerRequestDao;
import net.madand.conferences.entity.Language;
import net.madand.conferences.entity.TalkSpeakerRequest;
import net.madand.conferences.entity.User;
import net.madand.conferences.service.AbstractService;
import net.madand.conferences.service.ServiceException;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

public class TalkSpeakerRequestService extends AbstractService {
    public TalkSpeakerRequestService(DataSource dataSource) {
        super(dataSource);
    }

    public List<TalkSpeakerRequest> findAllTranslated(Language language) throws ServiceException {
        return callNoTransaction(
                connection -> TalkSpeakerRequestDao.findAll(connection, language),
                "Failed to fetch talk proposals"
        );
    }

    public List<TalkSpeakerRequest> findAllTranslated(Language language, User speaker) throws ServiceException {
        return callNoTransaction(
                connection -> TalkSpeakerRequestDao.findAll(connection, language, speaker),
                "Failed to fetch talk proposals"
        );
    }

    public Optional<TalkSpeakerRequest> findOne(int id) throws ServiceException {
        return callNoTransaction(
                connection -> TalkSpeakerRequestDao.findOne(connection, id),
                "Failed to fetch talk");
    }

    public void create(TalkSpeakerRequest talkSpeakerRequest) throws ServiceException {
        runWithinTransaction(
                connection -> TalkSpeakerRequestDao.insert(connection, talkSpeakerRequest),
                "Failed to save the talk proposal into the database");
    }

    public void delete(TalkSpeakerRequest talkSpeakerRequest) throws ServiceException {
        runWithinTransaction(connection -> TalkSpeakerRequestDao.delete(connection, talkSpeakerRequest),
                "Failed to delete the talk proposal from the database");
    }

    public void accept(TalkSpeakerRequest talkSpeakerRequest) throws ServiceException {
        runWithinTransaction(
                connection -> {
                    TalkDao.setSpeaker(connection, talkSpeakerRequest.getTalkId(), talkSpeakerRequest.getSpeaker().getId());
                    TalkSpeakerRequestDao.delete(connection, talkSpeakerRequest);
                },
                "Failed to accept the talk speaker request"
        );
    }
}
