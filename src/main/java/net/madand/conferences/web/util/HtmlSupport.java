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
        return linesToParagraphsTruncating(text, Integer.MAX_VALUE);
    }

    public static String linesToParagraphsTruncating(String text, Integer maxChars) {
        final String ELLIPSIS = "…";
        StringBuilder sb = new StringBuilder();

        int charsLeft = maxChars;
        Scanner scanner = new Scanner(text);
        while (scanner.hasNextLine()) {
            if (charsLeft <= 0) {
                break;
            }
            String nextLine = scanner.nextLine();
            final int length = nextLine.length();
            if (length > charsLeft) {
                nextLine = nextLine.substring(0, charsLeft) + ELLIPSIS;
            }
            charsLeft -= length;

            sb.append("<p>").append(Util.escapeXml(nextLine)).append("</p>");
        }

        return sb.toString();
    }
}
