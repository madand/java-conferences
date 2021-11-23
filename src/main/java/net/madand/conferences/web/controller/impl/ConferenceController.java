package net.madand.conferences.web.controller.impl;

import net.madand.conferences.entity.Conference;
import net.madand.conferences.entity.ConferenceTranslation;
import net.madand.conferences.entity.Language;
import net.madand.conferences.l10n.Languages;
import net.madand.conferences.service.ServiceException;
import net.madand.conferences.service.impl.ConferenceService;
import net.madand.conferences.web.controller.AbstractController;
import net.madand.conferences.web.controller.exception.HttpNotFoundException;
import net.madand.conferences.web.controller.exception.HttpRedirectException;
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

public class ConferenceController extends AbstractController {
    private static final Logger logger = Logger.getLogger(ConferenceController.class);

    private final ConferenceService conferenceService;

    {
        // Register the controller's actions.
        handlersMap.put(URLManager.URI_CONFERENCE_LIST, this::list);
        handlersMap.put(URLManager.URI_CONFERENCE_CREATE, this::create);
        handlersMap.put(URLManager.URI_CONFERENCE_EDIT, this::edit);
        handlersMap.put(URLManager.URI_CONFERENCE_DELETE, this::delete);
    }

    public ConferenceController(ServletContext servletContext) {
        super(servletContext);
        conferenceService = serviceFactory.getConferenceService();
    }

    public void list(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, ServiceException {
        final HttpSession session = request.getSession();
        request.setAttribute("conferences",
                conferenceService.findAllTranslated(SessionScope.getCurrentLanguage(session)));

        renderView("conference/list", request, response);
    }

    public void create(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, ServiceException, HttpRedirectException {
        Conference conference = Conference.makeInstanceWithTranslations();
        request.setAttribute("conference", conference);

        if ("POST".equals(request.getMethod())) {
            String eventDateStr = request.getParameter("eventDate");
            LocalDate eventDate = LocalDate.parse(eventDateStr);
            conference.setEventDate(eventDate);

            for (ConferenceTranslation translation : conference.getTranslations()) {
                final Language lang = translation.getLanguage();
                translation.setName(request.getParameter(HtmlSupport.localizedParamName("name", lang)));
                translation.setDescription(request.getParameter(HtmlSupport.localizedParamName("description", lang)));
                translation.setLocation(request.getParameter(HtmlSupport.localizedParamName("location", lang)));
            }

            conferenceService.create(conference);
            SessionScope.setFlashMessage(request.getSession(), "Saved successfully", "success");
            redirect(URLManager.buildURL(URLManager.URI_TALK_LIST, "id=" + conference.getId(), request));
        }

        renderView("conference/create", request, response);
    }

    public void edit(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, ServiceException, HttpRedirectException, HttpNotFoundException {
        final int id = Integer.parseInt(request.getParameter("id"));

        Conference conference = conferenceService.findOneWithTranslations(id, Languages.list())
                .orElseThrow(HttpNotFoundException::new);
        request.setAttribute("conference", conference);

        if ("POST".equals(request.getMethod())) {
            String eventDateStr = request.getParameter("eventDate");
            LocalDate eventDate = LocalDate.parse(eventDateStr);
            conference.setEventDate(eventDate);

            final int actuallyAttended = Integer.parseInt(request.getParameter("actuallyAttendedCount"));
            conference.setActuallyAttendedCount(actuallyAttended);

            for (ConferenceTranslation translation : conference.getTranslations()) {
                final Language lang = translation.getLanguage();
                translation.setName(request.getParameter(HtmlSupport.localizedParamName("name", lang)));
                translation.setDescription(request.getParameter(HtmlSupport.localizedParamName("description", lang)));
                translation.setLocation(request.getParameter(HtmlSupport.localizedParamName("location", lang)));
            }

            conferenceService.update(conference);

            final HttpSession session = request.getSession();
            SessionScope.setFlashMessage(session, "Saved successfully", "success");
            redirect(URLManager.buildURL(URLManager.URI_TALK_LIST, "id=" + conference.getId(), request));
        }

        renderView("conference/edit", request, response);
    }

    public void delete(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, ServiceException, HttpRedirectException, HttpNotFoundException {
        if (!"POST".equals(request.getMethod())) {
            response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            return;
        }

        final int id = Integer.parseInt(request.getParameter("id"));
        Conference conference = conferenceService.findOne(id)
                .orElseThrow(HttpNotFoundException::new);

        conferenceService.delete(conference);

        final HttpSession session = request.getSession();
        SessionScope.setFlashMessage(session, "Deleted successfully", "success");
        redirect((String) session.getAttribute("previousURL"));
    }
}
