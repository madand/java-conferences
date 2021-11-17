package net.madand.conferences.web.controller.impl;

import net.madand.conferences.entity.Conference;
import net.madand.conferences.entity.ConferenceTranslation;
import net.madand.conferences.entity.Language;
import net.madand.conferences.service.impl.ConferenceService;
import net.madand.conferences.service.ServiceException;
import net.madand.conferences.web.controller.AbstractController;
import net.madand.conferences.web.scope.ContextScope;
import net.madand.conferences.web.scope.SessionScope;
import net.madand.conferences.web.util.URLManager;
import net.madand.conferences.web.util.HtmlSupport;

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

public class ConferenceController extends AbstractController {
    private ConferenceService service;
    private Map<String, Action> handlersMap;

    public ConferenceController(ServletContext servletContext) {
        super(servletContext);
        service = ContextScope.getServiceFactory(servletContext).getConferenceService();
    }

    protected Map<String, Action> getHandlersMap() {
        if (handlersMap == null) {
            handlersMap = new HashMap<>();

            handlersMap.put(URLManager.URI_CONFERENCE_LIST, this::list);
            handlersMap.put(URLManager.URI_CONFERENCE_CREATE, this::create);
        }

        return handlersMap;
    }

    public void list(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        final HttpSession session = request.getSession();
        try {
            request.setAttribute("entities", service.findAllTranslated(SessionScope.getCurrentLanguage(session)));
        } catch (ServiceException e) {
            response.sendError(500, e.getMessage());
            return;
        }

        request.getRequestDispatcher("/WEB-INF/jsp/conference/list.jsp").forward(request, response);
    }

    public void create(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        Conference conference = new Conference();
        request.setAttribute("entity", conference);
        List<ConferenceTranslation> translations = conference.makeTranslations(
                ContextScope.getLanguages(request.getServletContext()));
        request.setAttribute("translations", translations);

        if ("POST".equals(request.getMethod())) {
            String eventDateStr = request.getParameter("eventDate");
            LocalDate eventDate = LocalDate.parse(eventDateStr);
            conference.setEventDate(eventDate);

            for (ConferenceTranslation translation : translations) {
                final Language lang = translation.getLanguage();
                translation.setName(request.getParameter(HtmlSupport.localizedParamName("name", lang)));
                translation.setDescription(request.getParameter(HtmlSupport.localizedParamName("description", lang)));
                translation.setLocation(request.getParameter(HtmlSupport.localizedParamName("location", lang)));
            }

            try {
                service.create(conference, translations);
                // Redirect (PRG) only if the operation was successful.
                response.sendRedirect(response.encodeRedirectURL(URLManager.buildURL(URLManager.URI_CONFERENCE_LIST, request)));
                return;
            } catch (ServiceException e) {
                response.sendError(500, e.getMessage());
                return;
            }
        }

        request.getRequestDispatcher("/WEB-INF/jsp/conference/form.jsp").forward(request, response);
    }

}
