package net.madand.conferences.db.dao;

import net.madand.conferences.db.Fields;
import net.madand.conferences.db.util.Mapper;
import net.madand.conferences.db.util.QueryHelper;
import net.madand.conferences.db.util.StatementParametersSetter;
import net.madand.conferences.entity.Conference;
import net.madand.conferences.entity.Language;
import net.madand.conferences.entity.Talk;
import net.madand.conferences.entity.TalkTranslation;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

public class TalkDao {
    private static final String SQL_FIND_ALL = "SELECT * FROM talk WHERE conference_id = ? ORDER BY start_time";
    private static final String SQL_FIND_ALL_LANG = "SELECT * FROM v_talk WHERE conference_id = ? AND language_id = ? ORDER BY start_time";
    private static final String SQL_FIND_ONE = "SELECT * FROM talk WHERE id = ?";
    private static final String SQL_INSERT = "INSERT INTO talk (conference_id, speaker_id, start_time, duration) VALUES (?,?,?,?)";
    private static final String SQL_UPDATE = "UPDATE talk SET conference_id = ?, speaker_id = ?, start_time = ?, duration = ? WHERE id = ?";
    private static final String SQL_DELETE = "DELETE FROM talk WHERE id = ?";

    public static List<Talk> findAll(Connection conn, Conference conference) throws SQLException {
        return QueryHelper.findAll(conn, SQL_FIND_ALL,
                stmt -> stmt.setInt(1, conference.getId()),
                makeRowMapper(conn));
    }

    public static List<Talk> findAll(Connection connection, Conference conference, Language language) throws SQLException {
        return QueryHelper.findAll(connection, SQL_FIND_ALL_LANG,
                stmt -> {
                    stmt.setInt(1, conference.getId());
                    stmt.setInt(2, language.getId());
                },
                (rs) -> {
                    Talk talk = makeRowMapper(connection).mapRow(rs);

                    TalkTranslation translation = TalkTranslationDao.mapRow(rs);
                    talk.setName(translation.getName());
                    talk.setDescription(translation.getDescription());

                    return talk;
                });
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

            talk.setId(rs.getInt(Fields.ID));
            talk.setCreatedAt(rs.getObject(Fields.CREATED_AT, OffsetDateTime.class));
            talk.setUpdatedAt(rs.getObject(Fields.UPDATED_AT, OffsetDateTime.class));

            talk.setStartTime(rs.getObject(Fields.START_TIME, LocalTime.class));
            talk.setDuration(rs.getInt(Fields.DURATION));
            talk.setEndTime(rs.getObject(Fields.END_TIME, LocalTime.class));

            talk.setSpeaker(UserDao.findOneById(conn, rs.getInt(Fields.SPEAKER_ID)).get());

            return talk;
        };
    }
}
