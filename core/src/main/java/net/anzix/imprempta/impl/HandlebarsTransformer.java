package net.anzix.imprempta.impl;

import com.github.jknack.handlebars.*;
import com.github.jknack.handlebars.helper.EachHelper;
import com.github.jknack.handlebars.helper.StringHelpers;
import net.anzix.imprempta.api.Content;
import net.anzix.imprempta.api.GeneratorException;
import net.anzix.imprempta.api.Header;
import net.anzix.imprempta.api.TextContent;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
                throw new GeneratorException("Can't resolve variable " + var + " in " + context);
            }
        });
        handlebars.registerHelper("each", new EachHelper());
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
    protected String transform(TextContent layout, TextContent content) {

        try {
            Template template = handlebars.compileInline(layout.getContent());
            Map<String, Object> values = new HashMap<String, Object>();
            values.put("site", site);
            if (content != null) {
                values.put("content", new Handlebars.SafeString(content.getContent()));
                values.put("page", content);
            } else {
                values.put("page", layout);
            }

            return template.apply(values);
        } catch (IOException e) {
            throw new GeneratorException("Can't render template with handlebars ", e);
        }


    }
}
