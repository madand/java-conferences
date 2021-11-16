package net.madand.conferences.web.controller.impl;

import net.madand.conferences.entity.User;
import net.madand.conferences.security.PasswordHelper;
import net.madand.conferences.service.ServiceException;
import net.madand.conferences.service.impl.UserService;
import net.madand.conferences.web.bean.LoginBean;
import net.madand.conferences.web.controller.AbstractController;
import net.madand.conferences.web.util.ContextHelper;
import net.madand.conferences.web.util.URLManager;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class UserController extends AbstractController {
    private UserService service;
    private Map<String, Action> handlersMap;

    public UserController(ServletContext servletContext) {
        super(servletContext);
        service = new UserService(ContextHelper.getDataSource(servletContext));
    }

    @Override
    protected Map<String, Action> getHandlersMap() {
        if (handlersMap == null) {
            handlersMap = new HashMap<>();

            handlersMap.put(URLManager.URI_USER_LOGIN, this::login);
            handlersMap.put(URLManager.URI_USER_LOGOUT, this::logout);
        }

        return handlersMap;
    }

    private void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LoginBean bean = new LoginBean();
        request.setAttribute("entity", bean);

        if ("POST".equals(request.getMethod())) {
            final HttpSession session = request.getSession();

            bean.setEmail(request.getParameter("email"));
            bean.setPassword(request.getParameter("password"));

            final Optional<User> user;
            try {
                user = service.findByEmail(bean.getEmail());
            } catch (ServiceException e) {
                response.sendError(500, e.getMessage());
                return;
            }

            if (user.isPresent() && PasswordHelper.checkPassword(bean.getPassword(), user.get().getPasswordHash())) {
                session.setAttribute("user", user.get());
                session.setAttribute("flashMessage", String.format("Successfully logged in as %s.", user.get().getRealName()));
                session.setAttribute("flashType", "success");
                response.sendRedirect(response.encodeRedirectURL(URLManager.buildURL(URLManager.URI_CONFERENCE_LIST, request)));
                return;
            }

            session.setAttribute("flashMessage", "Invalid login or password");
            session.setAttribute("flashType", "danger");
        }

        request.getRequestDispatcher("/WEB-INF/jsp/user/login.jsp").forward(request, response);
    }

    private void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user != null) {
            session.removeAttribute("user");
            session.setAttribute("flashMessage", String.format("Good buy %s.", user.getRealName()));
            session.setAttribute("flashType", "info");
        }

        response.sendRedirect(response.encodeRedirectURL(URLManager.buildURL(URLManager.URI_CONFERENCE_LIST, request)));
    }
}
