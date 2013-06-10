package net.anzix.imprempta;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.multibindings.Multibinder;
import com.google.inject.name.Names;
import net.anzix.imprempta.api.ContentParser;
import net.anzix.imprempta.api.Site;
import net.anzix.imprempta.api.Transformer;
import net.anzix.imprempta.impl.HandlebarsTransformer;
import net.anzix.imprempta.impl.MarkdowTransformer;
import net.anzix.imprempta.impl.SimplifiedLiquidTransformer;
import net.anzix.imprempta.impl.YamlHeaderContentParser;

public class Start {

    public static void main(final String args[]) {
        Injector i = Guice.createInjector(new AbstractModule() {

            @Override
            protected void configure() {
                bind(Site.class).asEagerSingleton();
                bind(ContentWriter.class).asEagerSingleton();
                bind(ContentParser.class).to(YamlHeaderContentParser.class).asEagerSingleton();

                Multibinder<Transformer> binder = Multibinder.newSetBinder(binder(), Transformer.class);
                binder.addBinding().to(MarkdowTransformer.class);
                binder.addBinding().to(HandlebarsTransformer.class);
                bind(String.class).annotatedWith(Names.named("rootdir")).toInstance(args[0]);



            }

        });
        Generator gen = i.getInstance(Generator.class);
        gen.setRoot(args[0]);
        if (args.length > 1) {
            gen.setDestination(args[1]);
        } else {
            gen.setDestination("_site");
        }
        gen.generate();
    }

}
