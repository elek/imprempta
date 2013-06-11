package net.anzix.imprempta.impl;

import net.anzix.imprempta.api.TextContent;
import net.anzix.imprempta.api.Transformer;

/**
 * Don't do anything.
 */
public class NoTransformer implements Transformer {

    @Override
    public void transform(TextContent content) {
    }
}
