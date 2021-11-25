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

public class TalkProposalController extends AbstractController {
    private final TalkProposalService talkProposalService;

    {
        // Register the controller's actions.
        handlersMap.put(URLManager.URI_TALK_PROPOSAL_LIST_MODER, this::listForModerator);
        handlersMap.put(URLManager.URI_TALK_PROPOSAL_LIST_SPEAKER, this::listForSpeaker);
        handlersMap.put(URLManager.URI_TALK_PROPOSAL_CREATE, this::create);
//        handlersMap.put(URLManager.URI_TALK_EDIT, this::edit);
//        handlersMap.put(URLManager.URI_TALK_DELETE, this::delete);
    }

    public TalkProposalController(ServletContext servletContext) {
        super(servletContext);
        talkProposalService = serviceFactory.getTalkProposalService();
    }

    public void listForModerator(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, ServiceException, HttpException {
        final Language currentLanguage = SessionScope.getCurrentLanguage(request.getSession());

        request.setAttribute("proposals", talkProposalService.findAllTranslated(currentLanguage));

        renderView("talkProposal/listForModerator", request, response);
    }

    public void listForSpeaker(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, ServiceException, HttpException {
        final Language currentLanguage = SessionScope.getCurrentLanguage(request.getSession());

        request.setAttribute("proposals", talkProposalService.findAllTranslated(currentLanguage));

        renderView("talkProposal/listForSpeaker", request, response);
    }

    public void create(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, ServiceException, HttpRedirectException, HttpException {
        final Language currentLanguage = SessionScope.getCurrentLanguage(request.getSession());
        final int conferenceId = Integer.parseInt(request.getParameter("id"));

        User user = RequestScope.getUser(request)
                .orElseThrow(HttpException::new);
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
            SessionScope.setFlashMessageSuccess(request.getSession(), "Successfully created a talk proposal");
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
                talkProposal.setSpeaker(serviceFactory.getUserService().findById(speakerId)
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
            SessionScope.setFlashMessageSuccess(session, "Saved successfully");
            redirect(URLManager.buildURL(URLManager.URI_TALK_LIST, "id=" + talkProposal.getConference().getId(), request));
        }

        renderView("talkProposal/edit", request, response);
    }

    public void delete(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, ServiceException, HttpRedirectException, HttpException {
        ensureIsPost(request);

        final int id = Integer.parseInt(request.getParameter("id"));
        TalkProposal talkProposal = talkProposalService.findOne(id)
                .orElseThrow(HttpException::new);

        talkProposalService.delete(talkProposal);

        final HttpSession session = request.getSession();
        SessionScope.setFlashMessageSuccess(session, "Deleted successfully");
        redirect(SessionScope.getPreviousUrl(request.getSession(), URLManager.homePage(request)));
    }
}
