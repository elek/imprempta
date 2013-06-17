package net.anzix.imprempta.api.selector;

import net.anzix.imprempta.api.Content;

/**
 * Filter out Content based on metadata property.
 */
public class Prop implements ContentSelector {

    private String property;

    private String value;

    public Prop(String property, String value) {
        this.property = property;
        this.value = value;
    }

    @Override
    public boolean isMatch(Content c) {
        return (c.getMeta(property) != null && c.getMeta(property).equals(value));
    }

    @Override
    public String toString() {
        return property + " = " + value;
    }
}
