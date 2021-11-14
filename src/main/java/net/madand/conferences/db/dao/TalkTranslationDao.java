package net.madand.conferences.db.dao;

import net.madand.conferences.db.util.Mapper;
import net.madand.conferences.db.util.QueryHelper;
import net.madand.conferences.db.util.StatementParametersSetter;
import net.madand.conferences.entity.Talk;
import net.madand.conferences.entity.TalkTranslation;
import net.madand.conferences.entity.Language;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

public class TalkTranslationDao {
    private static final String SQL_FIND_ONE = "SELECT * FROM talk_translation WHERE talk_id = ? AND language_id = ?";
    private static final String SQL_INSERT = "INSERT INTO talk_translation (talk_id, language_id, name, description) VALUES (?,?,?,?)";
    private static final String SQL_UPDATE = "UPDATE talk_translation SET " +
            "talk_id = ?, language_id = ?, name = ?, description = ? " +
            "WHERE talk_id = ? AND language_id = ?";
    private static final String SQL_DELETE = "DELETE FROM talk_translation WHERE talk_id = ? AND language_id = ?";

    public static Optional<TalkTranslation> findOne(Connection conn, Talk talk, Language language) throws SQLException {
        return QueryHelper.findOne(conn, SQL_FIND_ONE, stmt -> {
                    stmt.setInt(1, talk.getId());
                    stmt.setInt(2, language.getId());
                },
                makeRowMapper(talk, language));
    }

    public static void insert(Connection conn, TalkTranslation talkTranslation) throws SQLException {
        QueryHelper.insert(conn, SQL_INSERT, makeInsertUpdateParametersSetter(talkTranslation));
    }

    public static void update(Connection conn, TalkTranslation talkTranslation) throws SQLException {
        QueryHelper.update(conn, SQL_UPDATE, stmt -> {
            makeInsertUpdateParametersSetter(talkTranslation).setStatementParameters(stmt);
            stmt.setInt(5, talkTranslation.getTalk().getId());
            stmt.setInt(6, talkTranslation.getLanguage().getId());
        });
    }

    private static StatementParametersSetter makeInsertUpdateParametersSetter(TalkTranslation talkTranslation) {
        return (stmt) -> {
            int i = 0;
            stmt.setInt(++i, talkTranslation.getTalk().getId());
            stmt.setInt(++i, talkTranslation.getLanguage().getId());
            stmt.setString(++i, talkTranslation.getName());
            stmt.setString(++i, talkTranslation.getDescription());
        };
    }

    private static Mapper<TalkTranslation> makeRowMapper(Talk talk, Language language) throws SQLException {
        return (rs) -> {
            TalkTranslation talkTranslation = new TalkTranslation();
            talkTranslation.setTalk(talk);
            talkTranslation.setLanguage(language);

            int i = 2; // Skip the first two rows, with talk_id and language_id.
            talkTranslation.setName(rs.getString(++i));
            talkTranslation.setDescription(rs.getString(++i));

            return talkTranslation;
        };
    }
}
