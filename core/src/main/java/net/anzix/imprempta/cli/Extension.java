package net.anzix.imprempta.cli;

import com.google.inject.Inject;
import net.anzix.imprempta.GuiceConfig;
import org.antlr.v4.runtime.misc.MultiMap;

public class Extension implements Command {

    @Inject
    net.anzix.imprempta.GuiceConfig config;

    @Override
    public void execute() {
        System.out.println("Extension points:");
        MultiMap<Class, GuiceConfig.ClassWithRole> extensions = config.getExtensions();
        int c1 = 15, c2 = 35;
        String tableFormat = "| %-" + c1 + "s | %-" + c2 + "s |";
        for (Class type : extensions.keySet()) {

            System.out.println(printCenter("Extension point: " + type.getSimpleName(), c1 + c2 + 5));
            String.format(tableFormat, "role", "implementation");
            for (GuiceConfig.ClassWithRole cwr : extensions.get(type)) {
                System.out.println(String.format(tableFormat, cwr.role == null ? "" : cwr.role, cwr.type.getSimpleName()));
            }
            System.out.println("\n");
        }
    }

    public String printCenter(String s, int w) {
        int k = (w - s.length()) / 2;
        return printSpace(k , '-') + " " + s + " " + printSpace(k, '-');
    }

    public String printSpace(int no, char ch) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < no; i++) {
            sb.append(ch);
        }
        return sb.toString();
    }
}
