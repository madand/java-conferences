package net.madand.conferences.db.dao;

import net.madand.conferences.auth.Role;
import net.madand.conferences.entity.Conference;
import net.madand.conferences.entity.Talk;
import net.madand.conferences.entity.Language;
import net.madand.conferences.entity.User;
import net.madand.conferences.l10n.Languages;
import net.madand.conferences.test.DbHelper;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.Assert.*;

public class TalkDaoTest {
    private final DbHelper dbHelper;
    private final Connection connection;
    private Conference conference;
    private Language language;
    private User speaker;

    public TalkDaoTest() throws SQLException, IOException {
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

        User speaker = User.makeInstance("speaker@example.com", "Mr Speaker", "secret", Role.SPEAKER);
        UserDao.insert(connection, speaker);
        this.speaker = speaker;

        Conference conference = Conference.makeInstance(LocalDate.now(), Languages.getDefaultLanguage(), 0);
        ConferenceDao.insert(connection, conference);
        this.conference = conference;
    }

    @Test
    public void findAll() throws SQLException {
        List<Talk> list1 = TalkDao.findAll(connection, conference);
        assertEquals(0, list1.size());

        Talk talk= Talk.makeInstance(conference, speaker, LocalTime.of(10, 0), LocalTime.of(10, 45));
        TalkDao.insert(connection, talk);
        List<Talk> list2 = TalkDao.findAll(connection, conference);
        assertEquals("Should successfully insert", 1, list2.size());
        assertTrue("Should have the same fields", compareTalks(talk, list2.get(0)));
    }

    @Test
    public void findOne() throws SQLException {
        Talk talk1= Talk.makeInstance(conference, speaker, LocalTime.of(10, 0), LocalTime.of(10, 45));
        TalkDao.insert(connection, talk1);
        Optional<Talk> talk2 = TalkDao.findOne(connection, talk1.getId());
        assertTrue("Should successfully find", talk2.isPresent());
        assertTrue("Should have the same fields", compareTalks(talk1, talk2.get()));
    }

    @Test
    public void insert() throws SQLException {
        Talk talk1= Talk.makeInstance(conference, speaker, LocalTime.of(10, 0), LocalTime.of(10, 45));
        TalkDao.insert(connection, talk1);
        Optional<Talk> talk2 = TalkDao.findOne(connection, talk1.getId());
        assertTrue(talk2.isPresent());
        assertTrue(compareTalks(talk1, talk2.get()));
    }

    @Test
    public void update() throws SQLException {
        Talk talk1= Talk.makeInstance(conference, speaker, LocalTime.of(10, 0), LocalTime.of(10, 45));
        TalkDao.insert(connection, talk1);

        final LocalTime NEW_END_TIME = talk1.getEndTime().plusMinutes(20);
        talk1.setEndTime(NEW_END_TIME);
        TalkDao.update(connection, talk1);
        Optional<Talk> talk2 = TalkDao.findOne(connection, talk1.getId());
        assertTrue(talk2.isPresent());
        assertEquals(NEW_END_TIME, talk2.get().getEndTime());
    }

    @Test
    public void delete() throws SQLException {
        List<Talk> list1 = TalkDao.findAll(connection, conference);
        assertEquals(0, list1.size());

        Talk talk= Talk.makeInstance(conference, speaker, LocalTime.of(10, 0), LocalTime.of(10, 45));
        TalkDao.insert(connection, talk);
        assertTrue(TalkDao.findOne(connection, talk.getId()).isPresent());

        TalkDao.delete(connection, talk);
        assertFalse(TalkDao.findOne(connection, talk.getId()).isPresent());
    }

    private boolean compareTalks(Talk t1, Talk t2) {
        return Objects.equals(t1.getConference(), t2.getConference())
                && Objects.equals(t1.getSpeaker(), t2.getSpeaker())
                && Objects.equals(t1.getStartTime(), t2.getStartTime())
                && Objects.equals(t1.getEndTime(), t2.getEndTime());
    }
}
