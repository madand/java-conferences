package net.madand.conferences.db.dao;

import net.madand.conferences.entity.Conference;
import net.madand.conferences.entity.ConferenceTranslation;
import net.madand.conferences.entity.Language;
import net.madand.conferences.l10n.Languages;
import net.madand.conferences.test.DbHelper;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

import static org.junit.Assert.*;

public class ConferenceTranslationDaoTest {
    private final DbHelper dbHelper;
    private final Connection connection;
    private Conference conference;
    private Language language;

    public ConferenceTranslationDaoTest() throws SQLException, IOException {
        dbHelper = DbHelper.getInstance();
        connection = dbHelper.getConnection();
    }

    @Before
    public void setUp() throws IOException, SQLException {
        dbHelper.recreateDbTables();

        Language en = Language.makeInstance("en", "English", true);
        LanguageDao.insert(connection, en);
        Languages.add(en);
        language = en;

        Conference conference = Conference.makeInstance(LocalDate.now(), Languages.getDefaultLanguage(), 0);
        ConferenceDao.insert(connection, conference);
        this.conference = conference;
    }

    @Test
    public void findOne() throws SQLException {
        ConferenceTranslation conferenceTranslation1 = ConferenceTranslation.makeInstance(conference, language,
                "JConf 2021", "Descr", "Lviv");
        ConferenceTranslationDao.insert(connection, conferenceTranslation1);
        ConferenceTranslation conferenceTranslation2 = ConferenceTranslationDao.findOne(connection, conference, language).get();
        assertNotNull("Should successfully find", conferenceTranslation2);
        assertTrue("Should have the same fields", compareConferenceTranslations(conferenceTranslation1, conferenceTranslation2));
    }

    @Test
    public void insert() throws SQLException {
        ConferenceTranslation conferenceTranslation1 = ConferenceTranslation.makeInstance(conference, language,
                "JConf 2021", "Descr", "Lviv");
        assertFalse(ConferenceTranslationDao.findOne(connection, conference, language).isPresent());
        ConferenceTranslationDao.insert(connection, conferenceTranslation1);
        final Optional<ConferenceTranslation> conferenceTranslation2 = ConferenceTranslationDao.findOne(connection, conference, language);
        assertTrue(conferenceTranslation2.isPresent());
        assertTrue(compareConferenceTranslations(conferenceTranslation1, conferenceTranslation2.get()));
    }

    @Test
    public void update() throws SQLException {
        ConferenceTranslation conferenceTranslation1 = ConferenceTranslation.makeInstance(conference, language,
                "JConf 2021", "Descr", "Lviv");
        ConferenceTranslationDao.insert(connection, conferenceTranslation1);

        final String NEW_LOCATION = "Kyiv";
        conferenceTranslation1.setLocation(NEW_LOCATION);
        ConferenceTranslationDao.update(connection, conferenceTranslation1);
        ConferenceTranslation conferenceTranslation2 = ConferenceTranslationDao.findOne(connection, conference, language).get();
        assertEquals(NEW_LOCATION, conferenceTranslation2.getLocation());
    }

    @Test
    public void delete() throws SQLException {
        assertFalse(ConferenceTranslationDao.findOne(connection, conference, language).isPresent());

        ConferenceTranslation conferenceTranslation = ConferenceTranslation.makeInstance(conference, language,
                "JConf 2021", "Descr", "Lviv");
        ConferenceTranslationDao.insert(connection, conferenceTranslation);
        assertTrue(ConferenceTranslationDao.findOne(connection, conference, language).isPresent());

        ConferenceTranslationDao.delete(connection, conferenceTranslation);
        assertFalse(ConferenceTranslationDao.findOne(connection, conference, language).isPresent());
    }

    private boolean compareConferenceTranslations(ConferenceTranslation ct1, ConferenceTranslation ct2) {
        return Objects.equals(ct1.getConference(), ct2.getConference())
                && Objects.equals(ct1.getLanguage(), ct2.getLanguage())
                && Objects.equals(ct1.getName(), ct2.getName())
                && Objects.equals(ct1.getDescription(), ct2.getDescription())
                && Objects.equals(ct1.getLocation(), ct2.getLocation());
    }
}
