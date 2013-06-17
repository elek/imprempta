package net.anzix.imprempta;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.matcher.AbstractMatcher;
import com.google.inject.matcher.Matcher;
import com.google.inject.matcher.Matchers;
import com.google.inject.name.Names;
import com.google.inject.spi.InjectionListener;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import net.anzix.imprempta.api.*;
import net.anzix.imprempta.impl.*;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import static net.anzix.imprempta.api.selector.Selector.*;

/**
 * Guice component wiring.
 */
public class GuiceConfig extends AbstractModule {

    private static final Logger LOG = LoggerFactory.getLogger(GuiceConfig.class);

    String rootdir;

    ExtensionManager ext = new ExtensionManager();


    /**
     * Type specific parameters.
     */
    Map<Class, Map<String, String>> parameters = new HashMap<>();
    /**
     * Generic parameters
     */
    Map<String, String> genericParameters = new HashMap<>();


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


        ext.use(NoTransformer.class).withRole("parse");
        ext.use(new MetadataTransfromer("class", "post")).forContent(path("_posts/.*"));
        ext.use(new JekyllDateParser()).forContent(prop("class", "post"));
        ext.use(new ShortUrlTransformer()).forContent(prop("class", "post"));
        ext.use(SyntaxTransformer.class).withRole("syntax");
        ext.use(TemplateContentTransformer.class).withRole("template").forContent(not(prop("class", "template")));
        ext.use(TemplateContentTransformer.class).forContent(prop("class", "template"));
        ext.use(TemplateLayoutTransformer.class).withRole("layout");

        ext.use(HandlebarsTemplateLanguage.class).withRole("default");

        ext.use(PegdownSyntax.class).withRole("md");
        ext.use(PegdownSyntax.class).withRole("markdown");
//        ext.use(SimpleSyntax.class).withRole("js");
//        ext.use(SimpleSyntax.class).withRole("css");
        ext.use(SimpleSyntax.class).withRole("html");

        ext.use(new HighlightJsHighlighter().withStyle("idea"));

        Matcher<TypeLiteral<?>> m = new AbstractMatcher<TypeLiteral<?>>() {
            @Override
            public boolean matches(TypeLiteral<?> typeLiteral) {
                return typeLiteral.getRawType().equals(String.class);
            }
        };
        bindListener(Matchers.any(), new TypeListener() {

            @Override
            public <I> void hear(TypeLiteral<I> type, TypeEncounter<I> encounter) {
                encounter.register(new InjectionListener<I>() {
                    @Override
                    public void afterInjection(I injectee) {
                        for (Field f : injectee.getClass().getDeclaredFields()) {
                            if (f.isAnnotationPresent(Parameter.class)) {
                                if (parameters.get(injectee.getClass()) != null) {
                                    String v = parameters.get(injectee.getClass()).get(f.getName());
                                    if (v != null) {
                                        try {
                                            f.setAccessible(true);
                                            f.set(injectee, v);
                                        } catch (IllegalAccessException e) {
                                            throw new GeneratorException("Can't inject parameter " + f.getName() + " to " + injectee, e);
                                        }
                                    }
                                }
                            }
                        }
                    }
                });
            }
        });
        ext.closeUsage();
        readConfig();
        ext.closeUsage();

        //for (Class iface : extensions.keySet()) {
        //LOG.debug("Binding extension " + iface);
        //bind(ExtensionChain.class).annotatedWith(new TypeImpl(iface)).toInstance(extensions.get(iface));
        //}
        bind(ExtensionManager.class).toInstance(ext);
        bind(GuiceConfig.class).toInstance(this);


    }

//    public <T> Class<ExtensionChain<T>> g(Class<T> type){
//        return (Class<ExtensionChain<T>>) ExtensionChain<T>.class;
//    }


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


}

