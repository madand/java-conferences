package net.madand.conferences.entity;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.Objects;

public class TalkSpeakerProposal implements Serializable {
    private static final long serialVersionUID = -5686484202577742448L;

    private int id;
    private OffsetDateTime createdAt;
    private Talk talk;
    private User speaker;
    private User moderator;

    private int talkId;

    private String talkName;
    private String conferenceName;
    private String speakerName;
    private String speakerEmail;
    private String moderatorName;
    private String moderatorEmail;

    public static TalkSpeakerProposal makeInstance(Talk talk, User speaker, User moderator) {
        TalkSpeakerProposal instance = new TalkSpeakerProposal();
        instance.setTalk(talk);
        instance.setSpeaker(speaker);
        instance.setModerator(moderator);
        return instance;
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

    public Talk getTalk() {
        return talk;
    }

    public void setTalk(Talk talk) {
        this.talk = talk;
    }

    public User getSpeaker() {
        return speaker;
    }

    public void setSpeaker(User speaker) {
        this.speaker = speaker;
    }

    public String getTalkName() {
        return talkName;
    }

    public void setTalkName(String talkName) {
        this.talkName = talkName;
    }

    public String getConferenceName() {
        return conferenceName;
    }

    public void setConferenceName(String conferenceName) {
        this.conferenceName = conferenceName;
    }

    public String getSpeakerName() {
        return speakerName;
    }

    public void setSpeakerName(String speakerName) {
        this.speakerName = speakerName;
    }

    public int getTalkId() {
        return talkId;
    }

    public void setTalkId(int talkId) {
        this.talkId = talkId;
    }

    public String getSpeakerEmail() {
        return speakerEmail;
    }

    public void setSpeakerEmail(String speakerEmail) {
        this.speakerEmail = speakerEmail;
    }

    public User getModerator() {
        return moderator;
    }

    public void setModerator(User moderator) {
        this.moderator = moderator;
    }

    public String getModeratorName() {
        return moderatorName;
    }

    public void setModeratorName(String moderatorName) {
        this.moderatorName = moderatorName;
    }

    public String getModeratorEmail() {
        return moderatorEmail;
    }

    public void setModeratorEmail(String moderatorEmail) {
        this.moderatorEmail = moderatorEmail;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TalkSpeakerProposal talkSpeakerProposal = (TalkSpeakerProposal) o;
        return id == talkSpeakerProposal.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "TalkSpeakerProposal{" +
                "id=" + id +
                ", createdAt=" + createdAt +
                ", talk=" + talk +
                ", speaker=" + speaker +
                ", moderator=" + moderator +
                '}';
    }
}
