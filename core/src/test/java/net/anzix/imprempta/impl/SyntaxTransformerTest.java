package net.anzix.imprempta.impl;

import com.google.inject.Guice;
import com.google.inject.Injector;
import junit.framework.Assert;
import net.anzix.imprempta.TestGuiceConfig;
import net.anzix.imprempta.api.ExtensionManager;
import net.anzix.imprempta.api.Header;
import net.anzix.imprempta.api.TextContent;
import org.junit.Test;

public class SyntaxTransformerTest {

    @Test
    public void testTransform() throws Exception {
        Injector i = Guice.createInjector(new TestGuiceConfig());
        i.getInstance(ExtensionManager.class).guicify(i);


        TextContent c = new TextContent();
        c.setContent("# q\n\nqwe");
        c.put(Header.TYPE, "md");

        i.getInstance(SyntaxTransformer.class).transform(c);

        Assert.assertTrue(c.getContent().contains("h1"));
        Assert.assertEquals("html", c.get(Header.TYPE));

    }
}
