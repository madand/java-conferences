package net.madand.conferences.entity;

import java.io.Serializable;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.Objects;

public class Talk implements Serializable {
    private static final long serialVersionUID = -994800254257454614L;

    private int id;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private Conference conference;
    private User speaker;
    private LocalTime startTime;
    private LocalTime endTime;

    /**
     * Static factory of {@link Talk} instances.
     * @param conference
     * @param speaker
     * @param startTime
     * @param endTime
     * @return
     */
    public static Talk makeInstance(Conference conference, User speaker, LocalTime startTime, LocalTime endTime) {
        Talk talk = new Talk();
        talk.setConference(conference);
        talk.setSpeaker(speaker);
        talk.setStartTime(startTime);
        talk.setEndTime(endTime);
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

    public LocalTime getEndTime() {
        return endTime;
    }

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
                ", endTime=" + endTime +
                '}';
    }
}
