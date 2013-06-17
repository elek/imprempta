package net.anzix.imprempta.api;

import net.anzix.imprempta.api.selector.ContentSelector;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Interface to get the plugin implementations.
 */
public class ExtensionChain<T> {

    private List<Extension<T>> extensions = new ArrayList<>();

    /**
     * Return all of the implementation for a specific content.
     */
    public List<T> getAll(Content content) {
        ArrayList<T> result = new ArrayList<>();
        for (Extension<T> ext : getAllExtension(content)) {
            result.add(ext.instance);
        }
        return result;
    }

    public List<Extension<T>> getAllExtension(Content content) {
        ArrayList<Extension<T>> result = new ArrayList<>();
        for (Extension<T> ext : extensions) {
            if (ext.isActiveFor(content)) {
                result.add(ext);
            }
        }
        return result;
    }

    public T getFirstMatch(Content content) {
        //todo optimze it
        List<T> all = getAll(content);
        if (all.size() == 0) {
            return null;
        } else {
            return all.get(0);
        }
    }

    public void add(T implementation) {
        extensions.add(new Extension<T>(implementation));

    }

    public void add(T implementation, String role) {
        extensions.add(new Extension<T>(implementation, role));
    }

    public void add(T implementation, String role, ContentSelector sel) {
        extensions.add(new Extension<T>(implementation, role, sel));
    }


    public void addAfterRole(String role, Class implementation) {
    }

    public List<Extension<T>> getExtensions() {
        return extensions;
    }

    public Set<String> getRoles() {
        Set<String> roles = new HashSet<>();
        for (Extension<T> extension : extensions) {
            if (extension.role != null) {
                roles.add(extension.role);
            }
        }
        return roles;
    }


}
