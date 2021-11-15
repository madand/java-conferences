package net.madand.conferences.web.controller;

import net.madand.conferences.entity.Conference;
import net.madand.conferences.entity.ConferenceTranslation;
import net.madand.conferences.entity.Language;
import net.madand.conferences.service.ConferenceService;
import net.madand.conferences.service.ServiceException;
import net.madand.conferences.web.util.ContextHelper;
import net.madand.conferences.web.util.SessionHelper;
import net.madand.conferences.web.util.URLManager;
import net.madand.conferences.web.util.WebFormUtil;

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

    public ConferenceController(ServletContext servletContext) {
        super(servletContext);
        service = new ConferenceService(ContextHelper.getDataSource(servletContext));
    }

    protected Map<String, Action> getHandlersMap() {
        Map<String, Action> map = new HashMap<>();

        map.put(URLManager.URI_CONFERENCE_LIST, this::list);
        map.put(URLManager.URI_CONFERENCE_CREATE, this::create);

        return map;
    }

    public void list(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        final HttpSession session = request.getSession();
        try {
            request.setAttribute("entities", service.findAllTranslated(SessionHelper.getCurrentLanguage(session)));
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
                ContextHelper.getLanguages(request.getServletContext()));
        request.setAttribute("translations", translations);

        if ("POST".equals(request.getMethod())) {
            String eventDateStr = request.getParameter("eventDate");
            LocalDate eventDate = LocalDate.parse(eventDateStr);
            conference.setEventDate(eventDate);

            for (ConferenceTranslation translation : translations) {
                final Language lang = translation.getLanguage();
                translation.setName(request.getParameter(WebFormUtil.localizedParamName("name", lang)));
                translation.setDescription(request.getParameter(WebFormUtil.localizedParamName("description", lang)));
                translation.setLocation(request.getParameter(WebFormUtil.localizedParamName("location", lang)));
            }

            try {
                service.create(conference, translations);
                // Redirect (PRG) only if the operation was successful.
                response.sendRedirect(URLManager.buildURL(request));
                return;
            } catch (ServiceException e) {
                response.sendError(500, e.getMessage());
            }
        }

        request.getRequestDispatcher("/WEB-INF/jsp/conference/form.jsp").forward(request, response);
    }

}
