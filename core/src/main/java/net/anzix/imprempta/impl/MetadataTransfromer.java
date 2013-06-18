package net.anzix.imprempta.impl;

import net.anzix.imprempta.api.Parameter;
import net.anzix.imprempta.api.TextContent;
import net.anzix.imprempta.api.Transformer;

/**
 * Add metadata to the selected text contents.
 */
public class MetadataTransfromer implements Transformer {

    @Parameter
    private String key;

    @Parameter
    private String value;

    public MetadataTransfromer() {
    }

    public MetadataTransfromer(String key, String value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public void transform(TextContent content) {
        content.put(key, value);
    }
}
