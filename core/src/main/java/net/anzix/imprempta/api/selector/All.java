package net.anzix.imprempta.api.selector;

import net.anzix.imprempta.api.Content;

/**
 * Use all contents.
 */
public class All implements ContentSelector {

    @Override
    public boolean isMatch(Content c) {
        return true;
    }

    @Override
    public String toString() {
        return "*";
    }
}
