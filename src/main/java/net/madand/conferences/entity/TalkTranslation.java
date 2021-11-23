package net.madand.conferences.entity;

import java.io.Serializable;
import java.util.Objects;

public class TalkTranslation implements Serializable {
    private static final long serialVersionUID = -1954899411115401054L;

    private Talk talk;
    private Language language;
    private String name;
    private String description;

    /**
     * Static factory of {@link TalkTranslation} instances.
     *
     * @param talk
     * @param language
     * @return
     */
    public static TalkTranslation makeInstance(Talk talk, Language language) {
        TalkTranslation talkTranslation = new TalkTranslation();
        talkTranslation.setTalk(talk);
        talkTranslation.setLanguage(language);
        return talkTranslation;
    }

    /**
     * Static factory of {@link TalkTranslation} instances.
     *
     * @param talk
     * @param language
     * @param name
     * @param description
     * @return
     */
    public static TalkTranslation makeInstance(Talk talk, Language language, String name, String description) {
        TalkTranslation talkTranslation = new TalkTranslation();
        talkTranslation.setTalk(talk);
        talkTranslation.setLanguage(language);
        talkTranslation.setName(name);
        talkTranslation.setDescription(description);
        return talkTranslation;
    }

    public Talk getTalk() {
        return talk;
    }

    public void setTalk(Talk talk) {
        this.talk = talk;
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
        TalkTranslation that = (TalkTranslation) o;
        return talk.equals(that.talk) && language.equals(that.language);
    }

    @Override
    public int hashCode() {
        return Objects.hash(talk, language);
    }

    @Override
    public String toString() {
        return "TalkTranslation{" +
                "language=" + language +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
