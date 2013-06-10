package net.anzix.imprempta.api;

import junit.framework.Assert;
import org.junit.Test;

import java.nio.file.Paths;

public class ContentTest {
    @Test
    public void testGetUrl() throws Exception {
        TextContent c = new TextContent();
        c.setSource(Paths.get("index.html"));
        Assert.assertEquals("index.html", c.getUrl());
        Assert.assertEquals("", c.getRootUrl());

        c.setSource(Paths.get("qwe/index.html"));
        Assert.assertEquals("qwe/index.html", c.getUrl());
        Assert.assertEquals("../", c.getRootUrl());

        c.setSource(Paths.get("qwe/asd/index.html"));
        Assert.assertEquals("qwe/asd/index.html", c.getUrl());
        Assert.assertEquals("../../", c.getRootUrl());
    }
}
