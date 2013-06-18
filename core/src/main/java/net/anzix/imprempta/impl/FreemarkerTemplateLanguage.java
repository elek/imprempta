package net.anzix.imprempta.impl;

import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import net.anzix.imprempta.api.ContentGenerationException;
import net.anzix.imprempta.api.TemplateLanguage;
import net.anzix.imprempta.api.TextContent;

import java.io.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Template resolution with freemarker.
 */
public class FreemarkerTemplateLanguage implements TemplateLanguage {

    Configuration cfg;

    Map<String, String> templateDirectory = new HashMap<>();

    public FreemarkerTemplateLanguage() {
        cfg = new Configuration();
        cfg.setTemplateLoader(new TemplateLoader() {
            @Override
            public Object findTemplateSource(String name) throws IOException {
                if (!templateDirectory.containsKey(name)) {
                    return null;
                }
                return name;
            }

            @Override
            public long getLastModified(Object templateSource) {
                return new Date().getTime();
            }

            @Override
            public Reader getReader(Object templateSource, String encoding) throws IOException {
                return new StringReader(templateDirectory.get((String) templateSource));
            }

            @Override
            public void closeTemplateSource(Object templateSource) throws IOException {

            }
        });
        cfg.setObjectWrapper(new DefaultObjectWrapper());
    }

    @Override
    public String render(TextContent content, Map<String, Object> context) {
        try {
            templateDirectory.put(content.getSource().toString(), content.getContent());
            Template temp = cfg.getTemplate(content.getSource().toString());

            Writer out = new StringWriter();
            temp.process(context, out);
            return out.toString();
        } catch (Exception ex) {
            throw new ContentGenerationException("Can't render with freemaker", ex, content);
        }
    }
}
