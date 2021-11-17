package net.madand.conferences.web.controller;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public abstract class AbstractController implements Controller {
    protected final ServletContext servletContext;

    protected AbstractController(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    /**
     * Return a map from URI paths to request handlers.
     * This is an example of the Template Method pattern.
     *
     * @return the map from URI paths to request handlers.
     */
    protected abstract Map<String, Action> getHandlersMap();

    @Override
    public boolean tryHandleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        final String path = request.getServletPath().substring(1);
        for (Map.Entry<String, Action> entry : getHandlersMap().entrySet()) {
            if (entry.getKey().equals(path)) {
                entry.getValue().handleRequest(request, response);
                return true;
            }
        }
        return false;
    }

}
