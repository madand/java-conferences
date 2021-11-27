package net.madand.conferences.service;

import net.madand.conferences.service.impl.*;

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

    public TalkService getTalkService() {
        return new TalkService(dataSource);
    }

    public TalkProposalService getTalkProposalService() {
        return new TalkProposalService(dataSource);
    }

    public TalkSpeakerRequestService getTalkSpeakerRequestService() {
        return new TalkSpeakerRequestService(dataSource);
    }

    public TalkSpeakerProposalService getTalkSpeakerProposalService() {
        return new TalkSpeakerProposalService(dataSource);
    }
}
