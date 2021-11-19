package net.madand.conferences.web.controller.exception;

/**
 * This type of exception designates that the controller action cannot find a resource, thus HTTP 404 should be sent
 * back to the client.
 */
public class HttpNotFoundException extends Exception {
    public HttpNotFoundException() {
        super();
    }
}
