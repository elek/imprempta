package net.anzix.imprempta.api;

import java.util.Map;

/**
 * Template language for resolving template elements.
 */
public interface TemplateLanguage {
    public String render(TextContent content, Map<String, Object> context);
}
