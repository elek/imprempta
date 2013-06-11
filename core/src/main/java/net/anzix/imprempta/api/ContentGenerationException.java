package net.anzix.imprempta.api;

/**
 * Generic exception which is related with one content.
 */
public class ContentGenerationException extends GeneratorException {

    private Content source;

    public ContentGenerationException(String message, Content source) {
        super(message);
        this.source = source;
    }

    public ContentGenerationException(String message, Throwable cause, Content source) {
        super(message, cause);
        this.source = source;
    }

    public Content getSource() {
        return source;
    }

    public void setSource(Content source) {
        this.source = source;
    }
}
