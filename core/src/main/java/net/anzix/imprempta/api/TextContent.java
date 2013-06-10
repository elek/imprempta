package net.anzix.imprempta.api;

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


}
