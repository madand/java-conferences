package net.madand.conferences.l10n;

import net.madand.conferences.entity.Language;

import javax.servlet.jsp.jstl.fmt.LocaleSupport;
import java.util.*;

/**
 * Container of all available languages. It allows one to get the language by ID from anywhere, without resorting to DAO.
 * This cache MUST be populated when the application starts.
 */
public class Languages {
    private static final Map<Integer, Language> languagesById = new HashMap<>();
    private static final Map<String, Language> languagesByCode = new HashMap<>();

    private static Language defaultLanguage;

    private Languages() {}

    public static Language getById(int id) {
        return languagesById.get(id);
    }

    /**
     * Return the language with the given code, or null if there is no such language.
     *
     * @param code the language code.
     * @return the language or null.
     */
    public static Language getByCode(String code) {
        return languagesByCode.get(code);
    }

    public static Language getDefaultLanguage() {
        if (defaultLanguage == null) {
            throw new NoSuchElementException("No default language was loaded");
        }
        return defaultLanguage;
    }

    public static synchronized void add(Language language) {
        languagesById.put(language.getId(), language);
        languagesByCode.put(language.getCode(), language);
        if (language.isDefault()) {
            defaultLanguage = language;
        }
    }
}
