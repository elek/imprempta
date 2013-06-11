package net.anzix.imprempta;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import com.google.inject.name.Names;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import net.anzix.imprempta.api.*;
import net.anzix.imprempta.impl.HandlebarsTransformer;
import net.anzix.imprempta.impl.MarkdowTransformer;
import net.anzix.imprempta.impl.NoTransformer;
import net.anzix.imprempta.impl.YamlHeaderContentParser;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

/**
 * Guice component wiring.
 */
public class GuiceConfig extends AbstractModule {

    private String rootdir;

    private Map<Class, List<Class>> extension = new HashMap();

    List<PhasedClass> transformations = new ArrayList<>();

    private Map<String, Class> namedComponents = new HashMap<>();

    public GuiceConfig(String rootdir) {
        this.rootdir = rootdir;
    }

    @Override
    protected void configure() {
        Logger l = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        l.setLevel(Level.INFO);

        bind(Site.class).asEagerSingleton();
        bind(ContentWriter.class).asEagerSingleton();
        bind(ContentParser.class).to(YamlHeaderContentParser.class).asEagerSingleton();

        transformations.add(new PhasedClass(NoTransformer.class, Phase.PARSE));
        transformations.add(new PhasedClass(MarkdowTransformer.class, Phase.SYNTAX));
        transformations.add(new PhasedClass(HandlebarsTransformer.class, Phase.TEMPLATE));

        bind(String.class).annotatedWith(Names.named("rootdir")).toInstance(rootdir);

        readConfig();

        Multibinder<Transformer> binder = Multibinder.newSetBinder(binder(), Transformer.class);
        for (PhasedClass pc : transformations) {
            binder.addBinding().to(pc.type);
        }

//        for (Class iface : extension.keySet()) {
//            Multibinder<Transformer> binder = Multibinder.newSetBinder(binder(), iface);
//            for (Class impl : extension.get(iface)) {
//                binder.addBinding().to(impl);
//            }
//        }


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

    protected void addExtension(Class iface, String name, Class implementation) {
        Class existingImpl = namedComponents.get(name);
        if (existingImpl != null) {
            for (Class ifp : extension.keySet()) {
                List<Class> implementations = extension.get(ifp);
                if (implementations.contains(existingImpl)) {
                    implementations.remove(existingImpl);
                }
            }
        }
        namedComponents.put(name, implementation);
        addExtension(iface, implementation);

    }

    protected void addExtension(Class iface, Class implementation) {
        List<Class> c = extension.get(iface);
        if (c == null) {
            extension.put(iface, c = new ArrayList<Class>());
        }
        c.add(implementation);
    }

    static class PhasedClass {
        Class type;
        Phase phase;

        PhasedClass(Class type) {
            this.type = type;
        }

        PhasedClass(Class type, Phase phase) {
            this.type = type;
            this.phase = phase;
        }
    }

    class Usage {
        private Class implementation;

        private Usage(Class implementation) {
            this.implementation = implementation;
        }

        public void as(Phase phase) {

        }

        public void after(Phase phase) {
            int ix = -1;
            int i = 0;
            for (PhasedClass type : transformations) {
                if (phase.equals(type.phase)) {
                    ix = i;
                    break;
                }
                i++;
            }
            if (ix == -1) {
                throw new IllegalArgumentException("No such element with the phase " + phase);
            }
            ix++;
            transformations.add(ix, new PhasedClass(implementation));
        }
    }
}
