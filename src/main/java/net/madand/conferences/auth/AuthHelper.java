package net.madand.conferences.security;

import net.madand.conferences.entity.User;
import net.madand.conferences.web.controller.exception.HttpException;
import net.madand.conferences.web.scope.RequestScope;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

public class PermissionHelper {
    private PermissionHelper() {}

    public static User ensureGetModerator(HttpServletRequest request) throws HttpException {
        User user = RequestScope.getUser(request).orElseThrow(HttpException::forbidden);
        return Optional.of(user)
                .filter(u -> u.getRole().isModerator())
                .orElseThrow(HttpException::forbidden);
    }
}
