package net.anzix.imprempta.impl;

import com.google.common.io.Files;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import net.anzix.imprempta.api.*;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import static java.nio.charset.Charset.forName;

/**
 * Parse file with mandatory YAML header.
 * <p/>
 * Naiv implementation.
 */
public class YamlHeaderContentParser implements ContentParser {

    private Path rootDir;
    private Map<String, Syntax> syntaxes;
    private Site site;

    @Inject
    public YamlHeaderContentParser(Map<String, Syntax> syntaxes, Site site) {
        this(Paths.get(site.getSourceDir()));
        this.syntaxes = syntaxes;
    }

    public YamlHeaderContentParser(Path rootDir) {
        this.rootDir = rootDir;
    }

    @Override
    public Content parse(Path file) {
        String ext = Files.getFileExtension(file.getFileName().toString());
        String parentDir = file.getParent().getFileName().toString();
        if (parentDir.equals("_layouts")) {
            Layout c = new Layout();
            loadTextContent(file, c);
            return c;
        } else if (syntaxes.keySet().contains(ext.toLowerCase())) {
            TextContent c = new TextContent();
            loadTextContent(file, c);
            return c;
        } else {
            return parseBinaryContent(file);
        }

    }

    Content parseBinaryContent(Path file) {
        Content c = new BinaryContent();
        c.setSource(rootDir.relativize(file));
        return c;
    }

    void loadTextContent(Path file, TextContent c) {
        try {
            c.setSource(rootDir.relativize(file));
            StringBuilder builder = new StringBuilder();
            List<String> lines = java.nio.file.Files.readAllLines(file, forName("UTF-8"));
            int lineno = 0;
            boolean inHeader = false;
            boolean inText = false;
            for (String line : lines) {
                // header
                if (line.trim().matches("-{3,}")) {
                    if (!inText) {
                        if (inHeader) {
                            inHeader = false;
                            inText = true;
                        } else {
                            inHeader = true;
                        }
                    }

                } else {
                    if (inHeader) {
                        int i = line.indexOf(':');
                        if (i == -1) {
                            throw new GeneratorException("YAML header should be in the format 'key: value'. Was: " + line);
                        }
                        c.setMeta(line.substring(0, i).trim(), line.substring(i + 1).trim());
                    } else {
                        if (!inText && line.trim().length() > 0) {
                            inText = true;
                        }
                        builder.append(line + "\n");
                    }
                }
            }
            c.setContent(builder.toString());
        } catch (IOException e) {
            throw new GeneratorException("Can't open file: " + file.toAbsolutePath().toString(), e);
        }
    }
}
