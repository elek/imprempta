package net.anzix.imprempta;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.MapBinder;
import com.google.inject.multibindings.Multibinder;
import com.google.inject.name.Names;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import net.anzix.imprempta.api.*;
import net.anzix.imprempta.impl.*;
import org.antlr.v4.runtime.misc.MultiMap;
import org.codehaus.groovy.control.CompilerConfiguration;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;

/**
 * Guice component wiring.
 */
public class GuiceConfig extends AbstractModule {


    String rootdir;

    MultiMap<Class, ClassWithRole> extensions = new MultiMap<>();

    public GuiceConfig() {
    }

    public GuiceConfig(String rootdir) {
        this.rootdir = rootdir;
    }

    @Override
    protected void configure() {

        bind(String.class).annotatedWith(Names.named("sourcedir")).toInstance(rootdir);
        bind(Site.class).asEagerSingleton();
        bind(ContentWriter.class).asEagerSingleton();
        bind(ContentParser.class).to(YamlHeaderContentParser.class).asEagerSingleton();

        addExtension(Transformer.class, NoTransformer.class, Phase.PARSE);
        addExtension(Transformer.class, SyntaxTransformer.class, Phase.SYNTAX);
        addExtension(Transformer.class, HandlebarsTransformer.class, Phase.TEMPLATE);

        addExtension(Syntax.class, MarkdownSyntax.class, "md");
        addExtension(Syntax.class, MarkdownSyntax.class, "markdown");
        addExtension(Syntax.class, SimpleSyntax.class, "js");
        addExtension(Syntax.class, SimpleSyntax.class, "css");
        addExtension(Syntax.class, SimpleSyntax.class, "html");

        readConfig();


        for (Class iface : extensions.keySet()) {
            MapBinder<String, Object> mapBinder = MapBinder.newMapBinder(binder(), String.class, iface);
            Multibinder binder = Multibinder.newSetBinder(binder(), iface);
            for (ClassWithRole impl : extensions.get(iface)) {
                if (impl.role != null) {
                    mapBinder.addBinding(impl.role).to(impl.type);
                }
                binder.addBinding().to(impl.type);
            }
        }
        bind(GuiceConfig.class).toInstance(this);

    }

    private <T> void addExtension(Class<T> iface, Class<? extends T> implementation, String parse) {
        extensions.map(iface, new ClassWithRole(implementation, parse));
    }

    void readConfig() {
        File config = new File(new File(rootdir), "imprempta.groovy");
        if (config.exists()) {
            Binding binding = new Binding();
            binding.setVariable("binder", this);

            CompilerConfiguration conf = new CompilerConfiguration();
            conf.setVerbose(true);
            GroovyShell shell = new GroovyShell(binding, conf);

            try {
                String s = new Scanner(new FileInputStream(config)).useDelimiter("\\Z").next();
                s = "import net.anzix.imprempta.api.*;\n" +
                        "import net.anzix.imprempta.impl.*;\n" +
                        "import static net.anzix.imprempta.api.Phase.*;\n\n" + s;
                shell.evaluate(s);
            } catch (IOException e) {
                throw new GeneratorException("Can't execute config file", e);
            }

        }
    }

    public Usage use(Class type) {
        return new Usage(type);
    }


    public static class ClassWithRole {
        public Class type;
        public String role;

        ClassWithRole(Class type) {
            this.type = type;
        }

        ClassWithRole(Class type, String role) {
            this.type = type;
            this.role = role;
        }
    }

    class Usage {

        private Class implementation;

        private Class iface = null;

        private String role;

        private Usage(Class implementation) {
            this.implementation = implementation;
        }

        public void after(String role) {
            if (iface == null) {
                throw new GeneratorException("Please set the type of an extension with use(ClassName.class).as(ExtensionType.class)...");
            }
            int ix = -1;
            int i = 0;
            for (ClassWithRole type : extensions.get(iface)) {
                if (role.equals(type.role)) {
                    ix = i;
                    break;
                }
                i++;
            }
            if (ix == -1) {
                throw new IllegalArgumentException("No such element with the role " + role + " in the extension list of " + iface.getSimpleName());
            }
            ix++;
            extensions.get(iface).add(ix, new ClassWithRole(implementation));
        }

        public Usage as(Class iface) {
            this.iface = iface;
            return this;
        }
    }

    public MultiMap<Class, ClassWithRole> getExtensions() {
        return extensions;
    }
}

