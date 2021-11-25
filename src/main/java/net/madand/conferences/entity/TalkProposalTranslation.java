package net.madand.conferences.entity;

import java.io.Serializable;
import java.util.Objects;

public class TalkProposalTranslation implements Serializable {
    private static final long serialVersionUID = -3759490339657489379L;

    private TalkProposal talkProposal;
    private Language language;
    private String name;
    private String description;

    /**
     * Static factory of {@link TalkProposalTranslation} instances.
     *
     * @param talkProposal
     * @param language
     * @return
     */
    public static TalkProposalTranslation makeInstance(TalkProposal talkProposal, Language language) {
        TalkProposalTranslation talkProposalTranslation = new TalkProposalTranslation();
        talkProposalTranslation.setTalkProposal(talkProposal);
        talkProposalTranslation.setLanguage(language);
        return talkProposalTranslation;
    }

    public TalkProposal getTalkProposal() {
        return talkProposal;
    }

    public void setTalkProposal(TalkProposal talkProposal) {
        this.talkProposal = talkProposal;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TalkProposalTranslation that = (TalkProposalTranslation) o;
        return talkProposal.equals(that.talkProposal) && language.equals(that.language);
    }

    @Override
    public int hashCode() {
        return Objects.hash(talkProposal, language);
    }

    @Override
    public String toString() {
        return "TalkProposalTranslation{" +
                "language=" + language +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
