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
    public String highlight(String format, String content) {
        //TODO create some kind of hooks for the first time initialization.
        if (firstTime) {
            //TODO remove template specific root from here
            site.addHeaderExtension(new Css("{{ page.rootUrl }}styles/" + style + ".css"));
            site.addHeaderExtension(new Js("{{ page.rootUrl }}js/highlight.pack.js"));
            site.addHeaderExtension(new ScriptContent("hljs.initHighlightingOnLoad();"));
            TextContent tc;

            tc = TextContent.fromResource("/highlight.pack.js", "js/highlight.pack.js");
            site.addContent(tc);

            tc = TextContent.fromResource("/" + style + ".css", "styles/" + style + ".css");
            site.addContent(tc);

            firstTime = false;
        }
        return content;
    }

    public HighlightJsHighlighter withStyle(String style) {
        this.style = style;
        return this;
    }
}
