package net.madand.conferences.web.util;

import net.madand.conferences.entity.Language;

import java.util.regex.Pattern;

public class WebFormUtil {
    private WebFormUtil() {}

    private static final Pattern PATTERN = Pattern.compile("[\\[\\]]");

    public static String nameToID(String name) {
        return PATTERN.matcher(name).replaceAll("-");
    }

    public static String localizedParamName(String baseName, Language language) {
        return String.format("%s[%s]", baseName, language.getCode());
    }
}
