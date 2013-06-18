package net.anzix.imprempta.impl;

import com.google.inject.Inject;
import net.anzix.imprempta.api.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Recursove layour resolution with the helping of a template language.
 */
public class TemplateLayoutTransformer implements Transformer {
    @Inject
    Site site;

    @Inject
    ExtensionManager manager;
    private TemplateLanguage templateLanguage;

    @Override
    public void transform(TextContent content) {
        while (content.get(Header.LAYOUT) != null) {
            String layoutName = (String) content.get(Header.LAYOUT);
            Layout layout = site.getLayout(layoutName);
            if (layout == null) {
                throw new GeneratorException("No such layout: " + layoutName + " " + content.getSource());
            } else {
                render(layout, content);
            }
        }
    }

    private void render(Layout layout, TextContent content) {
        content.delMeta("layout");

        Map<String, Object> values = new HashMap<String, Object>();
        values.put("site", site);
        values.put("content", content.getContent());
        values.put("page", content);

        String newContent = getTemplateLanguage(layout).render(layout, values);
        content.setContent(newContent);
        for (String meta : layout.getMetaKeys()) {
            if (!meta.equals(Header.PARENT.toString().toLowerCase()) && content.getMeta(meta) == null) {
                content.setMeta(meta, layout.getMeta(meta));
            }
        }
    }


    public TemplateLanguage getTemplateLanguage(TextContent content) {
        return manager.getExtensionChain(TemplateLanguage.class).getFirstMatch(content);
    }
}
