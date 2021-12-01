package net.madand.conferences.l10n;

import net.madand.conferences.entity.Language;

import java.util.Locale;
import java.util.ResourceBundle;

public class LocalizationHelper {
    private String bundleName;

    public LocalizationHelper(String bundleName) {
        this.bundleName = bundleName;
    }

    public String message(String key, Language language) {
        ResourceBundle bundle = ResourceBundle.getBundle(bundleName, new Locale(language.getCode()));
        return bundle.getString(key);
    }
}
