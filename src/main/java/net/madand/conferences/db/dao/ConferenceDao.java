package net.madand.conferences.db.dao;

import net.madand.conferences.db.QueryHelper;
import net.madand.conferences.db.StatementParametersSetter;
import net.madand.conferences.entity.Conference;
import net.madand.conferences.l10n.Languages;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

public class ConferenceDao {
    private static final String SQL_FIND_ALL = "SELECT * FROM conference ORDER BY event_date DESC";
    private static final String SQL_FIND_ONE = "SELECT * FROM conference WHERE id = ?";
    private static final String SQL_INSERT = "INSERT INTO conference (event_date, language_id, actually_attended_count) VALUES (?,?,?)";
    private static final String SQL_UPDATE = "UPDATE conference SET event_date = ?, language_id = ?, actually_attended_count = ? WHERE id = ?";
    private static final String SQL_DELETE = "DELETE FROM conference WHERE id = ?";

    public static List<Conference> findAll(Connection connection) throws SQLException {
        return QueryHelper.findAll(connection, SQL_FIND_ALL, ConferenceDao::mapRow);
    }

    public static Optional<Conference> findOneById(Connection conn, int id) throws SQLException {
        return QueryHelper.findOne(conn, SQL_FIND_ONE,
                stmt -> stmt.setInt(1, id),
                ConferenceDao::mapRow);
    }

    public static void insert(Connection conn, Conference conference) throws SQLException {
        Optional<Integer> maybeId = QueryHelper.insert(conn, SQL_INSERT, makeStatementParametersSetter(conference));
        conference.setId(maybeId.get());
    }

    public static void update(Connection conn, Conference conference) throws SQLException {
        QueryHelper.update(conn, SQL_UPDATE, stmt -> {
            makeStatementParametersSetter(conference).setStatementParameters(stmt);
            stmt.setInt(4, conference.getId());
        });
    }

    public static void delete(Connection conn, Conference conference) throws SQLException {
        QueryHelper.delete(conn, SQL_DELETE,
                stmt -> stmt.setInt(1, conference.getId()));
    }

    private static StatementParametersSetter makeStatementParametersSetter(Conference conference) {
        return (stmt) -> {
            int i = 0;
            stmt.setDate(++i, Date.valueOf(conference.getEventDate()));
            stmt.setInt(++i, conference.getLanguage().getId());
            stmt.setInt(++i, conference.getActuallyAttendedCount());
        };
    }

    private static Conference mapRow(ResultSet rs) throws SQLException {
        Conference conference = new Conference();

        int i = 0;
        conference.setId(rs.getInt(++i));
        conference.setCreatedAt(rs.getObject(++i, OffsetDateTime.class));
        conference.setUpdatedAt(rs.getObject(++i, OffsetDateTime.class));
        conference.setEventDate(rs.getObject(++i, LocalDate.class));
        conference.setLanguage(Languages.get(rs.getInt(++i)));
        conference.setActuallyAttendedCount(rs.getInt(++i));

        return conference;
    }
}
