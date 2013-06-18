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
    public String render(String rootUrl) {
        return "<link rel=\"stylesheet\" href=\"" + rootUrl + url + "\"></link>";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Css css = (Css) o;

        if (url != null ? !url.equals(css.url) : css.url != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return url != null ? url.hashCode() : 0;
    }
}
