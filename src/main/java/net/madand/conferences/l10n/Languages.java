package net.madand.conferences.l10n;

import net.madand.conferences.entity.Language;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * All available languages are cached here so that you can get the language by ID anywhere, without resorting to DAO.
 * This cache MUST be populated when the application starts.
 */
public class Languages {
    private static final Map<Integer, Language> languages = new HashMap<>();
    private static Language defaultLanguage;
    private static Language currentLanguage;

    private Languages() {}

    public static Language get(int id) {
        return Optional.ofNullable(languages.get(id)).orElseThrow(() -> {
            throw new RuntimeException(String.format("Language with ID=%d was not found", id));
        });
    }

    public static Language getDefaultLanguage() {
        if (defaultLanguage == null) {
            throw new RuntimeException("No default language was loaded");
        }
        return defaultLanguage;
    }

    public static Language getCurrentLanguage() {
        if (currentLanguage == null) {
            return getDefaultLanguage();
        }
        return currentLanguage;
    }

    public static void setCurrentLanguage(Language language) {
        currentLanguage = language;
    }

    public static void add(Language language) {
        languages.put(language.getId(), language);
        if (language.isDefault()) {
            defaultLanguage = language;
        }
    }
}
