package net.anzix.imprempta.impl;

import net.anzix.imprempta.api.Layout;
import net.anzix.imprempta.api.TextContent;

/**
 * Mimic simple liquid template engine possibilities.
 * <p/>
 * Currently only the {{ variable }} resolution is implemented.
 */
public class SimplifiedLiquidTransformer extends BasicTemplateTransformer {

    protected String transform(TextContent layout, TextContent content) {
        if (content != null) {
            return layout.getContent().replaceAll("\\{\\{\\s*content\\s*\\}\\}", content.getContent());
        } else {
            return layout.getContent();
        }
    }

}
