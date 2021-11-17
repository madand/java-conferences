package net.madand.conferences.db.inflator;

import net.madand.conferences.auth.Role;
import net.madand.conferences.db.dao.ConferenceDao;
import net.madand.conferences.db.dao.ConferenceTranslationDao;
import net.madand.conferences.db.dao.LanguageDao;
import net.madand.conferences.db.dao.UserDao;
import net.madand.conferences.entity.*;
import net.madand.conferences.l10n.Languages;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.jdbc.ScriptRunner;

import java.io.IOException;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.IntStream;

/**
 * Helper for populating the database with Demo Data.
 */
public class DbInflator {
    public static final String SQL_TRUNCATE_ALL_TABLES_FILE = "sql/truncate_all_tables.sql";

    private Connection connection;

    public DbInflator(Connection connection) {
        this.connection = connection;
    }

    private static final String CODE_EN = "en";
    private static final Language EN = Language.makeInstance(CODE_EN, "English", true);
    private static final String CODE_UK = "uk";
    private static final Language UK = Language.makeInstance(CODE_UK, "Українська", false);

    private int locationCursor = 0;
    private int locationsCount;
    private static final Map<String, String[]> locations = new HashMap<>();

    {
        locations.put(CODE_EN, new String[] { "Kyiv", "Lviv", "Odesa", "Kharkiv" });
        locations.put(CODE_UK, new String[] { "Київ", "Львів", "Одеса", "Харків" });
        locationsCount = locations.get(CODE_EN).length;
    }

    private Map<String, String> nextLocationNames() {
        Map<String, String> result = new HashMap<>();
        result.put(CODE_EN, locations.get(CODE_EN)[locationCursor]);
        result.put(CODE_UK, locations.get(CODE_UK)[locationCursor]);
        locationCursor = (locationCursor + 1) % locationsCount;
        return result;
    }

    private int confNameCursor = 0;
    private static final String[] confNames = new String[] { "JavaConf", "European Lisp Symposium", "JS Fest" };

    private String nextConferenceName(int year) {
        String result = confNames[confNameCursor] + " " + year;
        confNameCursor = (confNameCursor + 1) % confNames.length;
        return result;
    }

    public static void main(String[] args) throws SQLException, IOException {
        if (args.length != 3) {
            System.out.println("Usage: java " + DbInflator.class.getCanonicalName() + " connectionUrl username password");
            return;
        }

        DbInflator inflator = new DbInflator(DriverManager.getConnection(args[0], args[1], args[2]));

        inflator.truncateDbTables();
        inflator.insertAll();

        System.out.println("Data was successfully inserted into the database!");
    }

    public void truncateDbTables() throws IOException, SQLException {
        Reader reader = Resources.getResourceAsReader(SQL_TRUNCATE_ALL_TABLES_FILE);
        connection.setAutoCommit(false);
        ScriptRunner scriptRunner = new ScriptRunner(connection);
        scriptRunner.setLogWriter(null);
        scriptRunner.runScript(reader);
        connection.setAutoCommit(true);
    }

    private List<Language> languages = new ArrayList<>();
    private List<User> moderators = new ArrayList<>();
    private List<User> speakers = new ArrayList<>();
    private List<User> attendees = new ArrayList<>();
    private List<Conference> conferences = new ArrayList<>();
    private List<ConferenceTranslation> conferenceTranslations = new ArrayList<>();
    private List<Talk> talks = new ArrayList<>();
    private List<TalkTranslation> talkTranslations = new ArrayList<>();

    public void insertAll() throws SQLException, IOException {
        LanguageDao.insert(connection, EN);
        Languages.add(EN);
        languages.add(EN);
        LanguageDao.insert(connection, UK);
        Languages.add(UK);
        languages.add(UK);

        IntStream.range(0, 2).mapToObj((ignored) -> makeModerator()).forEach(moderators::add);
        for (User user : moderators) {
            UserDao.insert(connection, user);
        }
        IntStream.range(0, 5).mapToObj((ignored) -> makeSpeaker()).forEach(speakers::add);
        for (User user : speakers) {
            UserDao.insert(connection, user);
        }
        IntStream.range(0, 20).mapToObj((ignored) -> makeAttendee()).forEach(attendees::add);
        for (User user : attendees) {
            UserDao.insert(connection, user);
        }

        Conference conference1 = Conference.makeInstance(LocalDate.now(), 0);
        ConferenceDao.insert(connection, conference1);
        ConferenceTranslation conferenceTranslation1 = ConferenceTranslation.makeInstance(conference1, EN,
                "JConf 2021", "Descr\nDescr\nDescr", "Lviv");
        ConferenceTranslationDao.insert(connection, conferenceTranslation1);
        ConferenceTranslation conferenceTranslation2 = ConferenceTranslation.makeInstance(conference1, UK,
                "", "Опис\nОпис\nОпис", "Львів");
        ConferenceTranslationDao.insert(connection, conferenceTranslation2);
//
//        Talk talk= Talk.makeInstance(conference1, speaker1, LocalTime.of(10, 0), LocalTime.of(10, 45));
//        TalkDao.insert(connection, talk);
    }

    private int currModeratorNum = 1;
    private int currSpeakerNum = 1;
    private int currAttendeeNum = 1;

    private User makeModerator() {
        final User user = User.makeInstance(generateModeratorEmail(), generateModeratorName(), "a", Role.MODERATOR);
        currModeratorNum++;
        return user;
    }

    private User makeSpeaker() {
        final User user = User.makeInstance(generateSpeakerEmail(), generateSpeakerName(), "a", Role.SPEAKER);
        currSpeakerNum++;
        return user;
    }

    private User makeAttendee() {
        final User user = User.makeInstance(generateAttendeeEmail(), generateAttendeeName(), "a", Role.ATTENDEE);
        currAttendeeNum++;
        return user;
    }

    private String generateModeratorName() {
        return generateName("Moderator", currModeratorNum);
    }

    private String generateSpeakerName() {
        return generateName("Speaker", currSpeakerNum);
    }

    private String generateAttendeeName() {
        return generateName("Attendee", currAttendeeNum);
    }

    private String generateModeratorEmail() {
        return generateEmail("moderator", currModeratorNum);
    }

    private String generateSpeakerEmail() {
        return generateEmail("speaker", currSpeakerNum);
    }

    private String generateAttendeeEmail() {
        return generateEmail("attendee", currAttendeeNum);
    }

    private String generateEmail(String prefix, int number) {
        return prefix + "" + number + "@example.com";
    }

    private String generateName(String prefix, int number) {
        return prefix + " " + number;
    }

    private String generateConferenceName(int year) {
        return "";
    }

    private String generateDescription(String prefixText, int numberOfSentences, Language language) {
        final String SENTENCE = " fusce suscipit, wisi nec facilisis facilisis, est dui fermentum leo, quis tempor ligula erat quis odio.";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < numberOfSentences; i++) {
            sb.append(language.getName()).append(' ').append(prefixText).append(SENTENCE).append('\n');
        }
        return sb.toString();
    }
}
