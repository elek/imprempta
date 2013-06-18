package net.anzix.imprempta;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import org.codehaus.groovy.control.CompilerConfiguration;

public class ConfigurationTest {

    public static void main(String[] args) {
        new ConfigurationTest().run();
    }

    private void run() {

        Binding binding = new Binding();
        binding.setVariable("binder", this);

        CompilerConfiguration conf = new CompilerConfiguration();
        conf.setVerbose(true);
        conf.setScriptBaseClass("net.anzix.imprempta.impl.Valami");

        GroovyShell shell = new GroovyShell(this.getClass().getClassLoader(), new Binding(), conf);
        Object res = shell.evaluate("ahoj()");
        System.out.println(res);
        for (Object o : shell.getContext().getVariables().keySet()) {
            System.out.println(o);
        }
    }

    public String qwe() {
        return "a";
    }


}
