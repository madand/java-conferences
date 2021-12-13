package net.madand.conferences.web.controller.impl;

import net.madand.conferences.auth.AuthHelper;
import net.madand.conferences.db.web.QueryOptions;
import net.madand.conferences.db.web.Sorting;
import net.madand.conferences.entity.Conference;
import net.madand.conferences.entity.ConferenceTranslation;
import net.madand.conferences.entity.Language;
import net.madand.conferences.entity.User;
import net.madand.conferences.l10n.Languages;
import net.madand.conferences.service.ServiceException;
import net.madand.conferences.service.impl.ConferenceService;
import net.madand.conferences.web.controller.AbstractController;
import net.madand.conferences.web.controller.exception.HttpException;
import net.madand.conferences.web.controller.exception.HttpRedirectException;
import net.madand.conferences.web.scope.RequestScope;
import net.madand.conferences.web.scope.SessionScope;
import net.madand.conferences.web.util.HtmlSupport;
import net.madand.conferences.web.util.PaginationSortingSupport;
import net.madand.conferences.web.util.URLManager;
import org.apache.log4j.Logger;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class ConferenceController extends AbstractController {
    private static final Logger logger = Logger.getLogger(ConferenceController.class);

    private final ConferenceService conferenceService;

    {
        // Register the controller's actions.
        handlersMap.put(URLManager.URI_CONFERENCE_LIST_UPCOMING, this::listUpcoming);
        handlersMap.put(URLManager.URI_CONFERENCE_LIST_PAST, this::listPast);
        handlersMap.put(URLManager.URI_CONFERENCE_LIST_ALL, this::listAll);
        handlersMap.put(URLManager.URI_CONFERENCE_CREATE, this::create);
        handlersMap.put(URLManager.URI_CONFERENCE_EDIT, this::edit);
        handlersMap.put(URLManager.URI_CONFERENCE_DELETE, this::delete);
        handlersMap.put(URLManager.URI_CONFERENCE_ATTEND, this::attendConference);
        handlersMap.put(URLManager.URI_CONFERENCE_CANCEL_ATTENDANCE, this::cancelAttendance);
        handlersMap.put(URLManager.URI_MY_CONFERENCES, this::listMyConferences);
    }

    public ConferenceController(ServletContext servletContext) {
        super(servletContext);
        conferenceService = serviceFactory.getConferenceService();
    }

    public void listUpcoming(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, ServiceException {
        final HttpSession session = request.getSession();
        final Language currentLanguage = SessionScope.getCurrentLanguage(session);

        final String ITEMS_PER_PAGE_SESSION_KEY = "conferenceListItemsPerPage";
        QueryOptions queryOptions = new PaginationSortingSupport()
                .withSorting(Sorting.DESC, "event_date", "talks_count", "attendees_count")
                .withPagination(ITEMS_PER_PAGE_SESSION_KEY)
                .buildAndApplyTo(request);

        final List<Conference> conferences = conferenceService.findAllUpcomingWithAttendee(
                currentLanguage, RequestScope.getUser(request), queryOptions);
        request.setAttribute("conferences", conferences);

        URLManager.rememberUrlIfGET(request);

        renderView("conference/list", request, response);
    }

    public void listPast(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, ServiceException {
        final HttpSession session = request.getSession();
        final Language currentLanguage = SessionScope.getCurrentLanguage(session);

        final String ITEMS_PER_PAGE_SESSION_KEY = "conferenceListItemsPerPage";
        QueryOptions queryOptions = new PaginationSortingSupport()
                .withSorting(Sorting.ASC, "event_date", "talks_count", "attendees_count")
                .withPagination(ITEMS_PER_PAGE_SESSION_KEY)
                .buildAndApplyTo(request);

        final List<Conference> conferences = conferenceService.findAllPastWithAttendee(
                currentLanguage, RequestScope.getUser(request), queryOptions);

        request.setAttribute("conferences", conferences);

        URLManager.rememberUrlIfGET(request);

        renderView("conference/list", request, response);
    }

    public void listAll(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, ServiceException {
        final HttpSession session = request.getSession();
        final Language currentLanguage = SessionScope.getCurrentLanguage(session);

        final String ITEMS_PER_PAGE_SESSION_KEY = "conferenceListItemsPerPage";
        QueryOptions queryOptions = new PaginationSortingSupport()
                .withSorting(Sorting.ASC, "event_date", "talks_count", "attendees_count")
                .withPagination(ITEMS_PER_PAGE_SESSION_KEY)
                .buildAndApplyTo(request);

        final List<Conference> conferences = conferenceService.findAllWithAttendee(
                currentLanguage, RequestScope.getUser(request), queryOptions);

        request.setAttribute("conferences", conferences);

        URLManager.rememberUrlIfGET(request);

        renderView("conference/list", request, response);
    }

    public void listMyConferences(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, ServiceException, HttpException {
        final User user = AuthHelper.ensureGetUserAnyRole(request);
        final Language currentLanguage = SessionScope.getCurrentLanguage(request.getSession());

        final String ITEMS_PER_PAGE_SESSION_KEY = "myConferencesItemsPerPage";
        QueryOptions queryOptions = new PaginationSortingSupport()
                .withSorting(Sorting.DESC, "event_date", "talks_count", "attendees_count")
                .withPagination(ITEMS_PER_PAGE_SESSION_KEY)
                .buildAndApplyTo(request);

        final List<Conference> conferences = conferenceService.findAllForAttendee(currentLanguage, user, queryOptions);
        request.setAttribute("conferences", conferences);

        URLManager.rememberUrlIfGET(request);

        renderView("conference/myConferences", request, response);
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
            setLocalizedFlashMessageSuccess("flashMessage.savedSuccessfully", request);

            redirect(URLManager.buildURL(URLManager.URI_TALK_LIST, "id=" + conference.getId(), request));
        }

        renderView("conference/create", request, response);
    }

    public void edit(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, ServiceException, HttpRedirectException, HttpException {
        final int id = Integer.parseInt(request.getParameter("id"));
        Conference conference = conferenceService.findOneWithTranslations(id, Languages.list())
                .orElseThrow(HttpException::new);
        request.setAttribute("conference", conference);

        URLManager.rememberUrlIfGET(request);

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

            setLocalizedFlashMessageSuccess("flashMessage.savedSuccessfully", request);
            redirect(URLManager.buildURL(URLManager.URI_TALK_LIST, "id=" + conference.getId(), request));
        }

        renderView("conference/edit", request, response);
    }

    public void delete(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, ServiceException, HttpRedirectException, HttpException {
        checkIsPOST(request);

        final int id = Integer.parseInt(request.getParameter("id"));
        Conference conference = conferenceService.findOne(id)
                .orElseThrow(HttpException::new);

        conferenceService.delete(conference);

        setLocalizedFlashMessageInfo("flashMessage.deletedSuccessfully", request);
        redirect(URLManager.previousUrl(request));
    }

    public void attendConference(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, ServiceException, HttpRedirectException, HttpException {
        checkIsPOST(request);

        final int id = Integer.parseInt(request.getParameter("id"));
        Conference conference = conferenceService.findOne(id)
                .orElseThrow(HttpException::new);

        final Optional<User> userOptional = RequestScope.getUser(request);
        if (!userOptional.isPresent()) {
            redirect(URLManager.URI_USER_LOGIN);
        }

        conferenceService.addAttendee(conference, userOptional.get());

        redirect(URLManager.previousUrl(request));
    }

    public void cancelAttendance(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, ServiceException, HttpRedirectException, HttpException {
        checkIsPOST(request);

        final int id = Integer.parseInt(request.getParameter("id"));
        Conference conference = conferenceService.findOne(id)
                .orElseThrow(HttpException::new);

        final Optional<User> userOptional = RequestScope.getUser(request);
        if (!userOptional.isPresent()) {
            redirect(URLManager.URI_USER_LOGIN);
        }

        conferenceService.removeAttendee(conference, userOptional.get());

        setLocalizedFlashMessageInfo("flashMessage.cancelSuccessfully", request);
        redirect(URLManager.previousUrl(request));
    }
}
