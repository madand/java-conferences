package net.madand.conferences.web.util;

import net.madand.conferences.web.scope.SessionScope;

import javax.servlet.http.HttpServletRequest;

public class URLManager {
    private URLManager() {}

    public static final String URI_USER_LOGIN = "login";
    public static final String URI_USER_REGISTER = "register";
    public static final String URI_USER_LOGOUT = "logout";
    public static final String URI_USER_EDIT = "edit-user";
    public static final String URI_USER_CHANGE_PASSWORD = "change-password";
    public static final String URI_USER_DELETE = "delete-user";
    public static final String URI_USER_MANAGE = "manage-users";

    public static final String URI_CONFERENCE_LIST_UPCOMING = "";
    public static final String URI_CONFERENCE_LIST_PAST = "past";
    public static final String URI_CONFERENCE_LIST_ALL = "all";
    public static final String URI_CONFERENCE_CREATE = "create-conference";
    public static final String URI_CONFERENCE_EDIT = "edit-conference";
    public static final String URI_CONFERENCE_DELETE = "delete-conference";
    public static final String URI_CONFERENCE_ATTEND = "attend-conference";
    public static final String URI_CONFERENCE_CANCEL_ATTENDANCE = "cancel-attendance";
    public static final String URI_MY_CONFERENCES = "my-conferences";

    public static final String URI_TALK_LIST = "list-talks";
    public static final String URI_TALK_CREATE = "create-talk";
    public static final String URI_TALK_EDIT = "edit-talk";
    public static final String URI_TALK_DELETE = "delete-talk";

    public static final String URI_TALK_PROPOSAL_LIST_MODER = "list-talk-proposals-moderator";
    public static final String URI_TALK_PROPOSAL_LIST_SPEAKER = "list-talk-proposals-speaker";
    public static final String URI_TALK_PROPOSAL_CREATE = "create-talk-proposal";
    public static final String URI_TALK_PROPOSAL_REVIEW = "review-talk-proposal";
    public static final String URI_TALK_PROPOSAL_DELETE = "delete-talk-proposal";

    public static final String URI_TALK_SPEAKER_REQUEST_LIST_MODER = "list-talk-speaker-requests-moderator";
    public static final String URI_TALK_SPEAKER_REQUEST_LIST_SPEAKER = "list-talk-speaker-requests-speaker";
    public static final String URI_TALK_SPEAKER_REQUEST_CREATE = "request-being-speaker-for-talk";
    public static final String URI_TALK_SPEAKER_REQUEST_ACCEPT = "accept-talk-speaker-request";
    public static final String URI_TALK_SPEAKER_REQUEST_DELETE = "delete-talk-speaker-request";

    public static final String URI_TALK_SPEAKER_PROPOSAL_LIST_MODER = "list-talk-speaker-proposals-moderator";
    public static final String URI_TALK_SPEAKER_PROPOSAL_LIST_SPEAKER = "list-talk-speaker-proposals-speaker";
    public static final String URI_TALK_SPEAKER_PROPOSAL_CREATE = "propose-being-speaker-for-talk";
    public static final String URI_TALK_SPEAKER_PROPOSAL_ACCEPT = "accept-talk-speaker-proposal";
    public static final String URI_TALK_SPEAKER_PROPOSAL_DELETE = "delete-talk-speaker-proposal";

    public static String buildURLPreserveQuery(String uri, HttpServletRequest request) {
        return buildURL(uri, request.getQueryString(), request);
    }

    public static String buildURL(String uri, String query, HttpServletRequest request) {
        String contextPath = request.getContextPath();
        String builtUrl = contextPath + ensureLeadingSlash(uri);
        if (query == null || query.isEmpty()) {
            return builtUrl;
        }
        return builtUrl + "?" + query;
    }

    private static String ensureLeadingSlash(String uri) {
        return uri.startsWith("/") ? uri : "/" + uri;
    }

    public static String homePage(HttpServletRequest request) {
        return buildURL(URI_CONFERENCE_LIST_UPCOMING, null, request);
    }

    public static void rememberUrlIfGET(HttpServletRequest request) {
        if ("GET".equals(request.getMethod())) {
            SessionScope.setPreviousUrl(request.getSession(), buildURLPreserveQuery(request.getServletPath(), request));
        }
    }

    public static String previousUrl(HttpServletRequest request) {
        return SessionScope.getPreviousUrl(request.getSession(), URLManager.homePage(request));
    }
}
