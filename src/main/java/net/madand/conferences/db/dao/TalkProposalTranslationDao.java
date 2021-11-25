package net.madand.conferences.db.dao;

import net.madand.conferences.db.Fields;
import net.madand.conferences.db.util.QueryBuilder;
import net.madand.conferences.db.util.QueryHelper;
import net.madand.conferences.entity.Language;
import net.madand.conferences.entity.TalkProposal;
import net.madand.conferences.entity.TalkProposalTranslation;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class TalkProposalTranslationDao {
    public static Optional<TalkProposalTranslation> findOne(Connection conn, TalkProposal talkProposal, Language language) throws SQLException {
        final String SQL = new QueryBuilder("new_talk_proposal_translation")
                .where("new_talk_proposal_id = ?", "language_id = ?")
                .buildSelect();
        return QueryHelper.findOne(conn, SQL,
                stmt -> {
                    stmt.setInt(1, talkProposal.getId());
                    stmt.setInt(2, language.getId());
                },
                rs -> {
                    TalkProposalTranslation translation = TalkProposalTranslationDao.mapRow(rs);
                    translation.setTalkProposal(talkProposal);
                    translation.setLanguage(language);
                    return translation;
                });
    }

    public static void insert(Connection conn, TalkProposalTranslation translation) throws SQLException {
        final String SQL = "INSERT INTO new_talk_proposal_translation (new_talk_proposal_id, language_id, name, description) VALUES (?,?,?,?)";
        QueryHelper.insert(conn, SQL, stmt -> {
            int i = 0;
            stmt.setInt(++i, translation.getTalkProposal().getId());
            stmt.setInt(++i, translation.getLanguage().getId());
            stmt.setString(++i, translation.getName());
            stmt.setString(++i, translation.getDescription());
        });
    }

    public static void update(Connection conn, TalkProposalTranslation translation) throws SQLException {
        final String SQL = "UPDATE new_talk_proposal_translation SET name = ?, description = ? WHERE new_talk_proposal_id = ? AND language_id = ?";
        QueryHelper.update(conn, SQL, stmt -> {
            int i = 0;
            stmt.setString(++i, translation.getName());
            stmt.setString(++i, translation.getDescription());
            stmt.setInt(++i, translation.getTalkProposal().getId());
            stmt.setInt(++i, translation.getLanguage().getId());
        });
    }

    public static TalkProposalTranslation mapRow(ResultSet rs) throws SQLException {
        TalkProposalTranslation translation = new TalkProposalTranslation();
        translation.setName(rs.getString(Fields.NAME));
        translation.setDescription(rs.getString(Fields.DESCRIPTION));
        return translation;
    }
}
