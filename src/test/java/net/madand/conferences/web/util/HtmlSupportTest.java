package net.madand.conferences.web.util;

import net.madand.conferences.entity.Language;
import org.apache.ibatis.annotations.Lang;
import org.junit.Test;

import static org.junit.Assert.*;

public class HtmlSupportTest {

    @Test
    public void nameToID() {
        assertEquals("name", HtmlSupport.nameToID("name"));
        assertEquals("name-en-", HtmlSupport.nameToID("name[en]"));
    }

    @Test
    public void localizedParamName() {
        Language language = new Language();
        language.setCode("en");
        assertEquals("name[en]", HtmlSupport.localizedParamName("name", language));
    }

    @Test
    public void linesToParagraphs() {
        // One line text
        String text1 = "abc";
        assertEquals("<p>abc</p>", HtmlSupport.linesToParagraphs(text1));

        // Multiline text
        String text2 = "abc\ndef";
        assertEquals("<p>abc</p><p>def</p>", HtmlSupport.linesToParagraphs(text2));

        // Text with HTML-unsafe chars
        String text3 = "abc<blink>";
        assertEquals("<p>abc&lt;blink&gt;</p>", HtmlSupport.linesToParagraphs(text3));
    }

    @Test
    public void truncate() {
        // Shorter than threshold.
        String text1 = "abc";
        assertEquals("abc", HtmlSupport.truncate(text1, 5));

        // Longer than threshold.
        String text2 = "abcdefg";
        assertEquals("abcde…", HtmlSupport.truncate(text2, 5));
    }
}