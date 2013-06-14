package net.anzix.imprempta.impl;

import net.anzix.imprempta.api.SyntaxHighlighter;
import prettify.PrettifyParser;
import prettify.theme.ThemeDefault;
import syntaxhighlight.ParseResult;

import java.awt.*;
import java.util.List;

/**
 * Syntax highlighter, embedding color information with HTML tags..
 */
public class HTMLSyntaxHighlighter implements SyntaxHighlighter {

    PrettifyParser parser = new PrettifyParser();

    ThemeDefault theme = new ThemeDefault();


    private static String color(String style, ThemeDefault theme) {
        Color c = theme.getStyle(style).getColor();
        return String.format("#%02X%02X%02X", c.getRed(), c.getGreen(), c.getBlue());
    }


    @Override
    public String highlight(String form, String content) {
        List<ParseResult> results = parser.parse("java", content);
        int pos = 0;
        StringBuilder b = new StringBuilder();
        for (ParseResult r : results) {
            b.append("<span style=\"color: " + color(r.getStyleKeysString(), theme) + "\">");
            b.append(content.substring(r.getOffset(), r.getOffset() + r.getLength()));
            b.append("</span>");
            pos = r.getOffset();
        }
        return b.toString();
    }
}
