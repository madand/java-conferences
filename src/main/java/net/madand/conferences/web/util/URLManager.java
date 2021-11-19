package net.madand.conferences.web.util;

import net.madand.conferences.entity.Conference;
import net.madand.conferences.web.scope.SessionScope;
import org.apache.taglibs.standard.resources.Resources;
import org.apache.taglibs.standard.tag.common.core.ImportSupport;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.PageContext;
import javax.xml.validation.SchemaFactoryConfigurationError;

public class URLManager {
    private URLManager() {}

    public static final String URI_USER_LOGIN = "login";
    public static final String URI_USER_REGISTER = "register";
    public static final String URI_USER_LOGOUT = "logout";

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
        return buildURL(request, uri, request.getQueryString());
    }

    public static String buildURL(HttpServletRequest request, String uri, String query) {
        String contextPath = request.getContextPath();
        String builtUri = contextPath + ensureLeadingSlash(uri);
        if (query == null) {
            return builtUri;
        }
        return builtUri + "?" + query;
    }

    private static String ensureLeadingSlash(String uri) {
        return uri.startsWith("/") ? uri : "/" + uri;
    }

    public static String makeListTalksURL(HttpServletRequest request, Conference conference) {
        return buildURL(request, URI_TALK_LIST, "conference_id=" + conference.getId());
    }

    public static String resolveUrl(String url, String context, HttpServletRequest request) throws JspException {
        // normalize relative URLs against a context root
        if (context == null) {
            if (url.startsWith("/"))
                return (request.getContextPath() + url);
            else
                return url;
        } else {
            if (!context.startsWith("/") || !url.startsWith("/")) {
                throw new RuntimeException("Bad relative path.");
            }
            if (context.equals("/")) {
                // Don't produce string starting with '//', many
                // browsers interpret this as host name, not as
                // path on same host.
                return url;
            } else {
                return (context + url);
            }
        }
    }

    public static String homepage(HttpServletRequest request) {
        return buildURL(request, URI_CONFERENCE_LIST, null);
    }
}
