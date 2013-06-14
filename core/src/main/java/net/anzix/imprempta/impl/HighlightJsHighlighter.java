package net.anzix.imprempta.impl;

import com.google.inject.Inject;
import net.anzix.imprempta.api.Site;
import net.anzix.imprempta.api.SyntaxHighlighter;

/**
 * Client side only highlighter using highlight.js
 */
public class HighlightJsHighlighter implements SyntaxHighlighter {

    @Inject
    Site site;

    @Override
    public String highlight(String format, String content) {
        return content;
    }
}
