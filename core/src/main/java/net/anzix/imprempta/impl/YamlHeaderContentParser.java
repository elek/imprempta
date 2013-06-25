package net.anzix.imprempta.impl;

import com.google.common.io.Files;
import com.google.inject.Inject;
import net.anzix.imprempta.api.*;
import org.yaml.snakeyaml.Yaml;

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

    private ExtensionManager ext;

    private Site site;

    @Inject
    public YamlHeaderContentParser(ExtensionManager extMan, Site site) {
        this(Paths.get(site.getSourceDir()));
        this.ext = extMan;
    }

    /**
     * constructor for testing
     *
     * @param rootDir
     */
    public YamlHeaderContentParser(Path rootDir) {
        this.rootDir = rootDir;
        this.ext = new ExtensionManager();
        ext.use(PegdownSyntax.class).withRole("md");
        ext.use(PegdownSyntax.class).withRole("markdown");
        ext.use(SimpleSyntax.class).withRole("html");
    }

    @Override
    public Content parse(Path file) {
        String fileExtension = Files.getFileExtension(file.getFileName().toString());
        String parentDir = file.getParent().getFileName().toString();
        if (parentDir.equals("_layouts")) {
            Layout c = new Layout();
            loadTextContent(file, c);
            return c;
        } else if (this.ext.getExtensionChain(Syntax.class).getRoles().contains(fileExtension)) {
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

                //save parsed attributes
                Map<String, Object> map = (Map<String, Object>) yaml.load(sb.toString());
                for (String key : map.keySet()) {
                    c.put(key, map.get(key));
                }
            }
            while (lineno < lines.size()) {
                content.append(lines.get(lineno) + "\n");
                lineno++;
            }
            c.setContent(content.toString());
            c.setSource(rootDir.toAbsolutePath().relativize(file.toAbsolutePath()));
        } catch (IOException e) {
            throw new GeneratorException("Can't open file: " + file.toAbsolutePath().toString(), e);
        }
    }
}
