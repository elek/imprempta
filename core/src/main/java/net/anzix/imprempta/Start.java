package net.anzix.imprempta;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class Start {

    public static void main(final String args[]) {
        Injector i = Guice.createInjector(new GuiceConfig(args[0]));
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
