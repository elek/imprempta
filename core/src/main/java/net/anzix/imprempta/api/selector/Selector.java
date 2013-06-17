package net.anzix.imprempta.api.selector;

/**
 * Static builder methods for the selectors.
 */
public class Selector {

    public static Path path(String path) {
        return new Path(path);
    }

    public static ContentSelector not(ContentSelector selector) {
        return new Not(selector);
    }

    public static ContentSelector prop(String key, String value) {
        return new Prop(key, value);
    }
}
