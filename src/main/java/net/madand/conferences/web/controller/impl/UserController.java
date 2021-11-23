package net.madand.conferences.web.controller.impl;

import net.madand.conferences.auth.Role;
import net.madand.conferences.entity.User;
import net.madand.conferences.security.PasswordHelper;
import net.madand.conferences.service.ServiceException;
import net.madand.conferences.service.impl.UserService;
import net.madand.conferences.web.bean.LoginBean;
import net.madand.conferences.web.controller.AbstractController;
import net.madand.conferences.web.controller.exception.HttpNotFoundException;
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
        handlersMap.put(URLManager.URI_USER_REGISTER, this::register);
        handlersMap.put(URLManager.URI_USER_DELETE, this::delete);
    }

    public UserController(ServletContext servletContext) {
        super(servletContext);
        userService = serviceFactory.getUserService();
    }

    private void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, ServiceException, HttpRedirectException {
        LoginBean bean = new LoginBean();
        request.setAttribute("bean", bean);

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


    private void register(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, ServiceException, HttpRedirectException {
        User user = new User();
        user.setRole(Role.ATTENDEE); // Newly registered users are always Attendees.
        request.setAttribute("bean", user);

        if ("POST".equals(request.getMethod())) {
            final HttpSession session = request.getSession();

            user.setEmail(request.getParameter("email"));
            user.setRealName(request.getParameter("realName"));
            String password = request.getParameter("password");
            String passwordRepeat = request.getParameter("passwordRepeat");

            if (password != null && password.equals(passwordRepeat)) {
                user.setPassword(password);
                userService.create(user);
                SessionScope.setCurrentUserId(session, user.getId());
                SessionScope.setFlashMessage(session,
                        String.format("Successfully registered as %s.", user.getRealName()), "success");

                redirect(URLManager.homepage(request));
            } else {
                SessionScope.setFlashMessage(session, "Entered passwords are not the same", "error");
            }

//            SessionScope.setFlashMessage(session, "Invalid login or password", "danger");
        }

        renderView("user/register", request, response);
    }

    public void delete(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, ServiceException, HttpRedirectException, HttpNotFoundException {
        if (!"POST".equals(request.getMethod())) {
            response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            return;
        }

        User user = RequestScope.getUser(request)
                .orElseThrow(HttpNotFoundException::new);

        userService.delete(user);
        final HttpSession session = request.getSession();
        SessionScope.setFlashMessage(session, "Deleted successfully", "success");

        // Also, logout the user.
        logout(request, response);
    }

    private void logout(HttpServletRequest request, HttpServletResponse response) throws IOException, HttpRedirectException, HttpNotFoundException {
        if (!RequestScope.getUser(request).isPresent()) {
            // If no user is logged in, just show them the home page.
            redirect(URLManager.homepage(request));
        }

        SessionScope.removeCurrentUserId(request.getSession());

        redirect(URLManager.homepage(request));
    }
}
