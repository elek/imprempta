package net.anzix.imprempta.impl;


import com.google.inject.Inject;
import net.anzix.imprempta.api.Header;
import net.anzix.imprempta.api.Site;
import net.anzix.imprempta.api.TextContent;
import net.anzix.imprempta.api.Transformer;

import java.io.File;
import java.nio.file.Paths;
import java.util.Date;

/**
 * Use last modified date of the source file as the creation date.
 */
public class LastModifiedDate implements Transformer {


    @Inject
    private Site site;

    @Override
    public void transform(TextContent content) {
        File f = Paths.get(site.getSourceDir()).resolve(content.getSource()).toFile();
        long date = f.lastModified();
        Date d = new Date(date);
        content.put(Header.DATE, d.toString());
    }
}
