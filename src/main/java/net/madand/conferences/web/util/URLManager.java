package net.madand.conferences.web.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

public class URLManager {
    private URLManager() {}

    public static final String URI_USER_LOGIN = "login";
    public static final String URI_USER_REGISTER = "register";
    public static final String URI_USER_LOGOUT = "logout";
    public static final String URI_USER_EDIT = "edit-user";
    public static final String URI_USER_CHANGE_PASSWORD = "change-password";
    public static final String URI_USER_DELETE = "delete-user";


    public static final String URI_CONFERENCE_LIST = "";
    public static final String URI_CONFERENCE_VIEW = "view-conference";
    public static final String URI_CONFERENCE_CREATE = "create-conference";
    public static final String URI_CONFERENCE_EDIT = "edit-conference";
    public static final String URI_CONFERENCE_DELETE = "delete-conference";

    public static final String URI_TALK_LIST = "list-talks";
    public static final String URI_TALK_VIEW = "view-talk";
    public static final String URI_TALK_CREATE = "create-talk";
    public static final String URI_TALK_EDIT = "edit-talk";
    public static final String URI_TALK_DELETE = "delete-talk";

    public static String buildURLPreserveQuery(String uri, HttpServletRequest request) {
        return buildURL(uri, request.getQueryString(), request);
    }

    public static String buildURL(String uri, String query, HttpServletRequest request) {
        String contextPath = request.getContextPath();
        String builtUri = contextPath + ensureLeadingSlash(uri);
        if (query == null || query.isEmpty()) {
            return builtUri;
        }
        return builtUri + "?" + query;
    }

    private static String ensureLeadingSlash(String uri) {
        return uri.startsWith("/") ? uri : "/" + uri;
    }

    public static String resolveUrl(String url, String context, HttpServletRequest request) throws JspException {
        // normalize relative URLs against a context root
        if (context == null) {
            if (url.startsWith("/")) {
                return request.getContextPath() + url;
            }
            return url;
        }

        if (!context.startsWith("/") || !url.startsWith("/")) {
            throw new RuntimeException("Bad relative path.");
        }

        if (context.equals("/")) {
            // Don't produce string starting with '//', many
            // browsers interpret this as host name, not as
            // path on same host.
            return url;
        }

        return context + url;
    }

    public static String homepage(HttpServletRequest request) {
        return buildURL(URI_CONFERENCE_LIST, null, request);
    }
}
