package net.madand.conferences.web.controller;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public abstract class AbstractController {
    protected ServletContext servletContext;

    public AbstractController(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    protected ServletContext getServletContext() {
        return servletContext;
    }

    /**
     * Return a map from URI paths to request handlers.
     * This is an example of the Template Method pattern.
     *
     * @return the map from URI paths to request handlers.
     */
    protected abstract Map<String, ControllerAction> getHandlersMap();

    public boolean handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        final String path = request.getServletPath();
        for (Map.Entry<String, ControllerAction> entry : getHandlersMap().entrySet()) {
            if (entry.getKey().equals(path)) {
                entry.getValue().handleRequest(request, response);
                return true;
            }
        }
        return false;
    }

    @FunctionalInterface
    public interface ControllerAction {
        public void handleRequest(HttpServletRequest request, HttpServletResponse response)  throws ServletException, IOException;
    }
}
