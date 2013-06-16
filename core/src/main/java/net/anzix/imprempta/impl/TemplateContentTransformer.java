package net.anzix.imprempta.impl;


import com.github.jknack.handlebars.Handlebars;
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

    @Inject
    Injector inject;

    @Override
    public void transform(TextContent content) {
        Map<String, Object> values = new HashMap<String, Object>();
        values.put("site", site);
//        if (content != null) {
//            values.put("content", new Handlebars.SafeString(content.getContent()));
//            values.put("page", content);
//        } else {
        values.put("page", content);
        TemplateLanguage templater = getTemplateLanguage(content);
        String c = templater.render(content, values);
        content.setContent(c);
    }

    public TemplateLanguage getTemplateLanguage(TextContent content) {
        return inject.getInstance(manager.getExtensionChain(TemplateLanguage.class).getFirstMatch(content));
    }
}
