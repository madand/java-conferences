package net.madand.conferences.web.util;

import javax.servlet.http.HttpServletRequest;

public class URLManager {
    public static final String URI_USER_LOGIN = "/login";
    public static final String URI_USER_REGISTER = "/register";
    public static final String URI_USER_LOGOUT = "/logout";

    public static final String URI_CONFERENCE_LIST = "/";
    public static final String URI_CONFERENCE_VIEW = "/view-conference";
    public static final String URI_CONFERENCE_CREATE = "/create-conference";
    public static final String URI_CONFERENCE_EDIT = "/edit-conference";
    public static final String URI_CONFERENCE_DELETE = "/delete-conference";

    public static String buildURL(HttpServletRequest request) {
        String uri = request.getRequestURI();
        String query = request.getQueryString();
        if (query == null) {
            return uri;
        }
        return uri + "?" + query;
    }
}