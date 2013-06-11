package net.anzix.imprempta;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

/**
 * Starter for workaround for the gradle missing workspace isolation problem.
 */
public class LegacyStarter {

    public static void main(String[] args) throws Exception {
        main(args[0], args[1], args[2]);
    }

    public static void main(String cp, String dir, String dest) throws Exception {
        List<URL> urls = new ArrayList<>();
        for (String path : cp.split(File.pathSeparator)) {
            if (!path.contains("asm") || !path.contains("3.1")) {
                try {
                    File f = new File(path);
                    if (!f.exists()) {
                        throw new FileNotFoundException(f.getAbsolutePath());
                    } else {
                        urls.add(f.toURI().toURL());
                    }
                } catch (MalformedURLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        System.out.println(urls);
        System.out.println(LegacyStarter.class.getClassLoader().getClass());
        URLClassLoader l = new URLClassLoader(urls.toArray(new URL[0]));
        Class c = l.loadClass("net.anzix.imprempta.Start");
        Object params = new String[]{dir, dest};
        c.getMethod("main", String[].class).invoke(null, params);
    }
}
