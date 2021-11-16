package net.madand.conferences.entity;

import java.io.Serializable;
import java.util.Objects;

public class Language implements Serializable {
    private static final long serialVersionUID = -289243138145190331L;

    private int id;
    private String code;
    private String name;
    private boolean isDefault;

    /**
     * Static factory of {@link Language} instances.
     *
     * @param code the code.
     * @param name the name.
     * @param isDefault whether this is the default language.
     * @return the constructed instance.
     */
    public static Language makeInstance(String code, String name, boolean isDefault) {
        Language language = new Language();
        language.code = code;
        language.name = name;
        language.isDefault = isDefault;
        return language;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }

    @Override
    public String toString() {
        return "Language{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", isDefault=" + isDefault +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Language language = (Language) o;
        return id == language.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
