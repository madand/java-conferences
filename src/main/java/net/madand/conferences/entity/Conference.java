package net.madand.conferences.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Objects;

public class Conference implements Serializable {
    private static final long serialVersionUID = 8939135725291430909L;

    private int id;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private LocalDate eventDate;
    private Language talkLanguage;
    private int actuallyAttendedCount;

    /**
     * Static factory of {@link Conference} instances.
     *
     * @param eventDate the event date.
     * @return the constructed instance.
     */
    public static Conference makeInstance(LocalDate eventDate, Language language, int actuallyAttendedCount) {
        Conference conference = new Conference();
        conference.setEventDate(eventDate);
        conference.setTalkLanguage(language);
        conference.setActuallyAttendedCount(actuallyAttendedCount);
        return conference;
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

    public LocalDate getEventDate() {
        return eventDate;
    }

    public void setEventDate(LocalDate eventDate) {
        this.eventDate = eventDate;
    }

    public Language getTalkLanguage() {
        return talkLanguage;
    }

    public void setTalkLanguage(Language talkLanguage) {
        this.talkLanguage = talkLanguage;
    }

    public int getActuallyAttendedCount() {
        return actuallyAttendedCount;
    }

    public void setActuallyAttendedCount(int actuallyAttendedCount) {
        this.actuallyAttendedCount = actuallyAttendedCount;
    }

    @Override
    public String toString() {
        return "Conference{" +
                "id=" + id +
                ", eventDate=" + eventDate +
                ", actuallyAttendedCount=" + actuallyAttendedCount +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Conference that = (Conference) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
