package net.madand.conferences.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

public class ConferenceTranslation implements Serializable {
    private static final long serialVersionUID = -2505015520200827678L;

    private Conference conference;
    private Language language;
    private String name;
    private String description;
    private String location;

    /**
     * Static factory of {@link ConferenceTranslation} instances.
     *
     * @param conference
     * @param language
     * @param name
     * @param description
     * @param location
     * @return
     */
    public static ConferenceTranslation makeInstance(Conference conference, Language language, String name,
                                                     String description, String location) {
        ConferenceTranslation conferenceTranslation = new ConferenceTranslation();
        conferenceTranslation.setConference(conference);
        conferenceTranslation.setLanguage(language);
        conferenceTranslation.setName(name);
        conferenceTranslation.setDescription(description);
        conferenceTranslation.setLocation(location);
        return conferenceTranslation;
    }

    public Conference getConference() {
        return conference;
    }

    public void setConference(Conference conference) {
        this.conference = conference;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConferenceTranslation that = (ConferenceTranslation) o;
        return conference.equals(that.conference) && language.equals(that.language);
    }

    @Override
    public int hashCode() {
        return Objects.hash(conference, language);
    }

    @Override
    public String toString() {
        return "ConferenceTranslation{" +
                "conference=" + conference +
                ", language=" + language +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", location='" + location + '\'' +
                '}';
    }
}
