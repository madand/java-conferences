package net.madand.conferences.db.dao;

import net.madand.conferences.db.Fields;
import net.madand.conferences.db.util.QueryHelper;
import net.madand.conferences.db.util.StatementParametersSetter;
import net.madand.conferences.entity.Language;
import net.madand.conferences.entity.Talk;
import net.madand.conferences.entity.TalkTranslation;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class TalkTranslationDao {
    private static final String FIND_ONE = "SELECT * FROM talk_translation WHERE talk_id = ? AND language_id = ?";
    private static final String INSERT = "INSERT INTO talk_translation (talk_id, language_id, name, description) VALUES (?,?,?,?)";
    private static final String UPDATE = "UPDATE talk_translation SET " +
            "talk_id = ?, language_id = ?, name = ?, description = ? " +
            "WHERE talk_id = ? AND language_id = ?";

    public static Optional<TalkTranslation> findOne(Connection conn, Talk talk, Language language) throws SQLException {
        return QueryHelper.findOne(conn, FIND_ONE, stmt -> {
                            stmt.setInt(1, talk.getId());
                            stmt.setInt(2, language.getId());
                        },
                        TalkTranslationDao::mapRow)
                .map(translation -> {
                    translation.setTalk(talk);
                    translation.setLanguage(language);
                    return translation;
                });
    }

    public static void insert(Connection conn, TalkTranslation talkTranslation) throws SQLException {
        QueryHelper.insert(conn, INSERT, makeInsertUpdateParametersSetter(talkTranslation));
    }

    public static void update(Connection conn, TalkTranslation talkTranslation) throws SQLException {
        QueryHelper.update(conn, UPDATE, stmt -> {
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

    public static TalkTranslation mapRow(ResultSet rs) throws SQLException {
        TalkTranslation talkTranslation = new TalkTranslation();

        talkTranslation.setName(rs.getString(Fields.NAME));
        talkTranslation.setDescription(rs.getString(Fields.DESCRIPTION));

        return talkTranslation;
    }
}
