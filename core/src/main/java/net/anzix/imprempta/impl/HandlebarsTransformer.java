package net.anzix.imprempta.impl;

import com.github.jknack.handlebars.*;
import com.github.jknack.handlebars.helper.EachHelper;
import com.github.jknack.handlebars.helper.StringHelpers;
import com.github.jknack.handlebars.io.TemplateSource;
import net.anzix.imprempta.api.*;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Transform templates with Handlebars
 */
public class HandlebarsTransformer extends BasicTemplateTransformer {

    Handlebars handlebars = new Handlebars();

    public HandlebarsTransformer() {
        handlebars = handlebars.with(new MissingValueResolver() {
            @Override
            public String resolve(Object context, String var) {
                if (context instanceof HashMap && ((HashMap) context).get("page") instanceof Content) {
                    Content content = (Content) ((HashMap) context).get("page");
                    throw new ContentGenerationException("Can't resolve variable " + var, content);
                } else {
                    throw new GeneratorException("Can't resovle variable: " + var);
                }

            }
        });
        //handlebars.registerHelper("each", new EachHelper());
        StringHelpers.dateFormat.registerHelper(handlebars);
        handlebars.registerHelper("created", new Helper<Content>() {
            @Override
            public CharSequence apply(Content context, Options options) throws IOException {
                String d = (String) context.getMeta(Header.DATE);
                if (d == null) {
                    return "";
                } else {
                    try {
                        return new SimpleDateFormat("yyyy-MM-dd").format(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z").parse(d));
                    } catch (ParseException e) {
                        throw new GeneratorException("Can't reformat date " + d, e);
                    }
                }
            }
        });

    }

    @Override
    protected String transform(final TextContent layout, final TextContent content) {
        final String baseText;
        if (content != null) {
            baseText = content.getContent();
        } else {
            baseText = layout.getContent();
        }
        try {
            Template template = handlebars.compile(new TemplateSource() {
                @Override
                public String content() throws IOException {
                    return baseText;
                }

                @Override
                public Reader reader() throws IOException {
                    return new StringReader(layout.getContent());
                }

                @Override
                public String filename() {
                    if (content != null) {
                        return content.getSource().toString();
                    } else {
                        return layout.getSource().toString();
                    }
                }

                @Override
                public long lastModified() {
                    return new Date().getTime();
                }
            });

            Map<String, Object> values = new HashMap<String, Object>();
            values.put("site", site);
            if (content != null) {
                values.put("content", new Handlebars.SafeString(content.getContent()));
                values.put("page", content);
            } else {
                values.put("page", layout);
            }

            return template.apply(values);
        } catch (Exception e) {
            throw new ContentGenerationException("Can't render template with handlebars ", e, layout);
        }


    }
}
