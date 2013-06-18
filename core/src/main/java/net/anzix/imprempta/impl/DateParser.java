package net.anzix.imprempta.impl;

import net.anzix.imprempta.api.ContentGenerationException;
import net.anzix.imprempta.api.Header;
import net.anzix.imprempta.api.TextContent;
import net.anzix.imprempta.api.Transformer;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Replace string represented data with a parsed one.
 */
public class DateParser implements Transformer {

    private SimpleDateFormat formatter;

    public DateParser(String format) {
        formatter = new SimpleDateFormat(format);
    }

    @Override
    public void transform(TextContent content) {
        Object dateData = content.get(Header.DATE);
        if (dateData != null && dateData instanceof String) {
            try {
                content.put(Header.DATE, formatter.parse(((String) dateData).trim()));
            } catch (ParseException e) {
                throw new ContentGenerationException("Can't parse date", e, content);
            }
            ;
        }

    }
}
