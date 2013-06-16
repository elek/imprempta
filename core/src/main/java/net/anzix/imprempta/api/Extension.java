package net.anzix.imprempta.api;

import net.anzix.imprempta.api.selector.All;
import net.anzix.imprempta.api.selector.ContentSelector;

/**
 * One extension element.
 */
public class Extension<T> {

    public Class<? extends T> type;

    public String role;

    public ContentSelector selector;

    public Extension(Class<? extends T> type) {
        this.type = type;
        this.selector = new All();
    }

    public Extension(Class<? extends T> type, String role) {
        this.type = type;
        this.role = role;
        this.selector = new All();
    }

    public Extension(Class<? extends T> type, String role, ContentSelector selector) {
        this.type = type;
        this.role = role;
        this.selector = selector;
    }



    public boolean isActiveFor(Content c) {
        return selector.isMatch(c);
    }
}
