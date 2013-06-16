package net.anzix.imprempta.api.selector;

import net.anzix.imprempta.api.Content;

/**
 * Select a specific group of Content objects.
 */
public interface ContentSelector {

    public boolean isMatch(Content c);
}
