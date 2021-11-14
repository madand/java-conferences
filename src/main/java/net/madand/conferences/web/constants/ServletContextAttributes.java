package net.madand.conferences.web.constants;

/**
 * Singleton helper for having easy access to the global ServletContext anywhere.
 * Also provides accessors for the context attributes that should be available for whole application lifetime,
 * these methods also encapsulate type casts.
 */
public class ServletContextAttributes {
    public static final String DATA_SOURCE = "dataSource";
    public static final String LANGUAGES = "languages";

    private ServletContextAttributes() {}
}
