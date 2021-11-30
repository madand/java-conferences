package net.madand.conferences.db.dao;

import net.madand.conferences.db.Fields;
import net.madand.conferences.db.util.QueryBuilder;
import net.madand.conferences.db.util.QueryHelper;
import net.madand.conferences.db.util.StatementParametersSetter;
import net.madand.conferences.db.web.QueryOptions;
import net.madand.conferences.entity.Conference;
import net.madand.conferences.entity.Language;
import net.madand.conferences.entity.User;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

public class ConferenceDao {
    private static final Logger log = Logger.getLogger(ConferenceDao.class);

    private static final String FIND_ALL = "SELECT * FROM conference ORDER BY id";
    private static final String FIND_ALL_LANG = "SELECT * FROM v_conference WHERE language_id = ? ORDER BY event_date";
    private static final String FIND_ALL_FOR_USER = "SELECT * FROM v_conference_with_attendee WHERE language_id = ? AND  ORDER BY event_date";
    private static final String FIND_ONE = "SELECT * FROM conference WHERE id = ?";
    private static final String FIND_ONE_LANG = "SELECT * FROM v_conference WHERE id = ? AND language_id = ?";
    
    private static final String INSERT = "INSERT INTO conference (event_date, actually_attended_count) VALUES (?,?)";
    private static final String UPDATE = "UPDATE conference SET event_date = ?, actually_attended_count = ? WHERE id = ?";
    private static final String DELETE = "DELETE FROM conference WHERE id = ?";

    public static List<Conference> findAll(Connection connection) throws SQLException {
        return QueryHelper.findAll(connection, FIND_ALL, ConferenceDao::mapRow);
    }

    public static List<Conference> findAll(Connection connection, Language language) throws SQLException {
        final String SQL = new QueryBuilder("v_conference")
                .where("language_id = ?")
                .orderBy("event_date")
                .buildSelect();
        return QueryHelper.findAll(connection, SQL,
                stmt -> stmt.setInt(1, language.getId()),
                ConferenceDao::mapRowWithTranslation);
    }

    public static List<Conference> findAllUpcoming(Connection connection, Language language, Optional<User> user,
                                                   QueryOptions queryOptions) throws SQLException {
        final QueryBuilder queryBuilder = new QueryBuilder("v_conference t")
                .select("t.*, ca.user_id as attendee_id")
                .leftJoin("conference_attendee ca ON ca.conference_id = t.id AND ca.user_id = ?")
                .where("language_id = ? AND event_date >= CURRENT_DATE")
                .orderBy("event_date DESC");

        int attendeeId = user.map(User::getId).orElse(-1);
        final StatementParametersSetter paramsSetter = stmt -> {
            stmt.setInt(1, attendeeId);
            stmt.setInt(2, language.getId());
        };

        final String SQL_TOTAL_COUNT = queryBuilder.buildCountTotal();
        queryOptions.getPagination().setTotalItemsCount(QueryHelper.count(connection, SQL_TOTAL_COUNT, paramsSetter));

        queryOptions.applyTo(queryBuilder);
        final String SQL_SELECT = queryBuilder.buildSelect();
        return QueryHelper.findAll(connection, SQL_SELECT, paramsSetter,
                rs -> {
                    Conference conference = ConferenceDao.mapRowWithTranslation(rs);
                    conference.setCurrentUserAttending(rs.getInt(Fields.ATTENDEE_ID) > 0);
                    return conference;
                });
    }

    public static List<Conference> findAllPast(Connection connection, Language language, Optional<User> user,
                                               QueryOptions queryOptions) throws SQLException {
        final QueryBuilder queryBuilder = new QueryBuilder("v_conference t")
                .select("t.*, ca.user_id as attendee_id")
                .leftJoin("conference_attendee ca ON ca.conference_id = t.id AND ca.user_id = ?")
                .where("language_id = ? AND event_date < CURRENT_DATE")
                .orderBy("event_date DESC");

        int attendeeId = user.map(User::getId).orElse(-1);
        final StatementParametersSetter paramsSetter = stmt -> {
            stmt.setInt(1, attendeeId);
            stmt.setInt(2, language.getId());
        };

        final String SQL_TOTAL_COUNT = queryBuilder.buildCountTotal();
        queryOptions.getPagination().setTotalItemsCount(QueryHelper.count(connection, SQL_TOTAL_COUNT, paramsSetter));

        queryOptions.applyTo(queryBuilder);
        final String SQL_SELECT = queryBuilder.buildSelect();
        return QueryHelper.findAll(connection, SQL_SELECT, paramsSetter,
                rs -> {
                    Conference conference = ConferenceDao.mapRowWithTranslation(rs);
                    conference.setCurrentUserAttending(rs.getInt(Fields.ATTENDEE_ID) > 0);
                    return conference;
                });
    }

    public static List<Conference> findAll(Connection connection, Language language, Optional<User> user,
                                           QueryOptions queryOptions) throws SQLException {
        final QueryBuilder queryBuilder = new QueryBuilder("v_conference t")
                .select("t.*, ca.user_id as attendee_id")
                .leftJoin("conference_attendee ca ON ca.conference_id = t.id AND ca.user_id = ?")
                .where("language_id = ?")
                .orderBy("event_date");

        int attendeeId = user.map(User::getId).orElse(-1);
        final StatementParametersSetter paramsSetter = stmt -> {
            stmt.setInt(1, attendeeId);
            stmt.setInt(2, language.getId());
        };

        final String SQL_TOTAL_COUNT = queryBuilder.buildCountTotal();
        log.debug(SQL_TOTAL_COUNT);
        queryOptions.getPagination().setTotalItemsCount(QueryHelper.count(connection, SQL_TOTAL_COUNT, paramsSetter));

        queryOptions.applyTo(queryBuilder);
        final String SQL_SELECT = queryBuilder.buildSelect();
        log.debug(SQL_SELECT);
        return QueryHelper.findAll(connection, SQL_SELECT, paramsSetter,
                rs -> {
                    Conference conference = ConferenceDao.mapRowWithTranslation(rs);
                    conference.setCurrentUserAttending(rs.getInt(Fields.ATTENDEE_ID) > 0);
                    return conference;
                });
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
