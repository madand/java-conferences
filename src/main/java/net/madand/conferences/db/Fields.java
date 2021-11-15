package net.madand.conferences.db;

/**
 * Names of the database table fields.
 */
public class Fields {
    // common fields
    public static final String ID = "id";
    public static final String CREATED_AT = "created_at";
    public static final String UPDATED_AT = "updated_at";

    // foreign key fields
    public static final String LANGUAGE_ID = "language_id";
    public static final String CONFERENCE_ID = "conference_id";

    // conference
    public static final String EVENT_DATE = "event_date";
    public static final String ACTUALLY_ATTENDED_COUNT = "actually_attended_count";

    // conference_translation
    public static final String NAME = "name";
    public static final String DESCRIPTION = "description";
    public static final String LOCATION = "location";
}
