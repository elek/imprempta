package net.anzix.imprempta.cli;

import com.google.inject.Inject;
import net.anzix.imprempta.api.ExtensionChain;
import net.anzix.imprempta.api.ExtensionManager;
import org.antlr.v4.runtime.misc.MultiMap;

import java.util.List;
import java.util.Map;

public class Extension implements Command {

    @Inject
    ExtensionManager ext;

    @Override
    public void execute() {
        System.out.println("Extension points:");
        Map<Class, ExtensionChain> extensions = ext.getExtensions();
        int c1 = 15, c2 = 35, c3 = 20;
        String tableFormat = "| %-" + c1 + "s | %-" + c2 + "s | %-" + c3 + "s |";
        for (Class type : extensions.keySet()) {
            System.out.println(printCenter("Extension point: " + type.getSimpleName(), c1 + c2 + c3 + 7));
            String.format(tableFormat, "role", "implementation", "selector");
            List<net.anzix.imprempta.api.Extension> exts = extensions.get(type).getExtensions();
            for (net.anzix.imprempta.api.Extension cwr : exts) {
                System.out.println(String.format(tableFormat, cwr.role == null ? "" : cwr.role, cwr.instance.getClass().getSimpleName(), cwr.selector.toString()));
            }
            System.out.println("\n");
        }
    }

    public String printCenter(String s, int w) {
        int k = (w - s.length()) / 2;
        return printSpace(k, '-') + " " + s + " " + printSpace(k, '-');
    }

    public String printSpace(int no, char ch) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < no; i++) {
            sb.append(ch);
        }
        return sb.toString();
    }
}
