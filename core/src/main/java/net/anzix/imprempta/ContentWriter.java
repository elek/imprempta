package net.anzix.imprempta;

import net.anzix.imprempta.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

/**
 * Render per content.
 */
public class ContentWriter {
    public static Logger LOG = LoggerFactory.getLogger(ContentWriter.class);

    public void render(Content content, Path fromDir, Path destination) {
        try {
            System.out.println(content.getMeta(Header.NAME));
            System.out.println(content.getUrl());
            LOG.debug("Generating output to {}", destination);
            Files.createDirectories(destination.getParent());
            if (content instanceof TextContent) {
                Files.write(destination, ((TextContent) content).getContent().getBytes());
            } else if (content instanceof BinaryContent) {
                Files.copy(fromDir.resolve(content.getSource()), destination, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            throw new GeneratorException("Can't write file: " + destination, e);
        }
    }
}
