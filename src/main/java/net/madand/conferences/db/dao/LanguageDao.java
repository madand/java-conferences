package net.madand.conferences.db.dao;

import net.madand.conferences.db.util.QueryHelper;
import net.madand.conferences.entity.Language;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class LanguageDao {
    private static final String FIND_ALL = "SELECT * FROM language ORDER BY is_default DESC, name";
    private static final String INSERT = "INSERT INTO language (code, name, is_default) VALUES (?,?,?)";

    private static Map<Integer, Language> languages;

    private LanguageDao() {}

    public static List<Language> findAll(Connection conn) throws SQLException {
        return QueryHelper.findAll(conn, FIND_ALL, LanguageDao::mapRow);
    }

    public static void insert(Connection conn, Language language) throws SQLException {
        Optional<Integer> maybeId = QueryHelper.insert(conn, INSERT,
                stmt -> {
                    stmt.setString(1, language.getCode());
                    stmt.setString(2, language.getName());
                    stmt.setBoolean(3, language.isDefault());
                }
        );
        language.setId(maybeId.get());
    }

    private static Language mapRow(ResultSet rs) throws SQLException {
        Language language = new Language();

        int i = 0;
        language.setId(rs.getInt(++i));
        language.setCode(rs.getString(++i));
        language.setName(rs.getString(++i));
        language.setDefault(rs.getBoolean(++i));

        return language;
    }
}
