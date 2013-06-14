package net.anzix.imprempta.api.header;

/**
 * Javascript content.
 */
public class ScriptContent implements HeaderExtension {

    private String content;

    public ScriptContent(String content) {
        this.content = content;
    }

    @Override
    public String render() {
        return "<script>" + content + "</script>";
    }
}
