package net.madand.conferences.service.impl;

import net.madand.conferences.db.dao.LanguageDao;
import net.madand.conferences.entity.Language;
import net.madand.conferences.service.AbstractService;
import net.madand.conferences.service.ServiceException;

import javax.sql.DataSource;
import java.util.List;

public class LanguageService extends AbstractService {
    public LanguageService(DataSource dataSource) {
        super(dataSource);
    }

    public List<Language> findAll() throws ServiceException {
        return callNoTransaction(LanguageDao::findAll, "Failed to fetch the languages");
    }
}
