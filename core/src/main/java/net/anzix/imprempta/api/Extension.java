package net.anzix.imprempta.api;

import net.anzix.imprempta.api.selector.All;
import net.anzix.imprempta.api.selector.ContentSelector;

/**
 * One extension element.
 */
public class Extension<T> {

    public T instance;

    public String role;

    public ContentSelector selector;

    public Extension(T instance) {
        this.instance = instance;
        this.selector = new All();
    }

    public Extension(T instance, String role) {
        this.instance = instance;
        this.role = role;
        this.selector = new All();
    }

    public Extension(T instance, String role, ContentSelector selector) {
        this.instance = instance;
        this.role = role;
        this.selector = selector;
        if (this.selector == null) {
            this.selector = new All();
        }
    }

    public boolean isActiveFor(Content c) {
        return selector.isMatch(c);
    }

    @Override
    public String toString() {
        return instance.getClass().getSimpleName() + (role == null ? " " : " [" + role + "] ") + (selector == null ? "" : "{" + selector + "}");
    }
}
