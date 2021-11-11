package net.madand.conferences.auth;

/**
 * Application user roles.
 * Note: in the database the role type is defined as PostgreSQL's native ENUM type, named "user_role".
 */
public enum Role {
    MODERATOR, SPEAKER, ATTENDEE;

    public boolean isModerator() {
        return this == MODERATOR;
    }

    public boolean isSpeaker() {
        return this == SPEAKER;
    }

    public boolean isAttendee() {
        return this == ATTENDEE;
    }
}