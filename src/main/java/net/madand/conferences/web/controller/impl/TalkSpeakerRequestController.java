package net.madand.conferences.web.controller.impl;

import net.madand.conferences.entity.Language;
import net.madand.conferences.entity.Talk;
import net.madand.conferences.entity.TalkSpeakerRequest;
import net.madand.conferences.entity.User;
import net.madand.conferences.service.ServiceException;
import net.madand.conferences.service.impl.TalkSpeakerRequestService;
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
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class TalkSpeakerRequestController extends AbstractController {
    private final TalkSpeakerRequestService talkSpeakerRequestService;

    {
        // Register the controller's actions.
        handlersMap.put(URLManager.URI_TALK_SPEAKER_REQUEST_LIST_MODER, this::listForModerator);
        handlersMap.put(URLManager.URI_TALK_SPEAKER_REQUEST_LIST_SPEAKER, this::listForSpeaker);
        handlersMap.put(URLManager.URI_TALK_SPEAKER_REQUEST_CREATE, this::create);
        handlersMap.put(URLManager.URI_TALK_SPEAKER_REQUEST_ACCEPT, this::accept);
        handlersMap.put(URLManager.URI_TALK_SPEAKER_REQUEST_DELETE, this::delete);
    }

    public TalkSpeakerRequestController(ServletContext servletContext) {
        super(servletContext);
        talkSpeakerRequestService = serviceFactory.getTalkSpeakerRequestService();
    }

    public void listForModerator(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, ServiceException, HttpException {
        final Language currentLanguage = SessionScope.getCurrentLanguage(request.getSession());

        request.setAttribute("talkSpeakerRequests", talkSpeakerRequestService.findAllTranslated(currentLanguage));

        URLManager.rememberUrlIfGET(request);
        renderView("talkSpeakerRequest/listForModerator", request, response);
    }

    public void listForSpeaker(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, ServiceException, HttpException {
        final Language currentLanguage = SessionScope.getCurrentLanguage(request.getSession());
        User user = RequestScope.getUser(request).orElseThrow(HttpException::forbidden);
        if (!user.getRole().isSpeaker()) {
            throw HttpException.forbidden();
        }

        request.setAttribute("talkSpeakerRequests", talkSpeakerRequestService.findAllTranslated(currentLanguage, user));

        URLManager.rememberUrlIfGET(request);
        renderView("talkSpeakerRequest/listForSpeaker", request, response);
    }

    public void create(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, ServiceException, HttpRedirectException, HttpException {
        checkIsPOST(request);

        final Language currentLanguage = SessionScope.getCurrentLanguage(request.getSession());
        final int id = Integer.parseInt(request.getParameter("id"));

        User user = RequestScope.getUser(request).orElseThrow(HttpException::new);
        if (!user.getRole().isSpeaker()) {
            throw new HttpException(HttpServletResponse.SC_FORBIDDEN);
        }

        Talk talk = serviceFactory.getTalkService().findOne(id).orElseThrow(HttpException::new);
        TalkSpeakerRequest talkSpeakerRequest = TalkSpeakerRequest.makeInstance(talk, user);
        talkSpeakerRequestService.create(talkSpeakerRequest);

        setLocalizedFlashMessageSuccess("flashMessage.createdSuccessfully", request);
        redirect(URLManager.previousUrl(request));
    }

    public void delete(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, ServiceException, HttpRedirectException, HttpException {
        checkIsPOST(request);

        final int id = Integer.parseInt(request.getParameter("id"));
        TalkSpeakerRequest talkSpeakerRequest = talkSpeakerRequestService.findOne(id).orElseThrow(HttpException::new);

        // Only a moderator or a speaker, who created the proposal, can delete it.
        User user = RequestScope.getUser(request).orElseThrow(HttpException::forbidden);
        if (!user.getRole().isModerator() && !user.equals(talkSpeakerRequest.getSpeaker())) {
            throw HttpException.forbidden();
        }

        talkSpeakerRequestService.delete(talkSpeakerRequest);

        setLocalizedFlashMessageInfo("flashMessage.cancelSuccessfully", request);
        redirect(URLManager.previousUrl(request));
    }

    public void accept(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, ServiceException, HttpRedirectException, HttpException {
        checkIsPOST(request);

        final int id = Integer.parseInt(request.getParameter("id"));
        TalkSpeakerRequest talkSpeakerRequest = talkSpeakerRequestService.findOne(id).orElseThrow(HttpException::new);

        // Only a moderator can accept proposals.
        User user = RequestScope.getUser(request).orElseThrow(HttpException::forbidden);
        if (!user.getRole().isModerator()) {
            throw HttpException.forbidden();
        }

        talkSpeakerRequestService.accept(talkSpeakerRequest);

        setLocalizedFlashMessageSuccess("flashMessage.acceptedSuccessfully", request);
        redirect(URLManager.previousUrl(request));
    }
}
