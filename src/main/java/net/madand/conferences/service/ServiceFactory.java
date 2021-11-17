package net.madand.conferences.service;

import net.madand.conferences.service.impl.ConferenceService;
import net.madand.conferences.service.impl.LanguageService;
import net.madand.conferences.service.impl.UserService;

import javax.sql.DataSource;

public class ServiceFactory {
    private final DataSource dataSource;

    public ServiceFactory(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public LanguageService getLanguageService() {
        return new LanguageService(dataSource);
    }

    public ConferenceService getConferenceService() {
        return new ConferenceService(dataSource);
    }

    public UserService getUserService() {
        return new UserService(dataSource);
    }
}
