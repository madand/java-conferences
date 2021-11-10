package net.madand.conferences.auth;

/**
 * Application user roles. String representation is lower cased, and that is how it is stored in the database.
 * Note: inside the database the role type is defined as PostgreSQL's native ENUM type, named "user_role".
 */
public enum Role {
    MODERATOR, SPEAKER, ATTENDEE;

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}