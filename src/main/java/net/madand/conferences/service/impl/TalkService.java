package net.madand.conferences.service.impl;

import net.madand.conferences.db.dao.TalkDao;
import net.madand.conferences.db.dao.TalkTranslationDao;
import net.madand.conferences.entity.Conference;
import net.madand.conferences.entity.Talk;
import net.madand.conferences.entity.TalkTranslation;
import net.madand.conferences.entity.Language;
import net.madand.conferences.service.AbstractService;
import net.madand.conferences.service.ServiceException;

import javax.sql.DataSource;
import java.util.List;

public class TalkService extends AbstractService {
    public TalkService(DataSource dataSource) {
        super(dataSource);
    }

    public List<Talk> findAllTranslated(Conference conference, Language language) throws ServiceException {
        return callNoTransaction(
                connection -> TalkDao.findAll(connection, conference, language),
                "Error fetching talks"
        );
    }

    public void create(Talk talk, List<TalkTranslation> translations) throws ServiceException {
        runWithinTransaction(
                connection -> {
                    TalkDao.insert(connection, talk);
                    for (TalkTranslation translation : translations) {
                        TalkTranslationDao.insert(connection, translation);
                    }
                },
                "Failed to save the talk into the database"
        );
    }
}
