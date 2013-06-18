package net.anzix.imprempta.impl;

import junit.framework.Assert;
import net.anzix.imprempta.api.Header;
import net.anzix.imprempta.api.TextContent;
import org.junit.Test;

import java.nio.file.Paths;
import java.text.SimpleDateFormat;

public class ShortUrlTransformerTest {
    public static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");

    @Test
    public void testTransform() throws Exception {
        TextContent c = new TextContent();
        c.setSource(Paths.get("_post/test.md"));
        c.put(Header.DATE, SDF.parse("2012-05-31"));
        ShortUrlTransformer transformer = new ShortUrlTransformer();
        transformer.transform(c);
        Assert.assertEquals(Paths.get("2012/05/31/test/index.md"), c.getSource());


    }
}
