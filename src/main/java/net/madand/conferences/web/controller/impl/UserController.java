package net.madand.conferences.web.controller.impl;

import net.madand.conferences.auth.Role;
import net.madand.conferences.entity.User;
import net.madand.conferences.security.PasswordHelper;
import net.madand.conferences.service.ServiceException;
import net.madand.conferences.service.impl.UserService;
import net.madand.conferences.web.bean.LoginBean;
import net.madand.conferences.web.controller.AbstractController;
import net.madand.conferences.web.controller.exception.HttpException;
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
        handlersMap.put(URLManager.URI_USER_EDIT, this::edit);
        handlersMap.put(URLManager.URI_USER_CHANGE_PASSWORD, this::changePassword);
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
                    SessionScope.setFlashMessageSuccess(session,
                            String.format("Successfully logged in as %s.", user.getRealName()));

                    redirect(URLManager.previousUrl(request));
                }
            }

            SessionScope.setFlashMessageError(session, "Invalid login or password");
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
                SessionScope.setFlashMessageSuccess(session,
                        String.format("Successfully registered as %s.", user.getRealName()));

                redirect(URLManager.previousUrl(request));
            } else {
                SessionScope.setFlashMessageError(session, "Entered passwords are not the same");
            }
        }

        renderView("user/register", request, response);
    }

    private void edit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, ServiceException, HttpRedirectException, HttpException {
        final User user = RequestScope.getUser(request).orElseThrow(HttpException::forbidden);
        request.setAttribute("bean", user);

        URLManager.rememberUrlIfGET(request);

        if ("POST".equals(request.getMethod())) {
            final HttpSession session = request.getSession();

            user.setEmail(request.getParameter("email"));
            user.setRealName(request.getParameter("realName"));

            userService.update(user);

            SessionScope.setFlashMessageSuccess(session, "Successfully updated user profile");
            redirect(URLManager.previousUrl(request));
        }

        renderView("user/edit", request, response);
    }

    private void delete(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, ServiceException, HttpRedirectException, HttpException {
        checkIsPOST(request);

        User user = RequestScope.getUser(request).orElseThrow(HttpException::new);

        userService.delete(user);
        SessionScope.setFlashMessageSuccess(request.getSession(), "Successfully deleted user profile");
        logout(request, response);
    }

    private void logout(HttpServletRequest request, HttpServletResponse response) throws IOException, HttpRedirectException, HttpException {
        SessionScope.removeCurrentUserId(request.getSession());

        redirect(URLManager.homePage(request));
    }

    private void changePassword(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, ServiceException, HttpRedirectException, HttpException {
        final User user = RequestScope.getUser(request).orElseThrow(HttpException::forbidden);
        request.setAttribute("bean", user);

        if ("POST".equals(request.getMethod())) {
            final HttpSession session = request.getSession();

            String oldPassword = Optional.ofNullable(request.getParameter("password")).orElse("");
            String passwordNew = Optional.ofNullable(request.getParameter("passwordNew")).orElse("");
            String passwordNewRepeat = Optional.ofNullable(request.getParameter("passwordNewRepeat")).orElse("");

            if (PasswordHelper.checkPassword(oldPassword, user.getPasswordHash())
                    && passwordNew.equals(passwordNewRepeat)) {
                user.setPassword(passwordNew);
                userService.update(user);
                SessionScope.setFlashMessageSuccess(session, "Successfully changed password");

                redirect(URLManager.previousUrl(request));
            }

            SessionScope.setFlashMessageError(session, "Entered passwords did not match");
        }

        renderView("user/changePassword", request, response);
    }
}
