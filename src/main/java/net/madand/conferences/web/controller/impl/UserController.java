package net.madand.conferences.web.controller.impl;

import net.madand.conferences.entity.User;
import net.madand.conferences.security.PasswordHelper;
import net.madand.conferences.service.ServiceException;
import net.madand.conferences.service.impl.UserService;
import net.madand.conferences.web.bean.LoginBean;
import net.madand.conferences.web.controller.AbstractController;
import net.madand.conferences.web.controller.exception.HttpRedirectException;
import net.madand.conferences.web.scope.RequestScope;
import net.madand.conferences.web.scope.SessionScope;
import net.madand.conferences.web.util.URLManager;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Optional;

public class UserController extends AbstractController {
    private final UserService userService;

    {
        // Register the controller's actions.
        handlersMap.put(URLManager.URI_USER_LOGIN, this::login);
        handlersMap.put(URLManager.URI_USER_LOGOUT, this::logout);
    }

    public UserController(ServletContext servletContext) {
        super(servletContext);
        userService = serviceFactory.getUserService();
    }

    private void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, ServiceException, HttpRedirectException {
        LoginBean bean = new LoginBean();
        request.setAttribute("entity", bean);

        if ("POST".equals(request.getMethod())) {
            final HttpSession session = request.getSession();

            bean.setEmail(request.getParameter("email"));
            bean.setPassword(request.getParameter("password"));

            final Optional<User> userOptional = userService.findByEmail(bean.getEmail());

            if (userOptional.isPresent()) {
                User user = userOptional.get();
                if (PasswordHelper.checkPassword(bean.getPassword(), user.getPasswordHash())) {
                    SessionScope.setCurrentUserId(session, user.getId());
                    SessionScope.setFlashMessage(session,
                            String.format("Successfully logged in as %s.", user.getRealName()), "success");

                    redirect(URLManager.homepage(request));
                }
            }

            SessionScope.setFlashMessage(session, "Invalid login or password", "danger");
        }

        renderView("user/login", request, response);
    }

    private void logout(HttpServletRequest request, HttpServletResponse response) throws IOException, HttpRedirectException {
        final HttpSession session = request.getSession();
        User user = RequestScope.getUser(request);
        if (user != null) {
            SessionScope.removeCurrentUserId(session);
        }

        redirect(URLManager.homepage(request));
    }
}
