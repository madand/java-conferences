package net.madand.conferences.web.controller;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface Controller {
    /**
     * Let the controller try to find a handler for the request. If found, the handler will be invoked and this method
     * returns true; otherwise return false.
     *
     * @param request the request.
     * @param response the response.
     * @return true if a handler was found and the request was handled; otherwise - false.
     * @throws ServletException
     * @throws IOException
     */
    boolean tryHandleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;

    /**
     * The concrete controller action (i.e. a handler method).
     */
    @FunctionalInterface
    interface Action {
        void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;
    }
}
