package net.anzix.imprempta.impl;

import junit.framework.Assert;
import net.anzix.imprempta.api.Content;
import net.anzix.imprempta.api.Header;
import net.anzix.imprempta.api.Site;
import net.anzix.imprempta.api.TextContent;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Map;

public class ArchiveIndexTest {
    @Test
    public void testTransform() throws Exception {
        ArchiveIndex index = new ArchiveIndex();
        Site site = new Site(".");
        index.setSite(site);

        TextContent c = new TextContent();
        c.put(Header.DATE, new SimpleDateFormat("yyyy-MM-dd").parse("2013-05-12"));
        c.put(Header.CLASS, "test");

        index.transform(c);

        Map<String, Map<String, Map<String, Content>>> ix = (Map<String, Map<String, Map<String, Content>>>) site.get("archive");
        Assert.assertEquals(c, ix.get("test").get("2013").get("5"));


    }
}
