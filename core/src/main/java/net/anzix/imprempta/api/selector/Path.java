package net.anzix.imprempta.api.selector;

import net.anzix.imprempta.api.Content;

import java.io.File;

/**
 * Select content based the source path.
 */
public class Path implements ContentSelector {

    private String regexp;
    private char separatorChar;

    public Path(String path) {
        regexp = path;
        char sep = getSeparatorChar();
        if (sep == '\\') {
            regexp = regexp.replaceAll("/", "\\\\\\\\");
        } else {
            regexp = regexp.replaceAll("\\\\", "/");
        }
    }

    @Override
    public boolean isMatch(Content c) {
        return c.getSource().toString().matches(regexp);
    }

    @Override
    public String toString() {
        return "path ~ " + regexp;
    }

    public char getSeparatorChar() {
        return File.separatorChar;
    }
}
