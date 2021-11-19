package net.madand.conferences.web.controller;

import net.madand.conferences.service.ServiceException;
import net.madand.conferences.web.controller.exception.HttpNotFoundException;
import net.madand.conferences.web.controller.exception.HttpRedirectException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractController implements Controller {
    protected final ServletContext servletContext;

    /**
     * Every concrete controller fills this map with [URL => action handler] pairs.
     */
    protected Map<String, Action> handlersMap = new HashMap<>();

    protected AbstractController(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    @Override
    public boolean maybeHandleRequest(HttpServletRequest request, HttpServletResponse response) throws
            ServletException, IOException, ServiceException, HttpNotFoundException, HttpRedirectException {
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
}
