package net.anzix.imprempta.api;

import java.nio.file.Path;

/**
 * Parse a file to content and metadata.
 */
public interface ContentParser {

    public Content parse(Path file);

}
