package net.anzix.imprempta;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import com.google.inject.Guice;
import com.google.inject.Injector;
import net.anzix.imprempta.api.Site;
import net.anzix.imprempta.cli.Command;
import net.anzix.imprempta.cli.SubcommandOptionHandler;
import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class Start {

    @Option(name = "-v", usage = "Verbose mode")
    private boolean verbose;

    @Option(name = "-s", usage = "Source directory")
    private String rootdir = ".";

    @Argument(metaVar = "command", handler = SubcommandOptionHandler.class, required = true)
    CommandWithArgs commandType;

    public static void main(final String args[]) {


        Start s = new Start();
        CmdLineParser parser = new CmdLineParser(s);
        try {
            parser.parseArgument(args);
            s.execute();

        } catch (CmdLineException e) {
            System.err.println("ERROR: " + e.getMessage());
            System.err.print("Usage:\n   java -jar imprempta.jar ");
            parser.printSingleLineUsage(System.err);
        }


    }

    private void execute() {
        Logger l = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        if (verbose) {
            l.setLevel(Level.DEBUG);
        } else {
            l.setLevel(Level.INFO);
        }
        Injector i = Guice.createInjector(new GuiceConfig(rootdir));
        i.getInstance(Site.class).setSourceDir(rootdir);
        Command gen = i.getInstance(commandType.command);
        CmdLineParser p = new CmdLineParser(gen);
        try {
            p.parseArgument(commandType.args);
            gen.execute();
        } catch (CmdLineException e) {
            System.err.println("ERROR: " + e.getMessage());
            System.err.print("Usage:\n");
            p.printUsage(System.err);
        }


    }

    public static class CommandWithArgs {
        public Class<? extends Command> command;
        public List<String> args = new ArrayList<>();

        public CommandWithArgs(Class<? extends Command> command, List<String> args) {
            this.command = command;
            this.args = args;
        }
    }

}
