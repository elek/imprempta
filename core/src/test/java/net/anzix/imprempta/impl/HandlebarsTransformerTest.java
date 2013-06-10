package net.anzix.imprempta.impl;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.MissingValueResolver;
import com.github.jknack.handlebars.Template;
import junit.framework.Assert;
import net.anzix.imprempta.api.Layout;
import net.anzix.imprempta.api.Site;
import net.anzix.imprempta.api.TextContent;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Paths;

public class HandlebarsTransformerTest {


    @Test
    public void hbtest() throws IOException {
        Handlebars handlebars = new Handlebars();
        handlebars.with(new MissingValueResolver() {
            @Override
            public String resolve(Object context, String var) {
                System.out.println(var);
                return "xx";
            }

            ;
        });
        Template template = handlebars.compileInline("Hello {{tqhis}}!");

        System.out.println(template.apply("Handlebars.java"));
    }

    @Test
    public void testTransform() throws Exception {
        YamlHeaderContentParser parser = new YamlHeaderContentParser(Paths.get("."));
        Layout l = (Layout) parser.parse(Paths.get("src/test/resources/_layouts/handlebar_layout.html"));
        TextContent page = (TextContent) parser.parse(Paths.get("src/test/resources/post.html"));

        Site s = new Site();
        s.addLayout(l);


        HandlebarsTransformer transformer = new HandlebarsTransformer();
        transformer.setSite(s);

        String rendered = transformer.transform(l, page);
        System.out.println(rendered);
        Assert.assertTrue(rendered.contains("<body>"));
        Assert.assertTrue(rendered.contains("vasparittya"));
        Assert.assertTrue(rendered.contains("<a>"));


    }

    @Test
    public void error() throws Exception {
        YamlHeaderContentParser parser = new YamlHeaderContentParser(Paths.get("."));
        Layout l = (Layout) parser.parse(Paths.get("src/test/resources/_layouts/handlebar_layout.html"));
        TextContent page = (TextContent) parser.parse(Paths.get("src/test/resources/post3.html"));

        Site s = new Site();
        s.addLayout(l);


        HandlebarsTransformer transformer = new HandlebarsTransformer();

        transformer.setSite(s);

        String rendered = transformer.transform(l, page);
        System.out.println(rendered);
        Assert.assertTrue(rendered.contains("<body>"));
        Assert.assertTrue(rendered.contains("bbb"));
        Assert.assertTrue(rendered.contains("<a>"));


    }

    @Test
    public void recursive() throws Exception {
        YamlHeaderContentParser parser = new YamlHeaderContentParser(Paths.get("."));
        TextContent page = (TextContent) parser.parse(Paths.get("src/test/resources/post2.html"));

        Layout l;
        Site s = new Site();
        s.addLayout((Layout) parser.parse(Paths.get("src/test/resources/_layouts/handlebar_layout.html")));
        s.addLayout(l = (Layout) parser.parse(Paths.get("src/test/resources/_layouts/test.html")));


        HandlebarsTransformer transformer = new HandlebarsTransformer();
        transformer.setSite(s);

        transformer.transform(page);
        String rendered = page.getContent();
        System.out.println(rendered);
        Assert.assertTrue(rendered.contains("<body>"));
        Assert.assertTrue(rendered.contains("test"));
        Assert.assertTrue(rendered.contains("<a>"));
    }

    @Test
    public void pagelist() throws Exception {
        YamlHeaderContentParser parser = new YamlHeaderContentParser(Paths.get("."));
        TextContent page = (TextContent) parser.parse(Paths.get("src/test/resources/post4.html"));

        Layout l;
        Site s = new Site();
        s.addLayout((Layout) parser.parse(Paths.get("src/test/resources/_layouts/handlebar_layout.html")));
        s.addLayout(l = (Layout) parser.parse(Paths.get("src/test/resources/_layouts/test.html")));
        s.addContent(page);
        s.setBaseurl("bbbb");


        HandlebarsTransformer transformer = new HandlebarsTransformer();
        transformer.setSite(s);

        transformer.transform(page);
        String rendered = page.getContent();
        System.out.println(rendered);
        Assert.assertTrue(rendered.contains("<body>"));
        Assert.assertTrue(rendered.contains("qwetitle"));
        Assert.assertTrue(rendered.contains("bbbb"));


    }
}
