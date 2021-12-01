package net.madand.conferences.web.controller;

import net.madand.conferences.service.ServiceException;
import net.madand.conferences.service.ServiceFactory;
import net.madand.conferences.web.controller.exception.HttpException;
import net.madand.conferences.web.controller.exception.HttpRedirectException;
import net.madand.conferences.web.scope.ContextScope;
import net.madand.conferences.web.scope.SessionScope;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractController implements Controller {
    protected final ServiceFactory serviceFactory;

    /**
     * Every concrete controller fills this map with [URL => action handler] pairs.
     */
    protected final Map<String, Action> handlersMap = new HashMap<>();

    protected AbstractController(ServletContext servletContext) {
        this.serviceFactory = ContextScope.getServiceFactory(servletContext);
    }

    @Override
    public boolean maybeHandleRequest(HttpServletRequest request, HttpServletResponse response) throws
            ServletException, IOException, ServiceException, HttpException, HttpRedirectException {
        // Remove the leading slash.
        final String path = request.getServletPath().substring(1);
        Action action = handlersMap.get(path);
        if (action != null) {
            action.handleRequest(request, response);
            return true;
        }
        return false;
    }

    protected void renderView(String view, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        final String viewPath = String.format("/WEB-INF/jsp/%s.jsp", view);
        request.getRequestDispatcher(viewPath).forward(request, response);
    }

    protected void redirect(String url) throws HttpRedirectException {
        throw new HttpRedirectException(url);
    }

    protected void checkIsPOST(HttpServletRequest request) throws HttpException {
        if (!"POST".equals(request.getMethod())) {
            throw new HttpException(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        }
    }

    protected void setLocalizedFlashMessageSuccess(String key, HttpServletRequest request) {
        SessionScope.setFlashMessageSuccess(
                request.getSession(),
                ContextScope.getLocalizationHelper(request.getServletContext())
                        .message(key, SessionScope.getCurrentLanguage(request.getSession())));
    }

    protected void setLocalizedFlashMessageInfo(String key, HttpServletRequest request) {
        SessionScope.setFlashMessageInfo(
                request.getSession(),
                ContextScope.getLocalizationHelper(request.getServletContext())
                        .message(key, SessionScope.getCurrentLanguage(request.getSession())));
    }

    protected void setLocalizedFlashMessageError(String key, HttpServletRequest request) {
        SessionScope.setFlashMessageError(
                request.getSession(),
                ContextScope.getLocalizationHelper(request.getServletContext())
                        .message(key, SessionScope.getCurrentLanguage(request.getSession())));
    }
}
