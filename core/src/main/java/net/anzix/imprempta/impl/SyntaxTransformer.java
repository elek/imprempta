package net.anzix.imprempta.impl;

import com.google.inject.Inject;
import com.google.inject.Injector;
import net.anzix.imprempta.api.*;

import java.util.List;
import java.util.Map;

/**
 * Transform syntaxes to html.
 */
public class SyntaxTransformer implements Transformer {

    @Inject
    ExtensionManager ext;

    @Inject
    Injector inject;

    @Override
    public void transform(TextContent content) {
        //todo use selector instead of role
        ExtensionChain<Syntax> syntaxExts = ext.getExtensionChain(Syntax.class);
        for (Extension<Syntax> syn : syntaxExts.getAllExtension(content)) {
            if (syn.role.equals(content.getMeta(Header.TYPE))) {
                inject.getInstance(syn.type).transform(content);
            }
        }
    }
}
