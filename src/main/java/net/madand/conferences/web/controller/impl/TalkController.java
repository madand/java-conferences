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

public class TalkController extends AbstractController {
    private final TalkService service;

    {
        // Register the controller's actions.
        handlersMap.put(URLManager.URI_TALK_LIST, this::list);
//            handlersMap.put(URLManager.URI_TALK_CREATE, this::create);
    }

    public TalkController(ServletContext servletContext) {
        super(servletContext);
        service = ContextScope.getServiceFactory(servletContext).getTalkService();
    }

    public void list(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, ServiceException, HttpNotFoundException {
        final Language currentLanguage = SessionScope.getCurrentLanguage(request.getSession());
        final int id = Integer.parseInt(request.getParameter("id"));

        Conference conference = serviceFactory.getConferenceService().findOne(id, currentLanguage)
                .orElseThrow(HttpNotFoundException::new);

        request.setAttribute("conference", conference);
        request.setAttribute("talks", service.findAllTranslated(conference, currentLanguage));

        renderView("talk/list", request, response);
    }
}
