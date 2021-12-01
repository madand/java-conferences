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
    public static final String TALK_ID = "talk_id";
    public static final String SPEAKER_ID = "speaker_id";
    public static final String MODERATOR_ID = "speaker_id";

    // conference
    public static final String EVENT_DATE = "event_date";
    public static final String ACTUALLY_ATTENDED_COUNT = "actually_attended_count";

    // talk
    public static final String START_TIME = "start_time";
    public static final String END_TIME = "end_time";
    public static final String DURATION = "duration";

    // conference_translation and talk_translation
    public static final String NAME = "name";
    public static final String DESCRIPTION = "description";
    public static final String LOCATION = "location";

    // virtual columns in views
    public static final String ATTENDEE_ID = "attendee_id";
    public static final String TALK_NAME = "talk_name";
    public static final String CONFERENCE_NAME = "conference_name";
    public static final String SPEAKER_NAME = "speaker_name";
    public static final String SPEAKER_EMAIL = "speaker_email";
    public static final String MODERATOR_NAME = "moderator_name";
    public static final String MODERATOR_EMAIL = "moderator_email";
    public static final String ATTENDEES_COUNT = "attendees_count";
}
