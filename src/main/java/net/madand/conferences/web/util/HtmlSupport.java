package net.madand.conferences.web.util;

import net.madand.conferences.entity.Language;
import org.apache.taglibs.standard.tag.common.core.Util;

import java.util.Scanner;
import java.util.regex.Pattern;

public class HtmlSupport {
    private HtmlSupport() {}

    private static final Pattern PATTERN = Pattern.compile("[\\[\\]]");

    public static String nameToID(String name) {
        return PATTERN.matcher(name).replaceAll("-");
    }

    public static String localizedParamName(String baseName, Language language) {
        return String.format("%s[%s]", baseName, language.getCode());
    }


    public static String linesToParagraphs(String text) {
        StringBuilder sb = new StringBuilder();

        try (Scanner scanner = new Scanner(text)) {
            while (scanner.hasNextLine()) {
                sb.append("<p>").append(Util.escapeXml(scanner.nextLine())).append("</p>");
            }
        }

        return sb.toString();
    }

    public static String truncate(String text, Integer maxChars) {
        final String ELLIPSIS = "…";
        StringBuilder sb = new StringBuilder();

        int charsLeft = maxChars;
        try (Scanner scanner = new Scanner(text)) {
            while (scanner.hasNextLine()) {
                if (charsLeft <= 0) {
                    break;
                }

                String nextLine = scanner.nextLine();
                final int length = nextLine.length();
                final boolean isLastLine = length > charsLeft;
                if (isLastLine) {
                    nextLine = nextLine.substring(0, charsLeft).trim();
                }

                sb.append(Util.escapeXml(nextLine));

                if (isLastLine) {
                    sb.append(ELLIPSIS);
                } else {
                    sb.append(' ');
                }

                charsLeft -= length;
            }
        }

        return sb.toString();
    }
}
