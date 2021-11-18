package net.madand.conferences.db.dao;

import net.madand.conferences.db.util.Mapper;
import net.madand.conferences.db.util.QueryHelper;
import net.madand.conferences.db.util.StatementParametersSetter;
import net.madand.conferences.entity.Conference;
import net.madand.conferences.entity.Talk;

import java.sql.*;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

public class TalkDao {
    private static final String SQL_FIND_ALL = "SELECT * FROM talk WHERE conference_id = ? ORDER BY start_time";
    private static final String SQL_FIND_ONE = "SELECT * FROM talk WHERE id = ?";
    private static final String SQL_INSERT = "INSERT INTO talk (conference_id, speaker_id, start_time, duration) VALUES (?,?,?,?)";
    private static final String SQL_UPDATE = "UPDATE talk SET conference_id = ?, speaker_id = ?, start_time = ?, duration = ? WHERE id = ?";
    private static final String SQL_DELETE = "DELETE FROM talk WHERE id = ?";

    public static List<Talk> findAll(Connection conn, Conference conference) throws SQLException {
        return QueryHelper.findAll(conn, SQL_FIND_ALL,
                stmt -> stmt.setInt(1, conference.getId()),
                makeRowMapper(conn));
    }

    public static Optional<Talk> findOne(Connection conn, int id) throws SQLException {
        return QueryHelper.findOne(conn, SQL_FIND_ONE,
                stmt -> stmt.setInt(1, id),
                makeRowMapper(conn));
    }

    public static void insert(Connection conn, Talk talk) throws SQLException {
        Optional<Integer> maybeId = QueryHelper.insert(conn, SQL_INSERT, makeStatementParametersSetter(talk));
        talk.setId(maybeId.get());
    }

    public static void update(Connection conn, Talk talk) throws SQLException {
        QueryHelper.update(conn, SQL_UPDATE, stmt -> {
            makeStatementParametersSetter(talk).setStatementParameters(stmt);
            stmt.setInt(5, talk.getId());
        });
    }

    public static void delete(Connection conn, Talk talk) throws SQLException {
        QueryHelper.delete(conn, SQL_DELETE,
                stmt -> stmt.setInt(1, talk.getId()));
    }

    private static StatementParametersSetter makeStatementParametersSetter(Talk talk) {
        return (stmt) -> {
            int i = 0;
            stmt.setInt(++i, talk.getConference().getId());
            stmt.setInt(++i, talk.getSpeaker().getId());
            stmt.setTime(++i, Time.valueOf(talk.getStartTime()));
            stmt.setInt(++i, talk.getDuration());
        };
    }

    private static Mapper<Talk> makeRowMapper(Connection conn) throws SQLException {

        return (rs) -> {
            Talk talk = new Talk();

            int i = 0;
            talk.setId(rs.getInt(++i));
            talk.setCreatedAt(rs.getObject(++i, OffsetDateTime.class));
            talk.setUpdatedAt(rs.getObject(++i, OffsetDateTime.class));
            talk.setConference(ConferenceDao.findOne(conn, rs.getInt(++i)).get());
            talk.setSpeaker(UserDao.findOneById(conn, rs.getInt(++i)).get());
            talk.setStartTime(rs.getObject(++i, LocalTime.class));
            talk.setDuration(rs.getInt(++i));
            talk.setEndTime(rs.getObject(++i, LocalTime.class));

            return talk;
        };
    }
}
