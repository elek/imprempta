package net.anzix.imprempta.impl;

import junit.framework.Assert;
import net.anzix.imprempta.api.TextContent;
import org.junit.Test;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class YamlHeaderContentParserTest {

    @Test
    public void testParse() throws Exception {
        Path root = Paths.get("");
        Path resource = Paths.get("src/test/resources/jhacks/wiki/webapp.md");
        System.out.println(root.toAbsolutePath());
        System.out.println(root.relativize(resource));
        System.out.println(root.relativize(Paths.get("src")));
        System.out.println(root.relativize(Paths.get("src/test")));
        System.out.println(root.relativize(Paths.get("src\\test")));
        System.out.println(new File(".").getAbsolutePath() );
        YamlHeaderContentParser parser = new YamlHeaderContentParser(root);
        TextContent c = new TextContent();
        parser.loadTextContent(resource, c);
        Assert.assertEquals("kocka", c.get("author"));
        Assert.assertEquals(Paths.get("src/test/resources/jhacks/wiki/webapp.md"), Paths.get(c.getUrl()));
        System.out.println(c.getContent());
    }

    @Test
    public void testYaml() throws Exception {
        YamlHeaderContentParser parser = new YamlHeaderContentParser(Paths.get("."));
        TextContent c = new TextContent();
        parser.loadTextContent(Paths.get("src/test/resources/post5.html"), c);
        Assert.assertEquals("qwe", c.get("title"));
        Assert.assertEquals(Arrays.asList(new String[]{"asd", "qwe"}), c.getMeta("tags"));
    }
}
