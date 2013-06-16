package net.anzix.imprempta.api;

import java.util.HashMap;
import java.util.Map;

/**
 * Helper class to manage all of the extensions.
 */
public class ExtensionManager {

    Map<Class, ExtensionChain> extensions = new HashMap<>();

    public Map<Class, ExtensionChain> getExtensions() {
        return extensions;
    }

    public  ExtensionChain getExtensions(Class type) {
        return extensions.get(type);
    }

    public <T> ExtensionChain<T> getExtensionChain(Class<T> type) {
        if (extensions.get(type) == null) {
            extensions.put(type, new ExtensionChain<T>());
        }
        return extensions.get(type);
    }

    public void addExtension(Class iface, Class implementation) {
        getExtensionChain(iface).add(implementation);
    }

    public <T> void addExtension(Class<T> iface, Class<? extends T> implementation, String parse) {
        getExtensionChain(iface).add(implementation, parse);
    }

    public Usage use(Class type) {
        return new Usage(type);
    }


    class Usage {

        private Class implementation;

        private Class iface = null;

        private Usage(Class implementation) {
            this.implementation = implementation;
        }

        public void after(String role) {
            if (iface == null) {
                throw new GeneratorException("Please set the type of an extension with use(ClassName.class).as(ExtensionType.class)...");
            }
            getExtensionChain(iface).addAfterRole(role, implementation);
        }

        public Usage as(Class iface) {
            this.iface = iface;
            return this;
        }
    }

}
