package net.anzix.imprempta.impl;

import net.anzix.imprempta.api.Header;
import net.anzix.imprempta.api.TextContent;
import net.anzix.imprempta.api.Transformer;

import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Change destination path to use nice short urls.
 */
public class ShortUrlTransformer implements Transformer {

    private String dateFormat = "yyyy'/'MM'/'dd";

    @Override
    public void transform(TextContent content) {
        Date date = (Date) content.getMeta("date");
        if (date != null) {
            String name = (String) content.getMeta(Header.NAME);
            content.setSource(Paths.get(new SimpleDateFormat(dateFormat).format(date) + "/" + name + "/index." + content.getMeta(Header.TYPE)));
        }

    }
}
