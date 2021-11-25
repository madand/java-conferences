package net.madand.conferences.db.dao;

import net.madand.conferences.db.util.QueryHelper;
import net.madand.conferences.entity.Conference;
import net.madand.conferences.entity.User;

import java.sql.Connection;
import java.sql.SQLException;

public class ConferenceAttendeeDao {
    private ConferenceAttendeeDao() {}

    public static void insert(Connection conn, Conference conference, User user) throws SQLException {
        final String SQL = "INSERT INTO conference_attendee (conference_id, user_id) VALUES (?,?)";
        QueryHelper.insert(conn, SQL,
                stmt -> {
                    stmt.setInt(1, conference.getId());
                    stmt.setInt(2, user.getId());
                }
        );
    }

    public static void delete(Connection conn, Conference conference, User user) throws SQLException {
        final String SQL = "DELETE FROM conference_attendee WHERE conference_id = ? AND user_id = ?";
        QueryHelper.delete(conn, SQL,
                stmt -> {
                    stmt.setInt(1, conference.getId());
                    stmt.setInt(2, user.getId());
                }
        );
    }
}
