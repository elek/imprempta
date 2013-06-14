package net.anzix.imprempta.api;

import java.nio.file.Paths;
import java.util.Scanner;

/**
 * Text content, sould be transformed for output.
 */
public class TextContent extends Content {

    private String content = "";

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
}
