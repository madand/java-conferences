package net.madand.conferences.entity;

import net.madand.conferences.l10n.Languages;

import java.io.Serializable;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TalkProposal implements Serializable {
    private static final long serialVersionUID = -974867029443241210L;

    private int id;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private Conference conference;
    private User speaker;
    private int duration;

    // This field is for review form only, not in the DB.
    private LocalTime startTime;

    private final List<TalkProposalTranslation> translations = new ArrayList<>();

    // The following properties are only set when fetching data from v_conference view.
    private String name;
    private String description;

    public static TalkProposal makeInstanceWithTranslations(Conference conference) {
        final TalkProposal talkProposal = new TalkProposal();
        talkProposal.setConference(conference);
        Languages.list().stream()
                .map(language -> TalkProposalTranslation.makeInstance(talkProposal, language))
                .forEach(talkProposal::addTranslation);
        return talkProposal;
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

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
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

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public List<TalkProposalTranslation> getTranslations() {
        return translations;
    }

    public void addTranslation(TalkProposalTranslation translation) {
        translations.add(translation);
    }

    public void loadTranslation(TalkProposalTranslation translation) {
        setName(translation.getName());
        setDescription(translation.getDescription());
    }

    /**
     * Populate the given talk with data from this talk proposal and its translations.
     *
     * @param talk the talk to populate with data.
     */
    public void populateTalk(Talk talk) {
        talk.setConference(conference);
        talk.setSpeaker(speaker);
        talk.setStartTime(startTime);
        talk.setDuration(duration);

        List<TalkTranslation> talkTranslations = talk.getTranslations();
        List<TalkProposalTranslation> proposalTranslations = getTranslations();
        for (int i = 0; i < talkTranslations.size(); i++) {
            talkTranslations.get(i).setName(proposalTranslations.get(i).getName());
            talkTranslations.get(i).setDescription(proposalTranslations.get(i).getDescription());
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TalkProposal talk = (TalkProposal) o;
        return id == talk.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "TalkProposal{" +
                "id=" + id +
                ", conference=" + conference +
                ", speaker=" + speaker +
                ", duration=" + duration +
                '}';
    }
}
