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
    public String render(String rootUrl) {
        return "<script>" + content + "</script>";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ScriptContent that = (ScriptContent) o;

        if (content != null ? !content.equals(that.content) : that.content != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return content != null ? content.hashCode() : 0;
    }
}
