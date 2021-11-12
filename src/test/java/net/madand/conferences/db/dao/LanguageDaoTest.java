package net.madand.conferences.db.dao;

import net.madand.conferences.db.dao.LanguageDao;
import net.madand.conferences.entity.Language;
import net.madand.conferences.test.DbHelper;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.*;

public class LanguageDaoTest {
    private final DbHelper dbHelper;
    private final Connection connection;

    public LanguageDaoTest() throws SQLException, IOException {
        dbHelper = DbHelper.getInstance();
        connection = dbHelper.getConnection();
    }

    @Before
    public void setUp() throws IOException, SQLException {
        dbHelper.recreateDbTables();
    }

    @Test
    public void insert() throws SQLException {
        Language lang1 = Language.makeInstance("en", "English", true);
        int oldId = lang1.getId();
        LanguageDao.insert(connection, lang1);
        assertNotEquals("ID should change", oldId, lang1.getId());
    }

    @Test
    public void findAll() throws SQLException {
        List<Language> list1 = LanguageDao.findAll(connection);
        assertEquals("Initially empty", 0, list1.size());

        Language lang1 = Language.makeInstance("en", "English", true);
        LanguageDao.insert(connection, lang1);
        List<Language> list2 = LanguageDao.findAll(connection);
        assertEquals("Should find inserted row", 1, list2.size());
    }
}