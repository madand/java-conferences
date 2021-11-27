package net.madand.conferences.db.dao;

import net.madand.conferences.db.Fields;
import net.madand.conferences.db.util.Mapper;
import net.madand.conferences.db.util.QueryHelper;
import net.madand.conferences.entity.Language;
import net.madand.conferences.entity.Talk;
import net.madand.conferences.entity.TalkSpeakerProposal;
import net.madand.conferences.entity.User;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

public class TalkSpeakerProposalDao {
    public static List<TalkSpeakerProposal> findAll(Connection connection, Language language) throws SQLException {
        final String SQL = "SELECT * FROM v_talk_speaker_proposal WHERE language_id = ? ORDER BY created_at DESC";
        return QueryHelper.findAll(connection, SQL,
                stmt -> stmt.setInt(1, language.getId()),
                TalkSpeakerProposalDao::mapViewRow);
    }

    public static List<TalkSpeakerProposal> findAll(Connection connection, Language language, User speaker) throws SQLException {
        final String SQL = "SELECT * FROM v_talk_speaker_proposal WHERE language_id = ? AND speaker_id = ? ORDER BY created_at DESC";
        return QueryHelper.findAll(connection, SQL,
                stmt -> {
                    stmt.setInt(1, language.getId());
                    stmt.setInt(2, speaker.getId());
                },
                TalkSpeakerProposalDao::mapViewRow);
    }

    public static Optional<TalkSpeakerProposal> findOne(Connection conn, int id) throws SQLException {
        final String SQL = "SELECT * FROM talk_speaker_proposal WHERE id = ?";
        return QueryHelper.findOne(conn, SQL,
                stmt -> stmt.setInt(1, id),
                rs ->{
                    TalkSpeakerProposal talkSpeakerProposal = TalkSpeakerProposalDao.mapRow(rs);

                    talkSpeakerProposal.setSpeaker(UserDao.findOneById(conn, rs.getInt(Fields.SPEAKER_ID)).get());
                    talkSpeakerProposal.setModerator(UserDao.findOneById(conn, rs.getInt(Fields.MODERATOR_ID)).get());

                    return talkSpeakerProposal;
                });
    }

    public static void insert(Connection conn, TalkSpeakerProposal talkSpeakerProposal) throws SQLException {
        final String SQL = "INSERT INTO talk_speaker_proposal (talk_id, speaker_id, moderator_id) VALUES (?,?,?)";
        Optional<Integer> maybeId = QueryHelper.insert(conn, SQL, stmt -> {
            int i = 0;
            stmt.setInt(++i, talkSpeakerProposal.getTalk().getId());
            stmt.setInt(++i, talkSpeakerProposal.getSpeaker().getId());
            stmt.setInt(++i, talkSpeakerProposal.getModerator().getId());
        });
        talkSpeakerProposal.setId(maybeId.get());
    }

    public static void delete(Connection conn, TalkSpeakerProposal talkProposal) throws SQLException {
        final String SQL = "DELETE FROM talk_speaker_proposal WHERE id = ?";
        QueryHelper.delete(conn, SQL,
                stmt -> stmt.setInt(1, talkProposal.getId()));
    }

    private static TalkSpeakerProposal mapRow(ResultSet rs) throws SQLException {
        TalkSpeakerProposal talkSpeakerProposal = new TalkSpeakerProposal();

        talkSpeakerProposal.setId(rs.getInt(Fields.ID));
        talkSpeakerProposal.setCreatedAt(rs.getObject(Fields.CREATED_AT, OffsetDateTime.class));
        talkSpeakerProposal.setTalkId(rs.getInt(Fields.TALK_ID));

        return talkSpeakerProposal;
    }

    private static TalkSpeakerProposal mapViewRow(ResultSet rs) throws SQLException {
        TalkSpeakerProposal talkSpeakerProposal = TalkSpeakerProposalDao.mapRow(rs);

        talkSpeakerProposal.setTalkName(rs.getString(Fields.TALK_NAME));
        talkSpeakerProposal.setConferenceName(rs.getString(Fields.CONFERENCE_NAME));
        talkSpeakerProposal.setSpeakerName(rs.getString(Fields.SPEAKER_NAME));
        talkSpeakerProposal.setSpeakerEmail(rs.getString(Fields.SPEAKER_EMAIL));
        talkSpeakerProposal.setModeratorName(rs.getString(Fields.MODERATOR_NAME));
        talkSpeakerProposal.setModeratorEmail(rs.getString(Fields.MODERATOR_EMAIL));

        return talkSpeakerProposal;
    }
}
