package net.madand.conferences.web;

import net.madand.conferences.service.ServiceException;
import net.madand.conferences.web.controller.Controller;
import net.madand.conferences.web.controller.exception.HttpNotFoundException;
import net.madand.conferences.web.controller.exception.HttpRedirectException;
import net.madand.conferences.web.controller.impl.ConferenceController;
import net.madand.conferences.web.controller.impl.TalkController;
import net.madand.conferences.web.controller.impl.UserController;
import org.apache.log4j.Logger;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class HttpRouter extends HttpServlet {
    private static final long serialVersionUID = 3867883190596876581L;

    private static final Logger log = Logger.getLogger(HttpRouter.class);

    private Controller[] controllers;

    @Override
    public void init() throws ServletException {
        final ServletContext servletContext = getServletContext();

        // All active controllers should be instantiated here.
        controllers = new Controller[] {
                new ConferenceController(servletContext),
                new UserController(servletContext),
                new TalkController(servletContext),
        };
    }

    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException {
        process(request, response);
    }

    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws ServletException, IOException {
        process(request, response);
    }

    /**
     * Try to route the request to the appropriate controller. If there was no controller able to handle the request,
     * respond with 404 error.
     */
    private void process(HttpServletRequest request,
                         HttpServletResponse response) throws IOException, ServletException {
        log.trace("HttpRouter process began");

        boolean handled = false;
        for (Controller controller : controllers) {
            try {
                if (controller.maybeHandleRequest(request, response)) {
                    handled = true;
                    break;
                }
            } catch (ServiceException e) {
                log.error(e);
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
                return;
            } catch (HttpRedirectException e) {
                response.sendRedirect(response.encodeRedirectURL(e.getUrl()));
                return;
            } catch (HttpNotFoundException e) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
        }

        if (!handled) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}
