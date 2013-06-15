package net.anzix.imprempta.impl;

import com.google.common.io.Files;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import net.anzix.imprempta.api.*;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
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

    /**
     * constructor for testing
     *
     * @param rootDir
     */
    public YamlHeaderContentParser(Path rootDir) {
        this.rootDir = rootDir;
        this.syntaxes = new HashMap<>();
        this.syntaxes.put("html", new SimpleSyntax());
        this.syntaxes.put("md", new PegdownSyntax());
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
            StringBuilder content = new StringBuilder();
            List<String> lines = java.nio.file.Files.readAllLines(file, forName("UTF-8"));
            int lineno = 0;
            boolean inHeader = false;
            boolean inText = false;
            while (lines.get(lineno).trim().equals("")) {
                lineno++;
            }

            if (lines.get(lineno).trim().matches("-{3,}")) {
                //yaml header parsing
                StringBuilder sb = new StringBuilder();
                lineno++;
                while (!lines.get(lineno).trim().matches("-{3,}") && lineno < lines.size()) {
                    sb.append(lines.get(lineno) + "\n");
                    lineno++;
                }
                lineno++;
                Yaml yaml = new Yaml();
                Map<String, Object> map = (Map<String, Object>) yaml.load(sb.toString());
                c.setMetaHolder(map);
            }
            while (lineno < lines.size()) {
                content.append(lines.get(lineno) + "\n");
                lineno++;
            }
            c.setContent(content.toString());
        } catch (IOException e) {
            throw new GeneratorException("Can't open file: " + file.toAbsolutePath().toString(), e);
        }
    }
}
