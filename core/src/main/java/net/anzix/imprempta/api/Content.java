package net.anzix.imprempta.api;

import com.google.common.io.Files;

import java.io.File;
import java.nio.file.Path;
import java.util.*;

/**
 * Class represents one static page with a content a metadata.
 */
public abstract class Content {

    /**
     * Files which are source of this generated page.
     */
    private List<File> sourceFiles = new ArrayList<>();

    private Map<String, Object> metadaata = new HashMap<>();

    private Path source;
    private Object metadata;

    public Object getMeta(String key) {
        return metadaata.get(key);
    }

    public Object getMeta(Header key) {
        return metadaata.get(key.toString().toLowerCase());
    }

    public void setMeta(String key, Object o) {
        metadaata.put(key, o);
    }

    public void setMeta(Header key, Object o) {
        metadaata.put(key.toString().toLowerCase(), o);
    }

    public void setSource(Path source) {
        this.source = source;
        setMeta(Header.TYPE, Files.getFileExtension(source.getFileName().toString().toLowerCase()));
        setMeta(Header.NAME, Files.getNameWithoutExtension(source.getFileName().toString()));
        setMeta(Header.PARENT, source.getParent());

    }

    public Path getSource() {
        return source;
    }

    public void delMeta(String layout) {
        metadaata.remove(layout);
    }

    public Collection<String> getMetaKeys() {
        return metadaata.keySet();
    }

    public Map<String, Object> getMetadata() {
        return metadaata;
    }


    public String getUrl() {
        Path p = (Path) getMeta(Header.PARENT);
        String name = getMeta(Header.NAME) + "." + getMeta(Header.TYPE);
        if (p != null) {
            return p.resolve(name).toString();
        } else {
            return name;
        }

    }

    public String getRootUrl() {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < countOccurrences(getUrl(), File.separatorChar); i++) {
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

    public void setMetaHolder(Map<String, Object> metaHolder) {
        this.metadaata = metaHolder;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[" + source + ']';
    }
}
