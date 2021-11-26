package net.madand.conferences.entity;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.Objects;

public class TalkSpeakerRequest implements Serializable {
    private static final long serialVersionUID = -4455810024054386169L;

    private int id;
    private OffsetDateTime createdAt;
    private Talk talk;
    private User speaker;

    private int talkId;

    private String talkName;
    private String conferenceName;
    private String speakerName;
    private String speakerEmail;

    public static TalkSpeakerRequest makeInstance(Talk talk, User speaker) {
        TalkSpeakerRequest instance = new TalkSpeakerRequest();
        instance.setTalk(talk);
        instance.setSpeaker(speaker);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TalkSpeakerRequest talkSpeakerRequest = (TalkSpeakerRequest) o;
        return id == talkSpeakerRequest.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "TalkSpeakerRequest{" +
                "id=" + id +
                ", createdAt=" + createdAt +
                ", talk=" + talk +
                ", speaker=" + speaker +
                '}';
    }
}
