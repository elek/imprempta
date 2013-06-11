package net.anzix.imprempta.impl;


import com.google.inject.Inject;
import com.google.inject.name.Named;
import net.anzix.imprempta.api.TextContent;
import net.anzix.imprempta.api.Transformer;


import java.io.File;
import java.nio.file.Paths;
import java.util.Date;

/**
 * Use last modified date of the source file as the creation date.
 */
public class LastModifiedDate implements Transformer {

    @Named("rootdir")
    @Inject
    private String root;

    @Override
    public void transform(TextContent content) {
        File f = Paths.get(root).resolve(content.getSource()).toFile();
        long date = f.lastModified();
        Date d = new Date(date);
        content.setMeta("xxx", d.toString());
    }
}
