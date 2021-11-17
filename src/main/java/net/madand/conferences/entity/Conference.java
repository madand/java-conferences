package net.madand.conferences.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Conference implements Serializable {
    private static final long serialVersionUID = 8939135725291430909L;

    private int id;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private LocalDate eventDate;
    private int actuallyAttendedCount;

    // The following properties are only set when fetching data from v_conference view.
    private String name;
    private String description;
    private String location;

    /**
     * Static factory of {@link Conference} instances.
     *
     * @param eventDate the event date.
     * @return the constructed instance.
     */
    public static Conference makeInstance(LocalDate eventDate, int actuallyAttendedCount) {
        Conference conference = new Conference();
        conference.setEventDate(eventDate);
        conference.setActuallyAttendedCount(actuallyAttendedCount);
        return conference;
    }

    public List<ConferenceTranslation> makeTranslations(List<Language> languages) {
        return languages.stream()
                .map(lang -> ConferenceTranslation.makeInstance(this, lang))
                .collect(Collectors.toList());
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

    public int getActuallyAttendedCount() {
        return actuallyAttendedCount;
    }

    public void setActuallyAttendedCount(int actuallyAttendedCount) {
        this.actuallyAttendedCount = actuallyAttendedCount;
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
