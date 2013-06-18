package net.anzix.imprempta.api.header;

/**
 * Js link in the header.
 */
public class Js implements HeaderExtension {

    private String url;

    public Js(String url) {
        this.url = url;
    }

    @Override
    public String render(String rootUrl) {
        return "<script src=\"" + rootUrl + url + "\"></script>";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Js js = (Js) o;

        if (url != null ? !url.equals(js.url) : js.url != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return url != null ? url.hashCode() : 0;
    }
}
