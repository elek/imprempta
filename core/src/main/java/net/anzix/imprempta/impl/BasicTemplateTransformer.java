package net.anzix.imprempta.impl;

import com.google.inject.Inject;
import net.anzix.imprempta.api.*;

/**
 * Transformer for typical template engines with recursive layout resolution.
 */
public abstract class BasicTemplateTransformer implements Transformer {

    @Inject
    Site site;

    @Override
    public void transform(TextContent content) {
        resolveLayout(content);
    }

    private void resolveLayout(TextContent content) {
        content.setContent(transform(content, null));
        while (content.getMeta(Header.LAYOUT) != null) {
            String layoutName = (String) content.getMeta(Header.LAYOUT);
            Layout layout = site.getLayout(layoutName);
            if (layout == null) {
                throw new GeneratorException("No such layout: " + layoutName + " " + content.getSource());
            } else {
                render(layout, content);
            }
        }
    }

    protected abstract String transform(TextContent layout, TextContent content);

    private void render(Layout layout, TextContent content) {
        content.delMeta("layout");
        String newContent = transform(layout, content);
        content.setContent(newContent);
        for (String meta : layout.getMetaKeys()) {
            if (!meta.equals(Header.PARENT.toString().toLowerCase()) && content.getMeta(meta) == null) {
                content.setMeta(meta, layout.getMeta(meta));
            }
        }
    }

    public void setSite(Site site) {
        this.site = site;
    }
}
