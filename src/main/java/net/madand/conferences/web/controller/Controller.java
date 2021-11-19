package net.madand.conferences.web.controller;

import net.madand.conferences.service.ServiceException;
import net.madand.conferences.web.controller.exception.HttpNotFoundException;
import net.madand.conferences.web.controller.exception.HttpRedirectException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface Controller {
    /**
     * Let the controller try to find a handler for the request. If found, the handler will be invoked and this method
     * returns true; otherwise returns false.
     *
     * @param request the request.
     * @param response the response.
     * @return true if a handler was found and the request was handled; otherwise - false.
     * @throws ServletException
     * @throws IOException
     */
    boolean maybeHandleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, ServiceException, HttpNotFoundException, HttpRedirectException;

    /**
     * The concrete controller action (i.e. a handler method).
     */
    @FunctionalInterface
    interface Action {
        void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, ServiceException, HttpNotFoundException, HttpRedirectException;
    }
}
