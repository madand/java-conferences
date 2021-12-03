package net.madand.conferences.web.controller.impl;

import net.madand.conferences.entity.*;
import net.madand.conferences.l10n.Languages;
import net.madand.conferences.service.ServiceException;
import net.madand.conferences.service.impl.TalkProposalService;
import net.madand.conferences.web.controller.AbstractController;
import net.madand.conferences.web.controller.exception.HttpException;
import net.madand.conferences.web.controller.exception.HttpRedirectException;
import net.madand.conferences.web.scope.RequestScope;
import net.madand.conferences.web.scope.SessionScope;
import net.madand.conferences.web.util.HtmlSupport;
import net.madand.conferences.web.util.URLManager;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalTime;
import java.util.List;

public class TalkProposalController extends AbstractController {
    private final TalkProposalService talkProposalService;

    {
        // Register the controller's actions.
        handlersMap.put(URLManager.URI_TALK_PROPOSAL_LIST_MODER, this::listForModerator);
        handlersMap.put(URLManager.URI_TALK_PROPOSAL_LIST_SPEAKER, this::listForSpeaker);
        handlersMap.put(URLManager.URI_TALK_PROPOSAL_CREATE, this::create);
        handlersMap.put(URLManager.URI_TALK_PROPOSAL_REVIEW, this::review);
        handlersMap.put(URLManager.URI_TALK_PROPOSAL_DELETE, this::delete);
    }

    public TalkProposalController(ServletContext servletContext) {
        super(servletContext);
        talkProposalService = serviceFactory.getTalkProposalService();
    }

