package net.madand.conferences.db.dao;

import net.madand.conferences.auth.Role;
import net.madand.conferences.entity.*;
import net.madand.conferences.l10n.Languages;
import net.madand.conferences.test.DbHelper;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;
import java.util.Optional;

import static org.junit.Assert.*;

public class TalkTranslationDaoTest {
    private final DbHelper dbHelper;
    private final Connection connection;
    private Language language;
    private Talk talk;

    public TalkTranslationDaoTest() throws SQLException, IOException {
        dbHelper = DbHelper.getInstance();
        connection = dbHelper.getConnection();
    }

    @Before
    public void setUp() throws IOException, SQLException {
        dbHelper.truncateDbTables();

        Language en = Language.makeInstance("en", "English", true);
        LanguageDao.insert(connection, en);
        Languages.add(en);
        language = en;

        Conference conference = Conference.makeInstance(LocalDate.now(), 0);
        ConferenceDao.insert(connection, conference);

        User speaker = User.makeInstance("a@b.com", "Mr Speaker", "secret", Role.SPEAKER);
        UserDao.insert(connection, speaker);

//        Talk talk= Talk.makeInstance(conference, speaker, LocalTime.of(10, 0), LocalTime.of(10, 45));
//        TalkDao.insert(connection, talk);
//        this.talk = talk;
    }

    @Test
    public void findOne() throws SQLException {
        TalkTranslation talkTranslation1 = TalkTranslation.makeInstance(talk, language, "Java for Newbies", "Descr");
        TalkTranslationDao.insert(connection, talkTranslation1);
        Optional<TalkTranslation> talkTranslation2 = TalkTranslationDao.findOne(connection, talk, language);
        assertTrue(talkTranslation2.isPresent());
        assertTrue(compareTalkTranslations(talkTranslation1, talkTranslation2.get()));
    }

    @Test
    public void insert() throws SQLException {
        TalkTranslation talkTranslation1 = TalkTranslation.makeInstance(talk, language, "Java for Newbies", "Descr");
        assertFalse(TalkTranslationDao.findOne(connection, talk, language).isPresent());
        TalkTranslationDao.insert(connection, talkTranslation1);
        Optional<TalkTranslation> talkTranslation2 = TalkTranslationDao.findOne(connection, talk, language);
        assertTrue(talkTranslation2.isPresent());
        assertTrue(compareTalkTranslations(talkTranslation1, talkTranslation2.get()));
    }

    @Test
    public void update() throws SQLException {
        TalkTranslation talkTranslation1 = TalkTranslation.makeInstance(talk, language, "Java for Newbies", "Descr");
        TalkTranslationDao.insert(connection, talkTranslation1);

        final String NEW_NAME = "PHP for Newbies";
        talkTranslation1.setName(NEW_NAME);
        TalkTranslationDao.update(connection, talkTranslation1);
        Optional<TalkTranslation> talkTranslation2 = TalkTranslationDao.findOne(connection, talk, language);
        assertTrue(talkTranslation2.isPresent());
        assertEquals(NEW_NAME, talkTranslation2.get().getName());
    }

    private boolean compareTalkTranslations(TalkTranslation tt1, TalkTranslation tt2) {
        return Objects.equals(tt1.getTalk(), tt2.getTalk())
                && Objects.equals(tt1.getLanguage(), tt2.getLanguage())
                && Objects.equals(tt1.getName(), tt2.getName())
                && Objects.equals(tt1.getDescription(), tt2.getDescription());
    }
}
