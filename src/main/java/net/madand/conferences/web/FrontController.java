package net.madand.conferences.web;

import net.madand.conferences.service.ServiceException;
import net.madand.conferences.web.constants.ServletContextAttributes;
import net.madand.conferences.web.controller.AbstractController;
import net.madand.conferences.web.controller.ConferenceController;
import org.apache.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class FrontController extends HttpServlet {
    private static final long serialVersionUID = 3867883190596876581L;

    private static final Logger log = Logger.getLogger(FrontController.class);

    private AbstractController[] controllers;

    @Override
    public void init() throws ServletException {
        final ServletContext servletContext = getServletContext();
        controllers = new AbstractController[] {
                new ConferenceController(servletContext),
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
     * Main method of this controller.
     */
    private void process(HttpServletRequest request,
                         HttpServletResponse response) throws IOException, ServletException {
        log.trace("FrontController process begin");

        boolean handled = false;
        for (AbstractController controller : controllers) {
            if (controller.handleRequest(request, response)) {
                handled = true;
                break;
            }
        }

        if (!handled) {
            response.sendError(404, "Page not found");
        }
    }
}
