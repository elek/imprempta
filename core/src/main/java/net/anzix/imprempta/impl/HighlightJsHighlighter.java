package net.anzix.imprempta.impl;

import com.google.inject.Inject;
import net.anzix.imprempta.api.Parameter;
import net.anzix.imprempta.api.Site;
import net.anzix.imprempta.api.SyntaxHighlighter;
import net.anzix.imprempta.api.TextContent;
import net.anzix.imprempta.api.header.Css;
import net.anzix.imprempta.api.header.Js;
import net.anzix.imprempta.api.header.ScriptContent;

/**
 * Client side only highlighter using highlight.js
 */
public class HighlightJsHighlighter implements SyntaxHighlighter {

    @Inject
    Site site;

    boolean firstTime = true;

    @Parameter()
    private String style = "default";

    @Override
    public String highlight(String format, String content, TextContent context) {
        //TODO create some kind of hooks for the first time initialization.
        if (firstTime) {
            TextContent tc;

            tc = TextContent.fromResource("/highlight.pack.js", "js/highlight.pack.js");
            site.addContent(tc);

            tc = TextContent.fromResource("/" + style + ".css", "styles/" + style + ".css");
            site.addContent(tc);

            firstTime = false;
        }

        //TODO remove template specific root from here
        context.addHeaderExtension(new Css("styles/" + style + ".css"));
        context.addHeaderExtension(new Js("js/highlight.pack.js"));
        context.addHeaderExtension(new ScriptContent("hljs.initHighlightingOnLoad();"));

        return content;
    }

    public HighlightJsHighlighter withStyle(String style) {
        this.style = style;
        return this;
    }
}
