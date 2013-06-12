package net.anzix.imprempta.impl;

import net.anzix.imprempta.api.Syntax;
import net.anzix.imprempta.api.TextContent;

/**
 * Don't don anything syntax for pure text files.
 */
public class SimpleSyntax implements Syntax {

    @Override
    public void transform(TextContent content) {
        //NOOP
    }
}
