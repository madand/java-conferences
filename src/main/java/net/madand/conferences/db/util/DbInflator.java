package net.madand.conferences.db.util;

import net.madand.conferences.db.dao.LanguageDao;
import net.madand.conferences.entity.Language;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * This class has methods for populating the database with example data.
 * Useful for automated tests and demonstration purposes.
 */
public class DbInflator {
    private Connection connection;

    public DbInflator(Connection connection) {
        this.connection = connection;
    }

    public void inflateAll() throws SQLException {
        insertLanguages();
    }

    public void insertLanguages() throws SQLException {
        Language en = Language.makeInstance("en", "English", true);
        LanguageDao.insert(connection, en);

        Language uk = Language.makeInstance("uk", "Ukrainian", false);
        LanguageDao.insert(connection, uk);
    }
}
