package net.anzix.imprempta.impl;

import junit.framework.Assert;
import net.anzix.imprempta.api.Layout;
import net.anzix.imprempta.api.Site;
import net.anzix.imprempta.api.TextContent;
import org.junit.Test;

import java.nio.file.Paths;

public class SimplifiedLiquidTransformerTest {

    @Test
    public void testTransform() throws Exception {
        YamlHeaderContentParser parser = new YamlHeaderContentParser(Paths.get("."));
        Layout l = (Layout) parser.parse(Paths.get("src/test/resources/jhacks/_layouts/default.html"));

        Site s = new Site(".");
        s.addLayout(l);


        TextContent c = (TextContent) parser.parse(Paths.get("src/test/resources/jhacks/index.md"));

        SimplifiedLiquidTransformer slt = new SimplifiedLiquidTransformer();
        slt.setSite(s);

        slt.transform(c);
        Assert.assertTrue(c.getContent().contains("<body>"));
        System.out.println(c.getContent());
    }
}
