package net.anzix.imprempta;

import com.google.inject.Inject;
import net.anzix.imprempta.api.*;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashSet;
import java.util.Set;

public class Generator {

    /**
     * Source path.
     */
    private Path dir;
    /**
     * Destination path
     */
    private Path destination;

    private Set<String> excludes = new HashSet<String>();

    @Inject
    ContentWriter writer;

    @Inject
    ContentParser parser;

    @Inject
    Site site;

    @Inject
    Set<Transformer> transformers;


    public Generator() {
        excludes.add(".git");
        excludes.add("_site");
    }

    public void generate() {
        try {
            Files.walkFileTree(dir, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                    if (excludes.contains(dir.getFileName().toString())) {
                        return FileVisitResult.SKIP_SUBTREE;
                    }
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    String fileName = file.getFileName().toString();
                    if (fileName.endsWith("~") || fileName.startsWith(".")) {
                        return FileVisitResult.CONTINUE;
                    }
                    if (Files.isRegularFile(file)) {
                        Content content = parser.parse(file);
                        if (content instanceof Layout) {
                            site.addLayout((Layout) content);
                        } else {
                            site.addContent(content);
                        }
                    }

                    return FileVisitResult.CONTINUE;


                }
            });

        } catch (IOException ex) {
            throw new GeneratorException("Can't read recursive file list", ex);
        }

        //writer makrdown, and add resolution, etc.
        for (Transformer trafo : transformers) {
            for (Content c : site.getContents()) {
                if (c instanceof TextContent) {
                    trafo.transform((TextContent) c);
                }
            }
        }

        //write out
        for (Content c : site.getContents()) {
            Path dest = destination.resolve(c.getSource());
            String newName = c.getMeta(Header.NAME) + "." + c.getMeta(Header.TYPE);
            writer.render(c, dir, destination.resolve(dest.getParent().resolve(newName)));

        }

    }

    public void setRoot(String arg) {
        this.dir = Paths.get(arg);
    }

    public void setDestination(String destination) {
        this.destination = Paths.get(destination).toAbsolutePath();
    }


}
