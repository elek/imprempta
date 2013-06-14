package net.anzix.imprempta.impl;

import junit.framework.Assert;
import net.anzix.imprempta.api.TextContent;
import org.junit.Test;

public class TxtmarkTransformerTest {
    @Test
    public void test() {
        TxtmarkTransformer t = new TxtmarkTransformer();
        TextContent tc = new TextContent();
        tc.setContent("# qwe\n\nTalharu");
        t.transform(tc);
        System.out.println(tc.getContent());
        String expected = "<h1>qwe</h1>\n<p>Talharu</p>\n";
        System.out.println(expected);
        Assert.assertEquals(expected, tc.getContent());
    }
}
