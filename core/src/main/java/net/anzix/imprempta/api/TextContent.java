package net.anzix.imprempta.api;

import net.anzix.imprempta.api.header.HeaderExtension;

import java.nio.file.Paths;
import java.util.*;

/**
 * Text content, sould be transformed for output.
 */
public class TextContent extends Content {

    private String content = "";

    private Set<HeaderExtension> headerExtensions = new HashSet<>();

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public static TextContent fromResource(String resource, String logicalPath) {
        TextContent c = new TextContent();
        c.setContent(new Scanner(TextContent.class.getResourceAsStream(resource)).useDelimiter("\\Z").next());
        c.setSource(Paths.get(logicalPath));
        return c;
    }

    public void addHeaderExtension(HeaderExtension ext) {
        headerExtensions.add(ext);
    }

    public List<HeaderExtension> getHeaderExtensions() {
        return new ArrayList(headerExtensions);
    }

    public String getHeaderExtension() {
        StringBuilder b = new StringBuilder();
        for (HeaderExtension hex : headerExtensions) {
            b.append("   " + hex.render(getRootUrl()) + "\n");
        }
        return b.toString();
    }
}
