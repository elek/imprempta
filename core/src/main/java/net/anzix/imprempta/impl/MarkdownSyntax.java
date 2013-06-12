package net.anzix.imprempta.impl;

import net.anzix.imprempta.api.Header;
import net.anzix.imprempta.api.Syntax;
import net.anzix.imprempta.api.TextContent;
import net.anzix.imprempta.api.Transformer;
import org.pegdown.Extensions;
import org.pegdown.PegDownProcessor;

/**
 * Transform markdown type contents.
 */
public class MarkdownSyntax implements Syntax {

    PegDownProcessor processor;

    public MarkdownSyntax() {
        this.processor = new PegDownProcessor(Extensions.FENCED_CODE_BLOCKS);

    }

    @Override
    public void transform(TextContent content) {
        if (content.getMeta(Header.TYPE).equals("md")) {
            content.setContent(this.processor.markdownToHtml(content.getContent()));
            content.setMeta(Header.TYPE, "html");
        }
    }
}
