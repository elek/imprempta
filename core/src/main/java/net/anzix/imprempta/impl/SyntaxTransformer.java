package net.anzix.imprempta.impl;

import com.google.inject.Inject;
import net.anzix.imprempta.api.Header;
import net.anzix.imprempta.api.Syntax;
import net.anzix.imprempta.api.TextContent;
import net.anzix.imprempta.api.Transformer;

import java.util.Map;

/**
 * Transform syntaxes to html.
 */
public class SyntaxTransformer implements Transformer {

    @Inject
    Map<String, Syntax> syntaxes;

    @Override
    public void transform(TextContent content) {
        for (String role : syntaxes.keySet()) {
            if (role.equals(content.getMeta(Header.TYPE))) {
                syntaxes.get(role).transform(content);
            }
        }
    }
}
