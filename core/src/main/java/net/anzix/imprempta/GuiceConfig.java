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
    private String profile;


    public GuiceConfig() {
    }

    public GuiceConfig(String rootdir, String profile) {
        this.rootdir = rootdir;
        this.profile = profile;
    }


    protected void blogProfile() {
        ext.use(NoTransformer.class).withRole("parse");
        ext.use(new MetadataTransfromer("class", "post")).forContent(path("_posts/.*"));
        ext.use(new MetadataTransfromer("layout", "post")).forContent(path("_posts/.*"));
        ext.use(new JekyllDateParser()).forContent(prop("class", "post"));
        ext.use(new ShortUrlTransformer()).forContent(prop("class", "post"));
        ext.use(SyntaxTransformer.class).withRole("syntax");
        ext.use(ContentClassifier.class);
        ext.use(ArchiveIndex.class);
        ext.use(SiteUpdated.class);
        ext.use(TemplateContentTransformer.class).withRole("template").forContent(not(prop("class", "template")));
        ext.use(TemplateContentTransformer.class).forContent(prop("class", "template"));
        ext.use(TemplateLayoutTransformer.class).withRole("layout");

        ext.use(HandlebarsTemplateLanguage.class).withRole("default");

        ext.use(PegdownSyntax.class).withRole("md");
        ext.use(PegdownSyntax.class).withRole("markdown");
        ext.use(SimpleSyntax.class).withRole("html");
        ext.use(SimpleSyntax.class).withRole("xml");

        ext.use(new HighlightJsHighlighter().withStyle("idea"));
    }

    protected void wikiProfile() {
        ext.use(NoTransformer.class).withRole("parse");
        ext.use(new DateParser("yyyy-MM-dd HH:mm:ss Z"));
        ext.use(SyntaxTransformer.class).withRole("syntax");
        ext.use(ContentClassifier.class);
        ext.use(ArchiveIndex.class);
        ext.use(SiteUpdated.class);
        ext.use(TemplateContentTransformer.class).withRole("template").forContent(not(prop("class", "template")));
        ext.use(TemplateContentTransformer.class).forContent(prop("class", "template"));
        ext.use(TemplateLayoutTransformer.class).withRole("layout");

        ext.use(HandlebarsTemplateLanguage.class).withRole("default");

        ext.use(PegdownSyntax.class).withRole("md");
        ext.use(PegdownSyntax.class).withRole("markdown");
        ext.use(SimpleSyntax.class).withRole("html");

        ext.use(new HighlightJsHighlighter().withStyle("idea"));

    }

    @Override
    protected void configure() {
        if (profile != null) {
            LOG.debug("Starting configuration with profile " + profile);
        }
        bind(String.class).annotatedWith(Names.named("sourcedir")).toInstance(rootdir);
        bind(Site.class).asEagerSingleton();
        bind(ContentWriter.class).asEagerSingleton();
        bind(ContentParser.class).to(YamlHeaderContentParser.class).asEagerSingleton();

        if ("blog".equals(profile)) {
            blogProfile();
        } else {
            wikiProfile();
        }


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

        //extend the config with scripts.
        ext.closeUsage();
        readConfig();
        ext.closeUsage();

        bind(ExtensionManager.class).toInstance(ext);
        bind(GuiceConfig.class).toInstance(this);
    }

    void readConfig() {
        File config = new File(new File(rootdir), "imprempta.groovy");
        if (config.exists()) {
            Binding binding = new Binding();
            binding.setVariable("binder", this);
            binding.setVariable("ext", ext);

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

