package net.anzix.imprempta.impl;


import com.google.inject.Inject;
import com.google.inject.Injector;
import net.anzix.imprempta.api.*;

import java.util.HashMap;
import java.util.Map;

/**
 * User a templatel language to resolve variables inside a content.
 */
public class TemplateContentTransformer implements Transformer {

    @Inject
    Site site;

    @Inject
    ExtensionManager manager;
    private TemplateLanguage templateLanguage;

    @Override
    public void transform(TextContent content) {
        Map<String, Object> values = new HashMap<String, Object>();
        values.put("site", site);
        values.put("page", content);
        TemplateLanguage templater = getTemplateLanguage(content);
        String c = templater.render(content, values);
        content.setContent(c);
    }

    public TemplateLanguage getTemplateLanguage(TextContent content) {
        return manager.getExtensionChain(TemplateLanguage.class).getFirstMatch(content);
    }
}
