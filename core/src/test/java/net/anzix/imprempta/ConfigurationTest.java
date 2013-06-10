package net.anzix.imprempta;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import org.codehaus.groovy.control.CompilerConfiguration;

public class ConfigurationTest {

    public static void main(String[] args) {
        new ConfigurationTest().run();
    }

    private void run() {
        Configuration c = new Configuration();

        Binding binding = new Binding();
        binding.setVariable("binder", this);

        CompilerConfiguration conf = new CompilerConfiguration();
        conf.setVerbose(true);

        GroovyShell shell = new GroovyShell(binding);
        shell.evaluate("asd = 'qwe'");
        for (Object o : shell.getContext().getVariables().keySet()) {
            System.out.println(o);
        }
    }

    public String qwe() {
        return "a";
    }
}
