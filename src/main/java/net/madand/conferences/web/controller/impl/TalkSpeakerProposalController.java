package net.madand.conferences.web.controller.impl;

import net.madand.conferences.entity.Language;
import net.madand.conferences.entity.Talk;
import net.madand.conferences.entity.TalkSpeakerProposal;
import net.madand.conferences.entity.User;
import net.madand.conferences.service.ServiceException;
import net.madand.conferences.service.impl.TalkSpeakerProposalService;
import net.madand.conferences.web.controller.AbstractController;
import net.madand.conferences.web.controller.exception.HttpException;
import net.madand.conferences.web.controller.exception.HttpRedirectException;
import net.madand.conferences.web.scope.RequestScope;
import net.madand.conferences.web.scope.SessionScope;
import net.madand.conferences.web.util.URLManager;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class TalkSpeakerProposalController extends AbstractController {
    private final TalkSpeakerProposalService talkSpeakerProposalService;

    {
        // Register the controller's actions.
        handlersMap.put(URLManager.URI_TALK_SPEAKER_PROPOSAL_LIST_MODER, this::listForModerator);
        handlersMap.put(URLManager.URI_TALK_SPEAKER_PROPOSAL_LIST_SPEAKER, this::listForSpeaker);
        handlersMap.put(URLManager.URI_TALK_SPEAKER_PROPOSAL_CREATE, this::create);
        handlersMap.put(URLManager.URI_TALK_SPEAKER_PROPOSAL_ACCEPT, this::accept);
        handlersMap.put(URLManager.URI_TALK_SPEAKER_PROPOSAL_DELETE, this::delete);
    }

    public TalkSpeakerProposalController(ServletContext servletContext) {
        super(servletContext);
        talkSpeakerProposalService = serviceFactory.getTalkSpeakerProposalService();
    }

    public void listForModerator(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, ServiceException, HttpException {
        final Language currentLanguage = SessionScope.getCurrentLanguage(request.getSession());

        request.setAttribute("talkSpeakerProposals", talkSpeakerProposalService.findAllTranslated(currentLanguage));

        URLManager.rememberUrlIfGET(request);
        renderView("talkSpeakerProposal/listForModerator", request, response);
    }

    public void listForSpeaker(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, ServiceException, HttpException {
        final Language currentLanguage = SessionScope.getCurrentLanguage(request.getSession());
        User user = RequestScope.getUser(request).orElseThrow(HttpException::forbidden);
        if (!user.getRole().isSpeaker()) {
            throw HttpException.forbidden();
        }

        request.setAttribute("talkSpeakerProposals", talkSpeakerProposalService.findAllTranslated(currentLanguage, user));

        URLManager.rememberUrlIfGET(request);
        renderView("talkSpeakerProposal/listForSpeaker", request, response);
    }

    public void create(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, ServiceException, HttpRedirectException, HttpException {
        User user = RequestScope.getUser(request).orElseThrow(HttpException::new);
        if (!user.getRole().isModerator()) {
            throw HttpException.forbidden();
        }

        final Language currentLanguage = SessionScope.getCurrentLanguage(request.getSession());
        final int talkId = Integer.parseInt(request.getParameter("id"));
        Talk talk = serviceFactory.getTalkService().findOne(talkId, currentLanguage).orElseThrow(HttpException::new);
        final List<User> speakersList = serviceFactory.getUserService().unproposedSpeakersForTalk(talk);
        request.setAttribute("talk", talk);
        request.setAttribute("speakersList", speakersList);

        if ("POST".equals(request.getMethod())) {
            final int speakerId = Integer.parseInt(request.getParameter("speakerId"));
            final User speaker = serviceFactory.getUserService().findOneById(speakerId).orElseThrow(HttpException::new);
            TalkSpeakerProposal talkSpeakerProposal = TalkSpeakerProposal.makeInstance(talk, speaker, user);
            talkSpeakerProposalService.create(talkSpeakerProposal);

            setLocalizedFlashMessageSuccess("flashMessage.createdSuccessfully", request);
            redirect(URLManager.previousUrl(request));
        }

        renderView("talkSpeakerProposal/create", request, response);
    }

    public void delete(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, ServiceException, HttpRedirectException, HttpException {
        checkIsPOST(request);

        final int id = Integer.parseInt(request.getParameter("id"));
        TalkSpeakerProposal talkSpeakerProposal = talkSpeakerProposalService.findOne(id).orElseThrow(HttpException::new);

        // Only a moderator or a speaker, who created the proposal, can delete it.
        User user = RequestScope.getUser(request).orElseThrow(HttpException::forbidden);
        if (!user.getRole().isModerator() && !user.equals(talkSpeakerProposal.getSpeaker())) {
            throw HttpException.forbidden();
        }

        talkSpeakerProposalService.delete(talkSpeakerProposal);

        setLocalizedFlashMessageInfo("flashMessage.cancelSuccessfully", request);
        redirect(URLManager.previousUrl(request));
    }

    public void accept(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, ServiceException, HttpRedirectException, HttpException {
        checkIsPOST(request);

        final int id = Integer.parseInt(request.getParameter("id"));
        TalkSpeakerProposal talkSpeakerProposal = talkSpeakerProposalService.findOne(id).orElseThrow(HttpException::new);

        // Only a speaker can accept proposals.
        User user = RequestScope.getUser(request).orElseThrow(HttpException::forbidden);
        if (!user.getRole().isSpeaker()) {
            throw HttpException.forbidden();
        }

        talkSpeakerProposalService.accept(talkSpeakerProposal);

        setLocalizedFlashMessageSuccess("flashMessage.acceptedSuccessfully", request);
        redirect(URLManager.previousUrl(request));
    }
}
