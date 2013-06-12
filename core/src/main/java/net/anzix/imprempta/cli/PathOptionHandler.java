package net.anzix.imprempta.cli;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.OptionDef;
import org.kohsuke.args4j.spi.OptionHandler;
import org.kohsuke.args4j.spi.Parameters;
import org.kohsuke.args4j.spi.Setter;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Option handler to parse Path.
 */
public class PathOptionHandler extends OptionHandler<Path> {

    public PathOptionHandler(CmdLineParser parser, OptionDef option, Setter<Path> setter) {
        super(parser, option, setter);
    }

    @Override
    public int parseArguments(Parameters params) throws CmdLineException {
        setter.addValue(Paths.get(params.getParameter(0)));
        return 1;
    }

    @Override
    public String getDefaultMetaVariable() {
        return "path";
    }
}
