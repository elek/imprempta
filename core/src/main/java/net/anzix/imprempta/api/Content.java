package net.anzix.imprempta.api;

import com.google.common.io.Files;

import java.io.File;
import java.nio.file.Path;
import java.util.*;

/**
 * Class represents one static page with a content a metadata.
 */
public abstract class Content extends HashMap<String, Object> {

    /**
     * Files which are source of this generated page.
     */
    private List<File> sourceFiles = new ArrayList<>();

    private Path source;
    private Object metadata;

    @Deprecated
    public Object getMeta(String key) {
        return get(key);
    }

    @Deprecated
    public Object getMeta(Header key) {
        return get(key.toString().toLowerCase());
    }

    @Deprecated
    public void setMeta(String key, Object o) {
        put(key, o);
    }

    @Deprecated
    public void setMeta(Header key, Object o) {
        put(key.toString().toLowerCase(), o);
    }

    public void setSource(Path source) {
        this.source = source;
        put(Header.TYPE, Files.getFileExtension(source.getFileName().toString().toLowerCase()));
        put(Header.NAME, Files.getNameWithoutExtension(source.getFileName().toString()));
        put(Header.PARENT, source.getParent());

    }

    public Path getSource() {
        return source;
    }

    @Deprecated
    public void delMeta(String layout) {
        remove(layout);
    }

    @Deprecated
    public Collection<String> getMetaKeys() {
        return keySet();
    }

    @Deprecated
    public Map<String, Object> getMetadata() {
        return this;
    }


    public String getUrl() {
        Path p = (Path) get(Header.PARENT);
        String name = get(Header.NAME) + "." + get(Header.TYPE);
        if (p != null) {
            return p.resolve(name).toString().replace('\\', '/');
        } else {
            return name.replace('\\', '/');
        }

    }

    public String getRootUrl() {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < countOccurrences(getUrl(), '/'); i++) {
            s.append("../");
        }
        return s.toString();
    }

    public static int countOccurrences(String haystack, char needle) {
        int count = 0;
        for (int i = 0; i < haystack.length(); i++) {
            if (haystack.charAt(i) == needle) {
                count++;
            }
        }
        return count;
    }

    public boolean isResource() {
        String ext = (String) getMeta(Header.TYPE);
        return ext != null && (ext.equals("css") || ext.equals("js"));
    }


    @Override
    public String toString() {
        return getClass().getSimpleName() + "[" + source + ']';
    }

    public Object get(Header header) {
        return get(header.name().toLowerCase());
    }

    public void put(Header header, Object o) {
        put(header.name().toLowerCase(), o);
    }


}
