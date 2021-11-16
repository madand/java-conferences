package net.madand.conferences.web.constant;

/**
 * Singleton helper for having easy access to the global ServletContext anywhere.
 * Also provides accessors for the context attributes that should be available for whole application lifetime,
 * these methods also encapsulate type casts.
 */
public class ServletContextAttributes {

    private ServletContextAttributes() {}
}
