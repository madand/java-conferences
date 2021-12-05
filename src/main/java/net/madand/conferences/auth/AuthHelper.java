package net.madand.conferences.auth;

import net.madand.conferences.entity.User;
import net.madand.conferences.web.controller.exception.HttpException;
import net.madand.conferences.web.scope.RequestScope;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

public class AuthHelper {
    private AuthHelper() {}

    public static User ensureGetModerator(HttpServletRequest request) throws HttpException {
        return Optional.of(ensureGetUserAnyRole(request))
                .filter(u -> u.getRole().isModerator())
                .orElseThrow(HttpException::forbidden);
    }

    public static User ensureGetUserAnyRole(HttpServletRequest request) throws HttpException {
        return RequestScope.getUser(request).orElseThrow(HttpException::forbidden);
    }
}
