package net.anzix.imprempta.impl;

import com.google.inject.Inject;
import net.anzix.imprempta.api.*;
import org.parboiled.common.StringUtils;
import org.pegdown.*;
import org.pegdown.ast.RootNode;
import org.pegdown.ast.VerbatimNode;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Transform markdown type contents.
 */
public class PegdownSyntax implements Syntax, VerbatimSerializer {

    PegDownProcessor processor;

    @Inject
    Set<SyntaxHighlighter> highlighters;

    public PegdownSyntax() {
        this.processor = new PegDownProcessor(Extensions.FENCED_CODE_BLOCKS);
    }

    @Override
    public void transform(TextContent content) {

        if (content.getMeta(Header.TYPE).equals("md")) {

            try {

                Map<String, VerbatimSerializer> serializers = new HashMap<>();
                serializers.put(VerbatimSerializer.DEFAULT, this);
                RootNode astRoot = processor.parseMarkdown(content.getContent().toCharArray());
                String res = new ToHtmlSerializer(new LinkRenderer(), serializers).toHtml(astRoot);
                content.setContent(res);
                content.setMeta(Header.TYPE, "html");

            } catch (ParsingTimeoutException e) {
                throw new ContentGenerationException("Markdown transformatino was too slow ", e, content);
            }


        }
    }

    @Override
    public void serialize(VerbatimNode node, Printer printer) {
        printer.println().print("<pre><code");
        if (!StringUtils.isEmpty(node.getType())) {
            printAttribute(printer, "class", node.getType());
        }
        printer.print(">");
        String text = node.getText();
        // print HTML breaks for all initial newlines
        while (text.charAt(0) == '\n') {
            printer.print("<br/>");
            text = text.substring(1);
        }
        if (highlighters.size() > 0) {
            printer.print(highlighters.iterator().next().highlight(node.getType(), text));
        } else {
            printer.printEncoded(text);
        }
        printer.print("</code></pre>");
    }

    private void printAttribute(final Printer printer, final String name, final String value) {
        printer.print(' ').print(name).print('=').print('"').print(value).print('"');
    }
}
