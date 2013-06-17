package net.anzix.imprempta.api.selector;

import junit.framework.Assert;
import net.anzix.imprempta.api.TextContent;
import org.junit.Ignore;
import org.junit.Test;

import java.nio.file.Paths;

public class PathTest {

    /**
     * Todo run only on windows
     * @throws Exception
     */
    @Test
    @Ignore
    public void testIsMatch3() throws Exception {
        Path p = new Path("_posts/.*") {
            @Override
            public char getSeparatorChar() {
                return '\\';
            }
        };

        TextContent t1 = new TextContent();
        t1.setSource(Paths.get("_posts/test.html"));

        TextContent t2 = new TextContent();
        t2.setSource(Paths.get("_pages/test.html"));

        Assert.assertTrue(p.isMatch(t1));
        Assert.assertFalse(p.isMatch(t2));
    }

    /**
     * Todo run only on windows
     * @throws Exception
     */
    @Test
    @Ignore
    public void testIsMatch4() throws Exception {
        Path p = new Path("_posts\\\\.*") {
            @Override
            public char getSeparatorChar() {
                return '\\';
            }
        };

        TextContent t1 = new TextContent();
        t1.setSource(Paths.get("_posts/test.html"));

        TextContent t2 = new TextContent();
        t2.setSource(Paths.get("_pages/test.html"));

        Assert.assertTrue(p.isMatch(t1));
        Assert.assertFalse(p.isMatch(t2));
    }
}
