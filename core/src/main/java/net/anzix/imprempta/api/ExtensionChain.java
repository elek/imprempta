package net.anzix.imprempta.api;

import com.google.inject.Inject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Interface to get the plugin implementations.
 */
public class ExtensionChain<T> {

    private List<Extension<T>> extensions = new ArrayList<>();

    public List<Class<? extends T>> getAll(Content content) {
        ArrayList<Class<? extends T>> result = new ArrayList<>();
        for (Extension<T> ext : getAllExtension(content)) {
            result.add(ext.type);
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

    public Class<? extends T> getFirstMatch(Content content) {
        //todo optimze it
        List<Class<? extends T>> all = getAll(content);
        if (all.size() == 0) {
            return null;
        } else {
            return all.get(0);
        }
    }

    public void add(Class<? extends T> implementation) {
        extensions.add(new Extension<T>(implementation));

    }

    public void add(Class<? extends T> implementation, String role) {
        extensions.add(new Extension<T>(implementation, role));

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
