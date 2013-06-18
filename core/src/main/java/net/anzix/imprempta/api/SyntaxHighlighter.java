package net.anzix.imprempta.api;

/**
 * Syntax highlight a code block.
 */
public interface SyntaxHighlighter {

    public String highlight(String format, String content, TextContent context);

}
