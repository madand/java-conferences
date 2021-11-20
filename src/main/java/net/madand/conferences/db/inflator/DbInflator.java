package net.madand.conferences.db.inflator;

import net.madand.conferences.auth.Role;
import net.madand.conferences.db.dao.*;
import net.madand.conferences.entity.*;
import net.madand.conferences.l10n.Languages;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.jdbc.ScriptRunner;

import java.io.IOException;
import java.io.Reader;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

/**
 * Helper for populating the database with Demo Data.
 */
public class DbInflator {
    public static final String TRUNCATE_ALL_TABLES_FILE = "sql/truncate_all_tables.sql";

    private final Connection connection;

    public DbInflator(Connection connection) {
        this.connection = connection;
    }

    private static final String CODE_EN = "en";
    private static final Language EN = Language.makeInstance(CODE_EN, "English", true);
    private static final String CODE_UK = "uk";
    private static final Language UK = Language.makeInstance(CODE_UK, "Українська", false);

    private final List<Language> languages = new ArrayList<>();
    {
        EN.setId(1);
        UK.setId(2);
        Languages.add(EN);
        languages.add(EN);
        Languages.add(UK);
        languages.add(UK);
    }

    private int locationCursor = 0;
    private final int locationsCount;
    private static final Map<String, String[]> locations = new HashMap<>();

    {
        locations.put(CODE_EN, new String[] { "Kyiv, Ukraine", "Amsterdam, Netherlands", "Berlin, Germany", "Warsaw, Poland" });
        locations.put(CODE_UK, new String[] { "Київ, Україна", "Амстердам, Нідерланди", "Берлін, Німеччина", "Варшава, Польща" });
        locationsCount = locations.get(CODE_EN).length;
    }

    private Map<String, String> nextLocationNames() {
        Map<String, String> result = new HashMap<>();
        result.put(CODE_EN, locations.get(CODE_EN)[locationCursor]);
        result.put(CODE_UK, locations.get(CODE_UK)[locationCursor]);
        locationCursor = (locationCursor + 1) % locationsCount;
        return result;
    }

    public static void main(String[] args) throws SQLException, IOException {
        if (args.length != 4) {
            System.out.println("Usage: java " + DbInflator.class.getCanonicalName() + " connectionUrl username password operation");
            return;
        }

        DbInflator inflator = new DbInflator(DriverManager.getConnection(args[0], args[1], args[2]));

        switch (args[3]) {
            case "truncate":
                inflator.truncateDbTables();
                break;
            case "langsAndUsers":
                inflator.insertLangsAndUsers();
                break;
            case "adjustConferenceYears":
                inflator.adjustConferences();
                break;
            case "generateTalks":
                inflator.generateTalks();
                break;
        }

        System.out.println("Data was successfully inserted into the database!");
    }

    public void truncateDbTables() throws IOException, SQLException {
        Reader reader = Resources.getResourceAsReader(TRUNCATE_ALL_TABLES_FILE);
        connection.setAutoCommit(false);
        ScriptRunner scriptRunner = new ScriptRunner(connection);
        scriptRunner.setLogWriter(null);
        scriptRunner.runScript(reader);
        connection.setAutoCommit(true);
    }

    private final List<User> moderators = new ArrayList<>();
    private final List<User> speakers = new ArrayList<>();
    private final List<User> attendees = new ArrayList<>();
    private final List<Conference> conferences = new ArrayList<>();
    private final List<ConferenceTranslation> conferenceTranslations = new ArrayList<>();
    private final List<Talk> talks = new ArrayList<>();
    private final List<TalkTranslation> talkTranslations = new ArrayList<>();

    public void insertLangsAndUsers() throws SQLException {
        LanguageDao.insert(connection, EN);
        LanguageDao.insert(connection, UK);


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
    }

    public void adjustConferences() throws SQLException, IOException {
        List<Conference> conferences = ConferenceDao.findAll(connection);
        final List<LocalDate> eventDates = new ArrayList<>();
        Collections.addAll(eventDates,
                LocalDate.of(2020, 6, 20),
                LocalDate.of(2020, 12, 15),
                LocalDate.of(2020, 12, 20),
                LocalDate.of(2020, 12, 25));

        final int DISTINCT_CONFERENCES = 4;
        final Pattern yearPattern = Pattern.compile("\\d{4}");
        final int numIters = conferences.size() / DISTINCT_CONFERENCES;
        for (int i = 0; i < numIters; i++) {
            for (int j = 0; j < DISTINCT_CONFERENCES; j++) {
                final LocalDate date = eventDates.get(j).withYear(2019 + i);
                final Conference conference = conferences.get(i * 4 + j);

                conference.setEventDate(date);
                ConferenceDao.update(connection, conference);

                Map<String, String> nextLocationNames = nextLocationNames();
                for (Language language : languages) {
                    final ConferenceTranslation translation = ConferenceTranslationDao.findOne(connection, conference, language).get();
                    translation.setName(yearPattern.matcher(translation.getName()).replaceFirst(String.valueOf(date.getYear())));
                    translation.setLocation(nextLocationNames.get(language.getCode()));

                    ConferenceTranslationDao.update(connection, translation);
                }
            }
        }
//
//        Talk talk= Talk.makeInstance(conference1, speaker1, LocalTime.of(10, 0), LocalTime.of(10, 45));
//        TalkDao.insert(connection, talk);
    }

    private SecureRandom rng = new SecureRandom();

    private void generateTalks() throws SQLException {
        final List<Conference> conferences = ConferenceDao.findAll(connection);
        speakers.clear();
        speakers.addAll(UserDao.findAllByRole(connection, Role.SPEAKER));
        for (Conference conference : conferences) {
            int talksCount = 3 + rng.nextInt(4);
            for (int i = 0; i < talksCount; i++) {
                final Talk talk = Talk.makeInstance(
                        conference,
                        speakers.get(rng.nextInt(speakers.size())),
                        LocalTime.of(12 + i, 00),
                        20 + rng.nextInt(31)
                );
                TalkDao.insert(connection, talk);

                for (Language language : languages) {
                    final String name = "Talk " + (i + 1);
                    final TalkTranslation translation = TalkTranslation.makeInstance(
                            talk,
                            language,
                            name,
                            generateDescription(name, 3, language)
                    );
                    TalkTranslationDao.insert(connection, translation);
                }
            }
        }
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
