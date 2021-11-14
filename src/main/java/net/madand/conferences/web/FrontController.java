package net.madand.conferences.web;

import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class FrontController extends HttpServlet {
    private static final long serialVersionUID = 3867883190596876581L;

    private static final Logger log = Logger.getLogger(FrontController.class);

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
        log.trace("FrontController start");

        response.getWriter().print(request.getServletContext().getAttribute("javax.servlet.jsp.jstl.fmt.locale.application"));
//        request.getRequestDispatcher("WEB-INF/jsp/index.jsp").forward(request, response);

//        // extract command name from the request
//        String commandName = request.getParameter("command");
//        log.trace("Request parameter: command --> " + commandName);
//
//        // obtain command object by its name
//        Command command = CommandContainer.get(commandName);
//        log.trace("Obtained command --> " + command);
//
//        // execute command and get forward address
//        String forward = command.execute(request, response);
//        log.trace("Forward address --> " + forward);
//
//        log.debug("Controller finished, now go to forward address --> " + forward);
//
//        // if the forward address is not null go to the address
//        if (forward != null) {
//            RequestDispatcher disp = request.getRequestDispatcher(forward);
//            disp.forward(request, response);
//        }
    }
}
