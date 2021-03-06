package net.madand.conferences.entity;

import net.madand.conferences.l10n.Languages;

import java.io.Serializable;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Talk implements Serializable {
    private static final long serialVersionUID = -994800254257454614L;

    private int id;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private Conference conference;
    private User speaker;
    private LocalTime startTime;
    private int duration;
    private LocalTime endTime;

    private final List<TalkTranslation> translations = new ArrayList<>();

    // The following properties are only set when fetching data from v_conference view.
    private String name;
    private String description;

    /**
     * Static factory of {@link Talk} instances.
     * @param conference
     * @param speaker
     * @param startTime
     * @param duration
     * @return
     */
    public static Talk makeInstance(Conference conference, User speaker, LocalTime startTime, int duration) {
        Talk talk = new Talk();
        talk.setConference(conference);
        talk.setSpeaker(speaker);
        talk.setStartTime(startTime);
        talk.setDuration(duration);
        return talk;
    }

    public static Talk makeInstanceWithTranslations(Conference conference) {
        final Talk talk = new Talk();
        talk.setConference(conference);
        Languages.list().stream()
                .map(language -> TalkTranslation.makeInstance(talk, language))
                .forEach(talk::addTranslation);
        return talk;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(OffsetDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Conference getConference() {
        return conference;
    }

    public void setConference(Conference conference) {
        this.conference = conference;
    }

    public User getSpeaker() {
        return speaker;
    }

    public void setSpeaker(User speaker) {
        this.speaker = speaker;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public LocalTime getEndTime() {
        return endTime;
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

    public List<TalkTranslation> getTranslations() {
        return translations;
    }

    public void addTranslation(TalkTranslation translation) {
        translations.add(translation);
    }

    public void loadTranslation(TalkTranslation translation) {
        setName(translation.getName());
        setDescription(translation.getDescription());
    }

    /**
     * Note that end_time field in the DB is computed by trigger, so this value is never directly saved into the DB!
     * @param endTime
     */
    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Talk talk = (Talk) o;
        return id == talk.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Talk{" +
                "id=" + id +
                ", conference=" + conference +
                ", speaker=" + speaker +
                ", startTime=" + startTime +
                ", duration=" + duration +
                '}';
    }
}
