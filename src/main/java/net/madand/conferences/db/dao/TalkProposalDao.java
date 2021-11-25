package net.madand.conferences.db.dao;

import net.madand.conferences.db.Fields;
import net.madand.conferences.db.util.Mapper;
import net.madand.conferences.db.util.QueryHelper;
import net.madand.conferences.db.util.StatementParametersSetter;
import net.madand.conferences.entity.Language;
import net.madand.conferences.entity.TalkProposal;
import net.madand.conferences.entity.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

public class TalkProposalDao {
    public static List<TalkProposal> findAll(Connection connection, Language language) throws SQLException {
        final String SQL = "SELECT * FROM v_new_talk_proposal WHERE language_id = ? ORDER BY conference_id, created_at DESC";
        return QueryHelper.findAll(connection, SQL,
                stmt -> stmt.setInt(1, language.getId()),
                (rs) -> {
                    TalkProposal talk = makeRowMapper(connection, language).mapRow(rs);
                    talk.loadTranslation(TalkProposalTranslationDao.mapRow(rs));
                    return talk;
                });
    }

    public static List<TalkProposal> findAll(Connection connection, Language language, User speaker) throws SQLException {
        final String SQL = "SELECT * FROM v_new_talk_proposal WHERE language_id = ? AND speaker_id = ? ORDER BY conference_id, created_at DESC";
        return QueryHelper.findAll(connection, SQL,
                stmt -> {
                    stmt.setInt(1, language.getId());
                    stmt.setInt(2, speaker.getId());
                },
                (rs) -> {
                    TalkProposal talk = makeRowMapper(connection, language).mapRow(rs);
                    talk.loadTranslation(TalkProposalTranslationDao.mapRow(rs));
                    return talk;
                });
    }

    public static Optional<TalkProposal> findOne(Connection conn, int id) throws SQLException {
        final String SQL = "SELECT * FROM new_talk_proposal WHERE id = ?";
        return QueryHelper.findOne(conn, SQL,
                stmt -> stmt.setInt(1, id),
                makeRowMapper(conn, null));
    }

    public static void insert(Connection conn, TalkProposal talkProposal) throws SQLException {
        final String SQL = "INSERT INTO new_talk_proposal (conference_id, speaker_id, duration) VALUES (?,?,?)";
        Optional<Integer> maybeId = QueryHelper.insert(conn, SQL, makeStatementParametersSetter(talkProposal));
        talkProposal.setId(maybeId.get());
    }

    public static void update(Connection conn, TalkProposal talk) throws SQLException {
        final String SQL = "UPDATE new_talk_proposal SET conference_id = ?, speaker_id = ?, duration = ? WHERE id = ?";
        QueryHelper.update(conn, SQL, stmt -> {
            makeStatementParametersSetter(talk).setStatementParameters(stmt);
            stmt.setInt(4, talk.getId());
        });
    }

    public static void delete(Connection conn, TalkProposal talkProposal) throws SQLException {
        final String SQL = "DELETE FROM new_talk_proposal WHERE id = ?";
        QueryHelper.delete(conn, SQL,
                stmt -> stmt.setInt(1, talkProposal.getId()));
    }

    private static StatementParametersSetter makeStatementParametersSetter(TalkProposal talkProposal) {
        return (stmt) -> {
            int i = 0;
            stmt.setInt(++i, talkProposal.getConference().getId());
            stmt.setInt(++i, talkProposal.getSpeaker().getId());
            stmt.setInt(++i, talkProposal.getDuration());
        };
    }

    private static Mapper<TalkProposal> makeRowMapper(Connection conn, Language language) throws SQLException {
        return (rs) -> {
            TalkProposal talk = new TalkProposal();

            talk.setId(rs.getInt(Fields.ID));
            talk.setCreatedAt(rs.getObject(Fields.CREATED_AT, OffsetDateTime.class));
            talk.setUpdatedAt(rs.getObject(Fields.UPDATED_AT, OffsetDateTime.class));
            talk.setDuration(rs.getInt(Fields.DURATION));
            talk.setSpeaker(UserDao.findOneById(conn, rs.getInt(Fields.SPEAKER_ID)).get());

            if (language == null) {
                talk.setConference(ConferenceDao.findOne(conn, rs.getInt(Fields.CONFERENCE_ID)).get());
            } else {
                talk.setConference(ConferenceDao.findOne(conn, rs.getInt(Fields.CONFERENCE_ID), language).get());
            }

            return talk;
        };
    }
}
