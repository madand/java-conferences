package net.madand.conferences.web.controller.exception;

import javax.servlet.http.HttpServletResponse;

/**
 * This type of exception designates that the controller action cannot process the request normally.
 */
public class HttpException extends Exception {
    private int statusCode = HttpServletResponse.SC_NOT_FOUND;

    public HttpException() {}

    public HttpException(int statusCode) {
        this.statusCode = statusCode;
    }

    public HttpException(int statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }

    public HttpException(int statusCode, String message, Throwable cause) {
        super(message, cause);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
