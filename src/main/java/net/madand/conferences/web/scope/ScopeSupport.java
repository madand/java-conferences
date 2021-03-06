package net.madand.conferences.web.scope;

import org.apache.log4j.Logger;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;
import java.util.function.Supplier;

class ScopeSupport {
    private final Logger logger;

    public ScopeSupport(Logger logger) {
        this.logger = logger;
    }

    public Object getAttributeOrThrow(ServletContext servletContext, String attribute) {
        return Optional.ofNullable(servletContext.getAttribute(attribute))
                .orElseThrow(makeAttributeExceptionSupplier(attribute));
    }

    public Object getAttributeOrThrow(HttpSession session, String attribute) {
        return Optional.ofNullable(session.getAttribute(attribute))
                .orElseThrow(makeAttributeExceptionSupplier(attribute));
    }

    public Object getAttributeOrThrow(ServletRequest request, String attribute) {
        return Optional.ofNullable(request.getAttribute(attribute))
                .orElseThrow(makeAttributeExceptionSupplier(attribute));
    }


    public void setAttributeAndLog(ServletContext servletContext, String attribute, Object value) {
        servletContext.setAttribute(attribute, value);
        logSetAttribute(attribute, value);
    }

    public void setAttributeAndLog(HttpSession session, String attribute, Object value) {
        session.setAttribute(attribute, value);
        logSetAttribute(attribute, value);
    }

    public void setAttributeAndLog(ServletRequest request, String attribute, Object value) {
        request.setAttribute(attribute, value);
        logSetAttribute(attribute, value);
    }

    public void removeAttributeAndLog(HttpSession session, String attribute) {
        session.removeAttribute(attribute);
        logRemoveAttribute(attribute);
    }

    private Supplier<IllegalStateException> makeAttributeExceptionSupplier(String attribute) {
        return () -> new IllegalStateException(attribute + " attribute was not properly set (is NULL).");
    }

    private void logSetAttribute(String attribute, Object value) {
        logger.debug(attribute + " was set to: " + value);
    }

    private void logRemoveAttribute(String attribute) {
        logger.debug(attribute + " was removed");
    }
}
