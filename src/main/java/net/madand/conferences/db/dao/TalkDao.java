package net.madand.conferences.db.dao;

import net.madand.conferences.db.Fields;
import net.madand.conferences.db.util.Mapper;
import net.madand.conferences.db.util.QueryHelper;
import net.madand.conferences.db.util.StatementParametersSetter;
import net.madand.conferences.entity.*;

import java.sql.*;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

public class TalkDao {
    private static final String FIND_ALL = "SELECT * FROM talk WHERE conference_id = ? ORDER BY start_time";
    private static final String FIND_ALL_LANG = "SELECT * FROM v_talk WHERE conference_id = ? AND language_id = ? ORDER BY start_time";
    private static final String FIND_ONE = "SELECT * FROM talk WHERE id = ?";
    private static final String INSERT = "INSERT INTO talk (conference_id, speaker_id, start_time, duration) VALUES (?,?,?,?)";
    private static final String UPDATE = "UPDATE talk SET conference_id = ?, speaker_id = ?, start_time = ?, duration = ? WHERE id = ?";
    private static final String DELETE = "DELETE FROM talk WHERE id = ?";

    public static List<Talk> findAll(Connection conn, Conference conference) throws SQLException {
        return QueryHelper.findAll(conn, FIND_ALL,
                stmt -> stmt.setInt(1, conference.getId()),
                makeRowMapper(conn));
    }

    public static List<Talk> findAll(Connection connection, Conference conference, Language language) throws SQLException {
        return QueryHelper.findAll(connection, FIND_ALL_LANG,
                stmt -> {
                    stmt.setInt(1, conference.getId());
                    stmt.setInt(2, language.getId());
                },
                rs -> {
                    Talk talk = makeRowMapper(connection).mapRow(rs);
                    talk.loadTranslation(TalkTranslationDao.mapRow(rs));
                    return talk;
                });
    }

    public static Optional<Talk> findOne(Connection conn, int id) throws SQLException {
        return QueryHelper.findOne(conn, FIND_ONE,
                stmt -> stmt.setInt(1, id),
                rs -> {
                    Talk talk = makeRowMapper(conn).mapRow(rs);
                    talk.setConference(
                            ConferenceDao.findOne(conn, rs.getInt(Fields.CONFERENCE_ID)).get());
                    return talk;
                });
    }

    public static Optional<Talk> findOne(Connection conn, int id, Language language) throws SQLException {
        final String SQL = "SELECT * FROM v_talk WHERE id = ? AND language_id = ?";
        return QueryHelper.findOne(conn, SQL,
                stmt -> {
                    stmt.setInt(1, id);
                    stmt.setInt(2, language.getId());
                },
                rs -> {
                    Talk talk = makeRowMapper(conn).mapRow(rs);
                    talk.loadTranslation(TalkTranslationDao.mapRow(rs));
                    return talk;
                });
    }

    public static void insert(Connection conn, Talk talk) throws SQLException {
        Optional<Integer> maybeId = QueryHelper.insert(conn, INSERT, makeStatementParametersSetter(talk));
        talk.setId(maybeId.get());
    }

    public static void update(Connection conn, Talk talk) throws SQLException {
        QueryHelper.update(conn, UPDATE, stmt -> {
            makeStatementParametersSetter(talk).setStatementParameters(stmt);
            stmt.setInt(5, talk.getId());
        });
    }

    public static void delete(Connection conn, Talk talk) throws SQLException {
        QueryHelper.delete(conn, DELETE,
                stmt -> stmt.setInt(1, talk.getId()));
    }

    public static void setSpeaker(Connection conn, int talkId, int speakerId) throws SQLException {
         final String SQL = "UPDATE talk SET speaker_id = ? WHERE id = ?";
        QueryHelper.update(conn, SQL, stmt -> {
            stmt.setInt(1, speakerId);
            stmt.setInt(2, talkId);
        });
    }

    private static StatementParametersSetter makeStatementParametersSetter(Talk talk) {
        return (stmt) -> {
            int i = 0;
            stmt.setInt(++i, talk.getConference().getId());

            User speaker = talk.getSpeaker();
            if (speaker != null) {
                stmt.setInt(++i, speaker.getId());
            } else {
                stmt.setNull(++i, Types.INTEGER);
            }

            stmt.setTime(++i, Time.valueOf(talk.getStartTime()));
            stmt.setInt(++i, talk.getDuration());
        };
    }

    private static Mapper<Talk> makeRowMapper(Connection conn) throws SQLException {
        return (rs) -> {
            Talk talk = new Talk();

            talk.setId(rs.getInt(Fields.ID));
            talk.setCreatedAt(rs.getObject(Fields.CREATED_AT, OffsetDateTime.class));
            talk.setUpdatedAt(rs.getObject(Fields.UPDATED_AT, OffsetDateTime.class));

            talk.setStartTime(rs.getObject(Fields.START_TIME, LocalTime.class));
            talk.setDuration(rs.getInt(Fields.DURATION));
            talk.setEndTime(rs.getObject(Fields.END_TIME, LocalTime.class));

            final int speakerId = rs.getInt(Fields.SPEAKER_ID);
            if (speakerId > 0) {
                talk.setSpeaker(UserDao.findOneById(conn, speakerId).get());
            }

            return talk;
        };
    }
}
