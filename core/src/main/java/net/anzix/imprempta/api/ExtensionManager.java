package net.anzix.imprempta.api;

import com.google.inject.Inject;
import com.google.inject.Injector;
import net.anzix.imprempta.api.selector.ContentSelector;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class to manage all of the extensions.
 */
public class ExtensionManager {

    private Usage current;

    Map<Class, ExtensionChain> extensions = new HashMap<>();

    public ExtensionManager() {
    }

    @Inject
    Injector inject;

    public Map<Class, ExtensionChain> getExtensions() {
        return extensions;
    }

    public ExtensionChain getExtensions(Class type) {
        return extensions.get(type);
    }

    public <T> ExtensionChain<T> getExtensionChain(Class<T> type) {
        if (extensions.get(type) == null) {
            extensions.put(type, new ExtensionChain<T>());
        }
        return extensions.get(type);
    }


    public Usage use(Class type) {
        closeUsage();
        return current = new Usage(type);
    }

    public Usage use(Object instance) {
        closeUsage();
        return current = new Usage(instance);
    }

    public void closeUsage() {
        if (current != null) {
            current.add();
        }
    }

    public void guicify(Injector i) {
        this.inject = i;
        for (ExtensionChain chain : extensions.values()) {
            List<Extension> exts = chain.getExtensions();
            for (Extension ext : exts) {
                i.injectMembers(ext.instance);
            }
        }
    }


    public class Usage {

        private Object implementation;

        private Class iface = null;

        private String role;

        private ContentSelector selector;

        private Usage(Object implementation) {
            this.implementation = implementation;
        }

        private Usage(Class type) {
            try {
                this.implementation = type.newInstance();
            } catch (InstantiationException e) {
                throw new GeneratorException("Can't instatiate " + type, e);
            } catch (IllegalAccessException e) {
                throw new GeneratorException("Can't instatiate " + type, e);
            }
        }

        public void add() {
            if (iface == null) {
                iface = detectIface(implementation);
            }
            ExtensionChain chain = extensions.get(iface);
            if (chain == null) {
                chain = new ExtensionChain();
                extensions.put(iface, chain);
            }
            chain.add(implementation, role, selector);
        }

        private Class detectIface(Object implementation) {
            return implementation.getClass().getInterfaces()[0];
        }

        public void after(String role) {
            if (iface == null) {
                throw new GeneratorException("Please set the type of an extension with use(ClassName.class).as(ExtensionType.class)...");
            }
            //TODO
        }

        public void after(Class type) {
            if (iface == null) {
                iface = detectIface(implementation);
            }
            if (iface == null) {
                throw new GeneratorException("Please set the type of an extension with use(ClassName.class).as(ExtensionType.class)...");
            }
            ExtensionChain chain = extensions.get(iface);
            chain.addAdter(type, implementation, role, selector);
        }

        public Usage as(Class iface) {
            this.iface = iface;
            return this;
        }

        public Usage withRole(String role) {
            this.role = role;
            return this;
        }

        public Usage forContent(ContentSelector selector) {
            this.selector = selector;
            return this;
        }


    }

    public void setInject(Injector inject) {
        this.inject = inject;
    }
}
