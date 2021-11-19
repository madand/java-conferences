package net.madand.conferences.web.controller.impl;

import net.madand.conferences.entity.Conference;
import net.madand.conferences.entity.Language;
import net.madand.conferences.service.ServiceException;
import net.madand.conferences.service.impl.ConferenceService;
import net.madand.conferences.service.impl.TalkService;
import net.madand.conferences.web.controller.AbstractController;
import net.madand.conferences.web.controller.exception.HttpNotFoundException;
import net.madand.conferences.web.scope.ContextScope;
import net.madand.conferences.web.scope.SessionScope;
import net.madand.conferences.web.util.URLManager;
import org.apache.log4j.Logger;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class TalkController extends AbstractController {
    private static final Logger logger = Logger.getLogger(TalkController.class);

    private final TalkService service;

    public TalkController(ServletContext servletContext) {
        super(servletContext);
        service = ContextScope.getServiceFactory(servletContext).getTalkService();

        handlersMap.put(URLManager.URI_TALK_LIST, this::list);
//            handlersMap.put(URLManager.URI_TALK_CREATE, this::create);
    }

    public void list(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, ServiceException, HttpNotFoundException {
        final HttpSession session = request.getSession();
        final Language currentLanguage = SessionScope.getCurrentLanguage(session);
        ConferenceService conferenceService = ContextScope.getServiceFactory(servletContext).getConferenceService();

        final int id = Integer.parseInt(request.getParameter("conference_id"));
        Conference conference = conferenceService.findOne(id, currentLanguage)
                .orElseThrow(HttpNotFoundException::new);

        request.setAttribute("conference", conference);
        request.setAttribute("talks", service.findAllTranslated(conference, currentLanguage));

        renderView("talk/list", request, response);
    }
}
