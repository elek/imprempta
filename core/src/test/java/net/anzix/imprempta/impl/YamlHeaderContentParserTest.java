package net.anzix.imprempta.impl;

import junit.framework.Assert;
import net.anzix.imprempta.api.TextContent;
import org.junit.Test;

import java.nio.file.Paths;

public class YamlHeaderContentParserTest {

    @Test
    public void testParse() throws Exception {
        YamlHeaderContentParser parser = new YamlHeaderContentParser(Paths.get(""));
        TextContent c = new TextContent();
        parser.loadTextContent(Paths.get("src/test/resources/jhacks/wiki/webapp.md"), c);
        Assert.assertEquals("kocka", c.getMeta("author"));
        Assert.assertEquals("src/test/resources/jhacks/wiki/webapp.md", c.getUrl());
        System.out.println(c.getContent());

    }
}
