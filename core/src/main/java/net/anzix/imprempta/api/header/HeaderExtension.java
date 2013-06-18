package net.anzix.imprempta.api.header;

/**
 * Additional records rendered in the header (if the layout supports it).
 */
public interface HeaderExtension {

    public String render(String root);

}
