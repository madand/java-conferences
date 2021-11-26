package net.madand.conferences.db.dao;

import net.madand.conferences.db.Fields;
import net.madand.conferences.db.util.Mapper;
import net.madand.conferences.db.util.QueryHelper;
import net.madand.conferences.db.util.StatementParametersSetter;
import net.madand.conferences.entity.Language;
import net.madand.conferences.entity.TalkSpeakerRequest;
import net.madand.conferences.entity.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

public class TalkSpeakerRequestDao {
    public static List<TalkSpeakerRequest> findAll(Connection connection, Language language) throws SQLException {
        final String SQL = "SELECT * FROM v_talk_speaker_request WHERE language_id = ? ORDER BY created_at DESC";
        return QueryHelper.findAll(connection, SQL,
                stmt -> stmt.setInt(1, language.getId()),
                (rs) -> {
                    TalkSpeakerRequest talkSpeakerRequest = makeRowMapper(connection, language).mapRow(rs);
                    talkSpeakerRequest.setTalkName(rs.getString(Fields.TALK_NAME));
                    talkSpeakerRequest.setConferenceName(rs.getString(Fields.CONFERENCE_NAME));
                    talkSpeakerRequest.setSpeakerName(rs.getString(Fields.SPEAKER_NAME));
                    talkSpeakerRequest.setSpeakerEmail(rs.getString(Fields.SPEAKER_EMAIL));
                    return talkSpeakerRequest;
                });
    }

    public static List<TalkSpeakerRequest> findAll(Connection connection, Language language, User speaker) throws SQLException {
        final String SQL = "SELECT * FROM v_talk_speaker_request WHERE language_id = ? AND speaker_id = ? ORDER BY created_at DESC";
        return QueryHelper.findAll(connection, SQL,
                stmt -> {
                    stmt.setInt(1, language.getId());
                    stmt.setInt(2, speaker.getId());
                },
                (rs) -> {
                    TalkSpeakerRequest talkSpeakerRequest = makeRowMapper(connection, language).mapRow(rs);
                    talkSpeakerRequest.setTalkName(rs.getString(Fields.TALK_NAME));
                    talkSpeakerRequest.setConferenceName(rs.getString(Fields.CONFERENCE_NAME));
                    talkSpeakerRequest.setSpeakerName(rs.getString(Fields.SPEAKER_NAME));
                    talkSpeakerRequest.setSpeakerEmail(rs.getString(Fields.SPEAKER_EMAIL));
                    return talkSpeakerRequest;
                });
    }

    public static Optional<TalkSpeakerRequest> findOne(Connection conn, int id) throws SQLException {
        final String SQL = "SELECT * FROM talk_speaker_request WHERE id = ?";
        return QueryHelper.findOne(conn, SQL,
                stmt -> stmt.setInt(1, id),
                rs ->{
                    TalkSpeakerRequest talkSpeakerRequest = makeRowMapper(conn, null).mapRow(rs);
                    talkSpeakerRequest.setSpeaker(UserDao.findOneById(conn, rs.getInt(Fields.SPEAKER_ID)).get());
                    return talkSpeakerRequest;
                });
    }

    public static void insert(Connection conn, TalkSpeakerRequest talkSpeakerRequest) throws SQLException {
        final String SQL = "INSERT INTO talk_speaker_request (talk_id, speaker_id) VALUES (?,?)";
        Optional<Integer> maybeId = QueryHelper.insert(conn, SQL, stmt -> {
            int i = 0;
            stmt.setInt(++i, talkSpeakerRequest.getTalk().getId());
            stmt.setInt(++i, talkSpeakerRequest.getSpeaker().getId());
        });
        talkSpeakerRequest.setId(maybeId.get());
    }

    public static void delete(Connection conn, TalkSpeakerRequest talkProposal) throws SQLException {
        final String SQL = "DELETE FROM talk_speaker_request WHERE id = ?";
        QueryHelper.delete(conn, SQL,
                stmt -> stmt.setInt(1, talkProposal.getId()));
    }

    private static Mapper<TalkSpeakerRequest> makeRowMapper(Connection conn, Language language) throws SQLException {
        return (rs) -> {
            TalkSpeakerRequest talk = new TalkSpeakerRequest();

            talk.setId(rs.getInt(Fields.ID));
            talk.setCreatedAt(rs.getObject(Fields.CREATED_AT, OffsetDateTime.class));
            talk.setTalkId(rs.getInt(Fields.TALK_ID));

            return talk;
        };
    }
}
