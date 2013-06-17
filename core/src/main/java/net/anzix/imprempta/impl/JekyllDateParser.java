package net.anzix.imprempta.impl;

import net.anzix.imprempta.api.ContentGenerationException;
import net.anzix.imprempta.api.Header;
import net.anzix.imprempta.api.TextContent;
import net.anzix.imprempta.api.Transformer;

import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Jekyll compatible date field parser.
 * <p/>
 * If date header field exists: itt will be parsed, if not, the start of the file name will be used.
 */
public class JekyllDateParser implements Transformer {

    SimpleDateFormat fieldParser1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ");
    SimpleDateFormat fieldParser2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");


    SimpleDateFormat nameParser = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public void transform(TextContent content) {
        String name = (String) content.getMeta(Header.NAME);
        String date = (String) content.getMeta(Header.DATE);
        if (date != null) {
            try {
                content.setMeta(Header.DATE, fieldParser2.parse(date));
            } catch (ParseException e) {
                try {
                    content.setMeta(Header.DATE, fieldParser1.parse(date));
                } catch (ParseException ex) {
                    throw new ContentGenerationException("Can't parse date field: ", ex, content);
                }
            }
        } else {
            try {
                content.setMeta(Header.DATE, nameParser.parse(name.substring(0, 10)));
            } catch (ParseException e) {
                throw new ContentGenerationException("Can't parse date from the first characters of the " +
                        "fileName: ", e, content);
            }

        }
        //remove the date from the beginning of the
        content.setSource(Paths.get(content.getSource().toString().replace(name.substring(0, 11), "")));

    }
}
