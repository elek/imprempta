package net.anzix.imprempta.api.header;

/**
 * Css link in the header.
 */
public class Css implements HeaderExtension {

    private String url;

    public Css(String url) {
        this.url = url;
    }

    @Override
    public String render() {
        return "<link rel=\"stylesheet\" href=\"" + url + "\"></link>";
    }
}
