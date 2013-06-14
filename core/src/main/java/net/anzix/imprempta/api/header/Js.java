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
    public String render() {
        return "<script src=\"" + url + "\"></script>";
    }
}
