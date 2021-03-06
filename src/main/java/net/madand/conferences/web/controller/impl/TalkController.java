package net.madand.conferences.web.controller.impl;

import net.madand.conferences.auth.Role;
import net.madand.conferences.entity.*;
import net.madand.conferences.l10n.Languages;
import net.madand.conferences.service.ServiceException;
import net.madand.conferences.service.impl.TalkService;
import net.madand.conferences.service.impl.TalkSpeakerRequestService;
import net.madand.conferences.web.controller.AbstractController;
import net.madand.conferences.web.controller.exception.HttpException;
import net.madand.conferences.web.controller.exception.HttpRedirectException;
import net.madand.conferences.web.scope.ContextScope;
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
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class TalkController extends AbstractController {
    private final TalkService talkService;

    {
        // Register the controller's actions.
        handlersMap.put(URLManager.URI_TALK_LIST, this::list);
        handlersMap.put(URLManager.URI_TALK_CREATE, this::create);
        handlersMap.put(URLManager.URI_TALK_EDIT, this::edit);
        handlersMap.put(URLManager.URI_TALK_DELETE, this::delete);
    }

    public TalkController(ServletContext servletContext) {
        super(servletContext);
        talkService = ContextScope.getServiceFactory(servletContext).getTalkService();
    }

    public void list(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, ServiceException, HttpException {
        final Language currentLanguage = SessionScope.getCurrentLanguage(request.getSession());
        final int id = Integer.parseInt(request.getParameter("id"));
        final Conference conference = serviceFactory.getConferenceService().findOne(id, currentLanguage)
                .orElseThrow(HttpException::new);

        URLManager.rememberUrlIfGET(request);

        request.setAttribute("conference", conference);
        request.setAttribute("talks", talkService.findAllTranslated(conference, currentLanguage));

        // If current user is a Speaker, load info on what talks had he requested being a talker.
        final Optional<User> userOptional = RequestScope.getUser(request);
        if (userOptional.map(User::getRole).map(Role::isSpeaker).orElse(false)) {
            TalkSpeakerRequestService talkSpeakerRequestService = serviceFactory.getTalkSpeakerRequestService();
            final List<TalkSpeakerRequest> talkSpeakerRequests = talkSpeakerRequestService.findAllTranslated(currentLanguage, userOptional.get());
            final Map<Integer, Integer> talkSpeakerRequestsMap = talkSpeakerRequests.stream()
                    .collect(Collectors.toMap(TalkSpeakerRequest::getTalkId, TalkSpeakerRequest::getId));
            request.setAttribute("talkSpeakerRequestsMap", talkSpeakerRequestsMap);
        }

        renderView("talk/list", request, response);
    }

    public void create(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, ServiceException, HttpRedirectException, HttpException {
        final Language currentLanguage = SessionScope.getCurrentLanguage(request.getSession());
        final int conferenceId = Integer.parseInt(request.getParameter("id"));

        Conference conference = serviceFactory.getConferenceService().findOne(conferenceId, currentLanguage)
                .orElseThrow(HttpException::new);
        Talk talk = Talk.makeInstanceWithTranslations(conference);

        request.setAttribute("conference", conference);
        request.setAttribute("talk", talk);
        request.setAttribute("speakersList", serviceFactory.getUserService().speakersList());

        if ("POST".equals(request.getMethod())) {
            String speakerIdStr = Optional.ofNullable(request.getParameter("speakerId")).orElse("");
            if (!speakerIdStr.isEmpty()) {
                final int speakerId = Integer.parseInt(speakerIdStr);
                talk.setSpeaker(serviceFactory.getUserService().findOneById(speakerId)
                        .orElseThrow(HttpException::new));
            } else {
                talk.setSpeaker(null);
            }
            talk.setStartTime(LocalTime.parse(request.getParameter("startTime")));
            talk.setDuration(Integer.parseInt(request.getParameter("duration")));

            for (TalkTranslation translation : talk.getTranslations()) {
                final Language lang = translation.getLanguage();
                translation.setName(request.getParameter(HtmlSupport.localizedParamName("name", lang)));
                translation.setDescription(request.getParameter(HtmlSupport.localizedParamName("description", lang)));
            }

            talkService.create(talk);
            setLocalizedFlashMessageSuccess("flashMessage.savedSuccessfully", request);
            redirect(URLManager.buildURL(URLManager.URI_TALK_LIST, "id=" + conference.getId(), request));
        }

        renderView("talk/create", request, response);
    }

    public void edit(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, ServiceException, HttpRedirectException, HttpException {
        final int id = Integer.parseInt(request.getParameter("id"));
        Talk talk = talkService.findOneWithTranslations(id, Languages.list()).orElseThrow(HttpException::new);

        request.setAttribute("talk", talk);
        request.setAttribute("speakersList", serviceFactory.getUserService().speakersList());

        if ("POST".equals(request.getMethod())) {
            String speakerIdStr = Optional.ofNullable(request.getParameter("speakerId")).orElse("");
            if (!speakerIdStr.isEmpty()) {
                final int speakerId = Integer.parseInt(speakerIdStr);
                talk.setSpeaker(serviceFactory.getUserService().findOneById(speakerId)
                        .orElseThrow(HttpException::new));
            } else {
                talk.setSpeaker(null);
            }
            talk.setStartTime(LocalTime.parse(request.getParameter("startTime")));
            talk.setDuration(Integer.parseInt(request.getParameter("duration")));

            for (TalkTranslation translation : talk.getTranslations()) {
                final Language lang = translation.getLanguage();
                translation.setName(request.getParameter(HtmlSupport.localizedParamName("name", lang)));
                translation.setDescription(request.getParameter(HtmlSupport.localizedParamName("description", lang)));
            }

            talkService.update(talk);

            final HttpSession session = request.getSession();
            setLocalizedFlashMessageSuccess("flashMessage.savedSuccessfully", request);
            redirect(URLManager.buildURL(URLManager.URI_TALK_LIST, "id=" + talk.getConference().getId(), request));
        }

        renderView("talk/edit", request, response);
    }

    public void delete(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, ServiceException, HttpRedirectException, HttpException {
        checkIsPOST(request);

        final int id = Integer.parseInt(request.getParameter("id"));
        Talk talk = talkService.findOne(id).orElseThrow(HttpException::new);

        talkService.delete(talk);

        setLocalizedFlashMessageInfo("flashMessage.deletedSuccessfully", request);
        redirect(URLManager.previousUrl(request));
    }
}
