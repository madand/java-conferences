package net.madand.conferences.db.dao;

import net.madand.conferences.db.Fields;
import net.madand.conferences.db.util.QueryHelper;
import net.madand.conferences.db.util.StatementParametersSetter;
import net.madand.conferences.entity.Conference;
import net.madand.conferences.entity.Language;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

public class ConferenceDao {
    private static final String FIND_ALL = "SELECT * FROM conference ORDER BY id";
    private static final String FIND_ALL_LANG = "SELECT * FROM v_conference WHERE language_id = ? ORDER BY event_date";
    private static final String FIND_ONE = "SELECT * FROM conference WHERE id = ?";
    private static final String FIND_ONE_LANG = "SELECT * FROM v_conference WHERE id = ? AND language_id = ?";
    
    private static final String INSERT = "INSERT INTO conference (event_date, actually_attended_count) VALUES (?,?)";
    private static final String UPDATE = "UPDATE conference SET event_date = ?, actually_attended_count = ? WHERE id = ?";
    private static final String DELETE = "DELETE FROM conference WHERE id = ?";

    public static List<Conference> findAll(Connection connection) throws SQLException {
        return QueryHelper.findAll(connection, FIND_ALL, ConferenceDao::mapRow);
    }

    public static List<Conference> findAll(Connection connection, Language language) throws SQLException {
        return QueryHelper.findAll(connection, FIND_ALL_LANG,
                stmt -> stmt.setInt(1, language.getId()),
                ConferenceDao::mapRowWithTranslation);
    }

    public static Optional<Conference> findOne(Connection conn, int id, Language language) throws SQLException {
        return QueryHelper.findOne(conn, FIND_ONE_LANG,
                stmt -> {
                    stmt.setInt(1, id);
                    stmt.setInt(2, language.getId());
                },
                ConferenceDao::mapRowWithTranslation);
    }

    public static Optional<Conference> findOne(Connection conn, int id) throws SQLException {
        return QueryHelper.findOne(conn, FIND_ONE,
                stmt -> stmt.setInt(1, id),
                ConferenceDao::mapRow);
    }

    public static void insert(Connection conn, Conference conference) throws SQLException {
        QueryHelper.insert(conn, INSERT, makeStatementParametersSetter(conference))
                .ifPresent(conference::setId);
    }

    public static void update(Connection conn, Conference conference) throws SQLException {
        QueryHelper.update(conn, UPDATE, stmt -> {
            makeStatementParametersSetter(conference).setStatementParameters(stmt);
            stmt.setInt(3, conference.getId());
        });
    }

    public static void delete(Connection conn, Conference conference) throws SQLException {
        QueryHelper.delete(conn, DELETE,
                stmt -> stmt.setInt(1, conference.getId()));
    }

    private static StatementParametersSetter makeStatementParametersSetter(Conference conference) {
        return (stmt) -> {
            int i = 0;
            stmt.setDate(++i, Date.valueOf(conference.getEventDate()));
            stmt.setInt(++i, conference.getActuallyAttendedCount());
        };
    }

    private static Conference mapRow(ResultSet rs) throws SQLException {
        Conference conference = new Conference();

        conference.setId(rs.getInt(Fields.ID));
        conference.setCreatedAt(rs.getObject(Fields.CREATED_AT, OffsetDateTime.class));
        conference.setUpdatedAt(rs.getObject(Fields.UPDATED_AT, OffsetDateTime.class));
        conference.setEventDate(rs.getObject(Fields.EVENT_DATE, LocalDate.class));
        conference.setActuallyAttendedCount(rs.getInt(Fields.ACTUALLY_ATTENDED_COUNT));

        return conference;
    }

    private static Conference mapRowWithTranslation(ResultSet rs) throws SQLException {
        Conference conference = mapRow(rs);
        conference.loadTranslation(ConferenceTranslationDao.mapRow(rs));
        return conference;
    }
}
