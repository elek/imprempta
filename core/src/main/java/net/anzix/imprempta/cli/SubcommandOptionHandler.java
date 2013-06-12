package net.anzix.imprempta.cli;

import net.anzix.imprempta.Start;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.OptionDef;
import org.kohsuke.args4j.spi.OptionHandler;
import org.kohsuke.args4j.spi.Parameters;
import org.kohsuke.args4j.spi.Setter;

import java.util.ArrayList;
import java.util.List;

public class SubcommandOptionHandler extends OptionHandler<Start.CommandWithArgs> {

    private List<Class<? extends Command>> commands = new ArrayList<>();

    public SubcommandOptionHandler(CmdLineParser parser, OptionDef option, Setter<Start.CommandWithArgs> setter) {
        super(parser, option, setter);
        commands.add(Generate.class);
        commands.add(Extension.class);
    }

    @Override
    public int parseArguments(Parameters params) throws CmdLineException {
        String subCmd = params.getParameter(0);

        for (Class<? extends Command> c : commands) {
            if (c.getSimpleName().toLowerCase().equals(subCmd)) {
                List<String> restOfArgs = new ArrayList<>();
                for (int i = 1; i < params.size(); i++) {
                    restOfArgs.add(params.getParameter(i));
                }
                setter.addValue(new Start.CommandWithArgs(c, restOfArgs));
                return params.size();
            }
        }

        throw new CmdLineException(owner, "No such subcommand " + subCmd);
    }

    @Override
    public String getDefaultMetaVariable() {
        return "cmd";
    }
}
