package net.anzix.imprempta.api;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Base class represents all the site data with the data.
 */
public class Site {

    private List<Content> contents = new ArrayList<Content>();

    private Map<String, Layout> layouts = new HashMap<>();

    private String baseurl = "";

    private SimpleDateFormat defaultDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");


    public void addContent(Content content) {
        this.contents.add(content);
    }

    public List<Content> getContents() {
        return contents;
    }

    public void addLayout(Layout layout) {
        String name = (String) layout.getMeta(Header.NAME);
        if (name == null) {
            throw new IllegalArgumentException("Layout doesn't have a name");
        } else {
            layouts.put(name, layout);
        }
    }

    public Layout getLayout(String layoutName) {
        return layouts.get(layoutName);
    }

    public String getBaseurl() {
        return baseurl;
    }

    public List<TextContent> getPages() {
        List<TextContent> pages = new ArrayList();
        for (Content c : contents) {
            if (c instanceof TextContent && !c.isResource()) {
                pages.add((TextContent) c);
            }
        }
        return pages;
    }

    public void setBaseurl(String baseurl) {
        this.baseurl = baseurl;
    }

    public SimpleDateFormat getDefaultDateFormat() {
        return defaultDateFormat;
    }

    public Date getDate(Content content) {
        String date = (String) content.getMeta(Header.DATE);
        if (date != null) {
            try {
                return defaultDateFormat.parse(date);
            } catch (ParseException e) {
                throw new GeneratorException("Can't convert date " + date, e);
            }
        } else {
            return null;
        }

    }
}
