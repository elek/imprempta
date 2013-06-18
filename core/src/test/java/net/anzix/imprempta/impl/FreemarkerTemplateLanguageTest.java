package net.anzix.imprempta.impl;

import junit.framework.Assert;
import net.anzix.imprempta.api.TextContent;
import org.junit.Test;

import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class FreemarkerTemplateLanguageTest {
    @Test
    public void testRender() throws Exception {
        TextContent c = new TextContent();
        c.setContent("Test ${qwe}");
        c.setSource(Paths.get("index.html"));

        FreemarkerTemplateLanguage ftl = new FreemarkerTemplateLanguage();
        Map<String, Object> mos = new HashMap<>();
        mos.put("qwe", "xxx");
        String res = ftl.render(c, mos);

        Assert.assertEquals("Test xxx", res);
    }
}
