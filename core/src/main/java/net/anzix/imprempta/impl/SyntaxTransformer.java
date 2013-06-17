package net.anzix.imprempta.impl;

import com.google.inject.Inject;
import net.anzix.imprempta.api.*;

/**
 * Transform syntaxes to html.
 */
public class SyntaxTransformer implements Transformer {

    @Inject
    ExtensionManager ext;

    @Override
    public void transform(TextContent content) {
        //todo use selector instead of role
        ExtensionChain<Syntax> syntaxExts = ext.getExtensionChain(Syntax.class);
        for (Extension<Syntax> syn : syntaxExts.getAllExtension(content)) {
            if (syn.role.equals(content.getMeta(Header.TYPE))) {
                syn.instance.transform(content);
            }
        }
    }
}
