package net.anzix.imprempta.api.selector;

import net.anzix.imprempta.api.Content;

/**
 * Negate content selector condition.
 */
public class Not implements ContentSelector {

    ContentSelector selector;

    public Not(ContentSelector selector) {
        this.selector = selector;
    }

    @Override
    public boolean isMatch(Content c) {
        return !selector.isMatch(c);
    }

    @Override
    public String toString() {
        if (selector instanceof Prop) {
            return selector.toString().replace("=", "!=");
        } else {
            return "!(" + selector.toString() + ")";
        }
    }
}
