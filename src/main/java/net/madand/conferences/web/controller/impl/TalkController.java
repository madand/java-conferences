package net.madand.conferences.web.controller.impl;

import net.madand.conferences.entity.Conference;
import net.madand.conferences.entity.ConferenceTranslation;
import net.madand.conferences.entity.Language;
import net.madand.conferences.service.ServiceException;
import net.madand.conferences.service.impl.ConferenceService;
import net.madand.conferences.service.impl.TalkService;
import net.madand.conferences.web.controller.AbstractController;
import net.madand.conferences.web.scope.ContextScope;
import net.madand.conferences.web.scope.SessionScope;
import net.madand.conferences.web.util.HtmlSupport;
import net.madand.conferences.web.util.URLManager;
import org.apache.log4j.Logger;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class TalkController extends AbstractController {
    private static final Logger logger = Logger.getLogger(TalkController.class);

    private final TalkService service;
    private Map<String, Action> handlersMap;

    public TalkController(ServletContext servletContext) {
        super(servletContext);
        service = ContextScope.getServiceFactory(servletContext).getTalkService();
    }

    protected Map<String, Action> getHandlersMap() {
        if (handlersMap == null) {
            handlersMap = new HashMap<>();

            handlersMap.put(URLManager.URI_TALK_LIST, this::list);
//            handlersMap.put(URLManager.URI_TALK_CREATE, this::create);
        }

        return handlersMap;
    }

    public void list(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        final HttpSession session = request.getSession();
        final Language currentLanguage = SessionScope.getCurrentLanguage(session);
        ConferenceService conferenceService = ContextScope.getServiceFactory(servletContext).getConferenceService();

        try {
            Optional<Conference> conferenceOptional = conferenceService.findOne(
                    Integer.parseInt(request.getParameter("conference_id")),
                    currentLanguage
            );
            if (!conferenceOptional.isPresent()) {
                response.sendError(404);
                return;
            }

            request.setAttribute("conference", conferenceOptional.get());
            request.setAttribute("talks", service.findAllTranslated(
                    conferenceOptional.get(),
                    currentLanguage
            ));
        } catch (ServiceException e) {
            logger.warn(e);
            response.sendError(500, e.getMessage());
            return;
        }

        request.getRequestDispatcher("/WEB-INF/jsp/talk/list.jsp").forward(request, response);
    }
}
