package net.madand.conferences.db.dao;

import net.madand.conferences.db.Fields;
import net.madand.conferences.db.util.QueryHelper;
import net.madand.conferences.db.util.StatementParametersSetter;
import net.madand.conferences.entity.Conference;
import net.madand.conferences.entity.ConferenceTranslation;
import net.madand.conferences.entity.Language;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class ConferenceTranslationDao {
    private static final String SQL_FIND_ONE = "SELECT * FROM conference_translation WHERE conference_id = ? AND language_id = ?";
    private static final String SQL_INSERT = "INSERT INTO conference_translation (conference_id, language_id, name, description, location) VALUES (?,?,?,?,?)";
    private static final String SQL_UPDATE = "UPDATE conference_translation SET " +
            "conference_id = ?, language_id = ?, name = ?, description = ?, location = ? " +
            "WHERE conference_id = ? AND language_id = ?";
    private static final String SQL_DELETE = "DELETE FROM conference_translation WHERE conference_id = ? AND language_id = ?";

    public static Optional<ConferenceTranslation> findOne(Connection conn, Conference conference, Language language) throws SQLException {
        final Optional<ConferenceTranslation> translation = QueryHelper.findOne(conn, SQL_FIND_ONE, stmt -> {
                    stmt.setInt(1, conference.getId());
                    stmt.setInt(2, language.getId());
                },
                ConferenceTranslationDao::mapRow);

        if (translation.isPresent()) {
            translation.get().setConference(conference);
            translation.get().setLanguage(language);
        }

        return translation;
    }

    public static void insert(Connection conn, ConferenceTranslation conferenceTranslation) throws SQLException {
        QueryHelper.insert(conn, SQL_INSERT, makeInsertUpdateParametersSetter(conferenceTranslation));
    }

    public static void update(Connection conn, ConferenceTranslation conferenceTranslation) throws SQLException {
        QueryHelper.update(conn, SQL_UPDATE, stmt -> {
            makeInsertUpdateParametersSetter(conferenceTranslation).setStatementParameters(stmt);
            stmt.setInt(6, conferenceTranslation.getConference().getId());
            stmt.setInt(7, conferenceTranslation.getLanguage().getId());
        });
    }

    private static StatementParametersSetter makeInsertUpdateParametersSetter(ConferenceTranslation conferenceTranslation) {
        return (stmt) -> {
            int i = 0;
            stmt.setInt(++i, conferenceTranslation.getConference().getId());
            stmt.setInt(++i, conferenceTranslation.getLanguage().getId());
            stmt.setString(++i, conferenceTranslation.getName());
            stmt.setString(++i, conferenceTranslation.getDescription());
            stmt.setString(++i, conferenceTranslation.getLocation());
        };
    }

    public static ConferenceTranslation mapRow(ResultSet rs) throws SQLException {
        ConferenceTranslation conferenceTranslation = new ConferenceTranslation();

        conferenceTranslation.setName(rs.getString(Fields.NAME));
        conferenceTranslation.setDescription(rs.getString(Fields.DESCRIPTION));
        conferenceTranslation.setLocation(rs.getString(Fields.LOCATION));

        return conferenceTranslation;
    }
}
