package net.madand.conferences.db.util;

import net.madand.conferences.auth.Role;
import net.madand.conferences.db.dao.*;
import net.madand.conferences.entity.*;
import net.madand.conferences.l10n.Languages;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * This class has methods for populating the database with example data.
 * Useful for automated tests and demonstration purposes.
 */
public class DbInflator {
    private Connection connection;

    public DbInflator(Connection connection) {
        this.connection = connection;
    }

    public static void main(String[] args) throws SQLException {
        if (args.length != 3) {
            System.out.println("Usage: java " + DbInflator.class.getCanonicalName() + " connectionUrl username password");
            return;
        }

        DbInflator inflator = new DbInflator(DriverManager.getConnection(args[0], args[1], args[2]));
        inflator.insertAll();
        System.out.println("Data was successfully inserted into the database!");
    }

    public void insertAll() throws SQLException {
        Language en = Language.makeInstance("en", "English", true);
        LanguageDao.insert(connection, en);
        Languages.add(en);

        Language uk = Language.makeInstance("uk", "Ukrainian", false);
        LanguageDao.insert(connection, uk);
        Languages.add(uk);

        User moderator1 = User.makeInstance("mod1@s.com", "Mr Moder 1", "a", Role.MODERATOR);
        UserDao.insert(connection, moderator1);
        User speaker1 = User.makeInstance("speaker1@s.com", "Mr Speaker 1", "a", Role.SPEAKER);
        UserDao.insert(connection, speaker1);
        User attendee1 = User.makeInstance("attd1@s.com", "Mr Attendee 1", "a", Role.ATTENDEE);
        UserDao.insert(connection, attendee1);

        Conference conference1 = Conference.makeInstance(LocalDate.now(), Languages.getDefaultLanguage(), 0);
        ConferenceDao.insert(connection, conference1);
        ConferenceTranslation conferenceTranslation1 = ConferenceTranslation.makeInstance(conference1, en,
                "JConf 2021", "Descr\nDescr\nDescr", "Lviv");
        ConferenceTranslationDao.insert(connection, conferenceTranslation1);
        ConferenceTranslation conferenceTranslation2 = ConferenceTranslation.makeInstance(conference1, uk,
                "", "Опис\nОпис\nОпис", "Львів");
        ConferenceTranslationDao.insert(connection, conferenceTranslation2);

        Talk talk= Talk.makeInstance(conference1, speaker1, LocalTime.of(10, 0), LocalTime.of(10, 45));
        TalkDao.insert(connection, talk);
    }
}
