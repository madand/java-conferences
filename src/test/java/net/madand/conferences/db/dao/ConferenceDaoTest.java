package net.madand.conferences.db.dao;

import net.madand.conferences.entity.Conference;
import net.madand.conferences.entity.Language;
import net.madand.conferences.l10n.Languages;
import net.madand.conferences.test.DbHelper;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import static org.junit.Assert.*;

public class ConferenceDaoTest {
    private final DbHelper dbHelper;
    private final Connection connection;

    public ConferenceDaoTest() throws SQLException, IOException {
        dbHelper = DbHelper.getInstance();
        connection = dbHelper.getConnection();
    }

    @Before
    public void setUp() throws IOException, SQLException {
        dbHelper.recreateDbTables();

        Language en = Language.makeInstance("en", "English", true);
        LanguageDao.insert(connection, en);
        Languages.add(en);
    }

    @Test
    public void findAll() throws SQLException {
        List<Conference> list1 = ConferenceDao.findAll(connection);
        assertEquals(0, list1.size());

        Conference conference = Conference.makeInstance(LocalDate.now(), Languages.getDefaultLanguage(), 0);
        ConferenceDao.insert(connection, conference);
        List<Conference> list2 = ConferenceDao.findAll(connection);
        assertEquals("Successfully inserted", 1, list2.size());
        assertTrue("Should have the same fields", compareConferences(conference, list2.get(0)));
    }

    @Test
    public void findOne() throws SQLException {
        Conference conference1 = Conference.makeInstance(LocalDate.now(), Languages.getDefaultLanguage(), 0);
        ConferenceDao.insert(connection, conference1);
        Conference conference2 = ConferenceDao.findOne(connection, conference1.getId()).get();
        assertNotNull("Should successfully find", conference2);
        assertTrue("Should have the same fields", compareConferences(conference1, conference2));
    }

    @Test
    public void insert() throws SQLException {
        Conference conference1 = Conference.makeInstance(LocalDate.now(), Languages.getDefaultLanguage(), 0);
        int oldId = conference1.getId();
        ConferenceDao.insert(connection, conference1);
        assertNotEquals("Should set generated ID", oldId, conference1.getId());
    }

    @Test
    public void update() throws SQLException {
        Conference conference1 = Conference.makeInstance(LocalDate.now(), Languages.getDefaultLanguage(), 0);
        ConferenceDao.insert(connection, conference1);

        final LocalDate NEW_DATE = conference1.getEventDate().plusDays(10);
        conference1.setEventDate(NEW_DATE);
        ConferenceDao.update(connection, conference1);
        Conference conference2 = ConferenceDao.findOne(connection, conference1.getId()).get();
        assertEquals(NEW_DATE, conference2.getEventDate());
    }

    @Test
    public void delete() throws SQLException {
        List<Conference> list1 = ConferenceDao.findAll(connection);
        assertEquals("No moderators yet", 0, list1.size());

        Conference conference = Conference.makeInstance(LocalDate.now(), Languages.getDefaultLanguage(), 0);
        ConferenceDao.insert(connection, conference);
        assertNotNull(ConferenceDao.findOne(connection, conference.getId()));

        ConferenceDao.delete(connection, conference);
        assertFalse(ConferenceDao.findOne(connection, conference.getId()).isPresent());
    }

    private boolean compareConferences(Conference c1, Conference c2) {
        return Objects.equals(c1.getEventDate(), c2.getEventDate())
                && Objects.equals(c1.getTalkLanguage(), c2.getTalkLanguage())
                && Objects.equals(c1.getActuallyAttendedCount(), c2.getActuallyAttendedCount());
    }
}