    public void listForModerator(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, ServiceException, HttpException {
        final Language currentLanguage = SessionScope.getCurrentLanguage(request.getSession());

        request.setAttribute("proposals", talkProposalService.findAllTranslated(currentLanguage));

        URLManager.rememberUrlIfGET(request);
        renderView("talkProposal/listForModerator", request, response);
    }

    public void listForSpeaker(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, ServiceException, HttpException {
        final Language currentLanguage = SessionScope.getCurrentLanguage(request.getSession());
        User user = RequestScope.getUser(request).orElseThrow(HttpException::forbidden);
        if (!user.getRole().isSpeaker()) {
            throw HttpException.forbidden();
        }

        request.setAttribute("proposals", talkProposalService.findAllTranslated(currentLanguage, user));

        URLManager.rememberUrlIfGET(request);
        renderView("talkProposal/listForSpeaker", request, response);
    }

    public void create(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, ServiceException, HttpRedirectException, HttpException {
        final Language currentLanguage = SessionScope.getCurrentLanguage(request.getSession());
        final int conferenceId = Integer.parseInt(request.getParameter("id"));

        User user = RequestScope.getUser(request).orElseThrow(HttpException::new);
        if (!user.getRole().isSpeaker()) {
            throw new HttpException(HttpServletResponse.SC_FORBIDDEN);
        }

        Conference conference = serviceFactory.getConferenceService().findOne(conferenceId, currentLanguage)
                .orElseThrow(HttpException::new);
        request.setAttribute("conference", conference);

        TalkProposal talkProposal = TalkProposal.makeInstanceWithTranslations(conference);
        talkProposal.setSpeaker(user);

        request.setAttribute("talkProposal", talkProposal);

        if ("POST".equals(request.getMethod())) {
            talkProposal.setDuration(Integer.parseInt(request.getParameter("duration")));

            for (TalkProposalTranslation translation : talkProposal.getTranslations()) {
                final Language lang = translation.getLanguage();
                translation.setName(request.getParameter(HtmlSupport.localizedParamName("name", lang)));
                translation.setDescription(request.getParameter(HtmlSupport.localizedParamName("description", lang)));
            }

            talkProposalService.create(talkProposal);

            setLocalizedFlashMessageSuccess("flashMessage.talkProposal.created", request);
            redirect(URLManager.buildURL(URLManager.URI_TALK_LIST, "id=" + conference.getId(), request));
        }

        renderView("talkProposal/create", request, response);
    }

    public void edit(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, ServiceException, HttpRedirectException, HttpException {
        final int id = Integer.parseInt(request.getParameter("id"));

        TalkProposal talkProposal = talkProposalService.findOneWithTranslations(id, Languages.list())
                .orElseThrow(HttpException::new);
        request.setAttribute("conference", talkProposal.getConference());
        request.setAttribute("talkProposal", talkProposal);

        if ("POST".equals(request.getMethod())) {
            String speakerIdStr = request.getParameter("speakerId");
            if (speakerIdStr != null && !speakerIdStr.isEmpty()) {
                final int speakerId = Integer.parseInt(speakerIdStr);
                talkProposal.setSpeaker(serviceFactory.getUserService().findOneById(speakerId)
                        .orElseThrow(HttpException::new));
            }
            talkProposal.setDuration(Integer.parseInt(request.getParameter("duration")));

            for (TalkProposalTranslation translation : talkProposal.getTranslations()) {
                final Language lang = translation.getLanguage();
                translation.setName(request.getParameter(HtmlSupport.localizedParamName("name", lang)));
                translation.setDescription(request.getParameter(HtmlSupport.localizedParamName("description", lang)));
            }

            talkProposalService.update(talkProposal);

            final HttpSession session = request.getSession();
            setLocalizedFlashMessageSuccess("flashMessage.savedSuccessfully", request);
            redirect(URLManager.buildURL(URLManager.URI_TALK_LIST, "id=" + talkProposal.getConference().getId(), request));
        }

        renderView("talkProposal/edit", request, response);
    }

    public void delete(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, ServiceException, HttpRedirectException, HttpException {
        checkIsPOST(request);

        final int id = Integer.parseInt(request.getParameter("id"));
        TalkProposal talkProposal = talkProposalService.findOne(id)
                .orElseThrow(HttpException::new);

        User user = RequestScope.getUser(request).orElseThrow(HttpException::forbidden);
        // Only a moderator or a speaker, who created the proposal, can delete it.
        if (!user.getRole().isModerator() && !user.equals(talkProposal.getSpeaker())) {
            throw HttpException.forbidden();
        }

        talkProposalService.delete(talkProposal);

        setLocalizedFlashMessageInfo("flashMessage.deletedSuccessfully", request);
        redirect(URLManager.previousUrl(request));
    }

    public void review(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, ServiceException, HttpRedirectException, HttpException {
        final Language currentLanguage = SessionScope.getCurrentLanguage(request.getSession());
        final int id = Integer.parseInt(request.getParameter("id"));

        final TalkProposal talkProposal = talkProposalService.findOneWithTranslations(id, Languages.list())
                .orElseThrow(HttpException::new);
        final Conference conference = serviceFactory.getConferenceService()
                .findOne(talkProposal.getConference().getId(), currentLanguage).get();
        final List<Talk> existingTalks = serviceFactory.getTalkService().findAllTranslated(conference, currentLanguage);

        request.setAttribute("conference", conference);
        request.setAttribute("existingTalks", existingTalks);
        request.setAttribute("talk", talkProposal);

        if ("POST".equals(request.getMethod())) {
            talkProposal.setStartTime(LocalTime.parse(request.getParameter("startTime")));
            talkProposal.setDuration(Integer.parseInt(request.getParameter("duration")));

            for (TalkProposalTranslation translation : talkProposal.getTranslations()) {
                final Language lang = translation.getLanguage();
                translation.setName(request.getParameter(HtmlSupport.localizedParamName("name", lang)));
                translation.setDescription(request.getParameter(HtmlSupport.localizedParamName("description", lang)));
            }

            talkProposalService.acceptProposal(talkProposal);

            setLocalizedFlashMessageSuccess("flashMessage.acceptedSuccessfully", request);
            redirect(URLManager.buildURL(URLManager.URI_TALK_PROPOSAL_LIST_MODER, null, request));
        }

        renderView("talkProposal/review", request, response);
    }
}
