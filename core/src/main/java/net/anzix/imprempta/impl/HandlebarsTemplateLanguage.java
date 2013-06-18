package net.anzix.imprempta.impl;

import com.github.jknack.handlebars.*;
import com.github.jknack.handlebars.context.FieldValueResolver;
import com.github.jknack.handlebars.context.JavaBeanValueResolver;
import com.github.jknack.handlebars.context.MapValueResolver;
import com.github.jknack.handlebars.helper.StringHelpers;
import com.github.jknack.handlebars.io.TemplateSource;
import net.anzix.imprempta.api.*;
import net.anzix.imprempta.api.header.HeaderExtension;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Java based handlebars implementation.
 *
 * @url https://github.com/jknack/handlebars.java
 */
public class HandlebarsTemplateLanguage implements TemplateLanguage {

    Handlebars handlebars = new Handlebars();

    private TextContent current;

    public HandlebarsTemplateLanguage() {
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
        handlebars.registerHelper("rfcdate", new Helper<Date>() {
            private final SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            private final SimpleDateFormat sdf2 = new SimpleDateFormat("Z");

            @Override
            public CharSequence apply(Date context, Options options) throws IOException {
                String s1 = sdf1.format(context);
                String s2 = sdf2.format(context);
                if (s2.length() > 3) {
                    s2 = s2.substring(0, 3) + ":" + s2.substring(3);
                }
                return s1 + s2;

            }
        });

        handlebars.registerHelper("addHeaderExtension", new Helper<List<HeaderExtension>>() {

            @Override
            public CharSequence apply(List<HeaderExtension> context, Options options) throws IOException {
                for (HeaderExtension ext : context) {
                    current.addHeaderExtension(ext);
                }
                return "";
            }
        });


    }


    @Override
    public String render(final TextContent content, final Map<String, Object> context) {
        this.current = content;
        try {
            Template template = handlebars.compile(new TemplateSource() {
                @Override
                public String content() throws IOException {
                    return content.getContent();
                }

                @Override
                public Reader reader() throws IOException {
                    return new StringReader(content.getContent());
                }

                @Override
                public String filename() {
                    return content.getSource().toString();
                }

                @Override
                public long lastModified() {
                    return new Date().getTime();
                }
            });

            Context hcx = Context.newBuilder(context).resolver(JavaBeanValueResolver.INSTANCE, FieldValueResolver.INSTANCE, MapValueResolver.INSTANCE).build();
            return template.apply(hcx);
        } catch (Exception e) {
            throw new ContentGenerationException("Can't render template with handlebars ", e, content);
        }
    }
}
