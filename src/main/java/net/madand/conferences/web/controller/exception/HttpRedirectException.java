package net.madand.conferences.web.controller.exception;

/**
 * This type of exception designates that the controller action wants to perform a HTTP redirect, instead of just
 * rendering the view.
 */
public class HttpRedirectException extends Exception {
    private String url;

    public HttpRedirectException(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
