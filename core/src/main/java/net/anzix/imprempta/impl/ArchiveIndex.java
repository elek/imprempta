package net.anzix.imprempta.impl;

import com.google.inject.Inject;
import net.anzix.imprempta.api.*;

import java.util.Comparator;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

/**
 * Create site wide class -> year -> month -> contents map.
 */
public class ArchiveIndex implements Transformer {

    @Inject
    Site site;


    @Override
    public void transform(TextContent content) {
        String contentClass = (String) content.get(Header.CLASS);
        Date date = (Date) content.get(Header.DATE);
        if (contentClass != null && date != null) {
            indexContent(contentClass, "" + (date.getYear() + 1900), "" + (date.getMonth() + 1), content);
        }

    }

    private void indexContent(String contentClass, String year, String month, TextContent content) {
        if (!site.containsKey("archive")) {
            site.put("archive", new TreeMap<String, Map<String, Map<String, Content>>>());
        }
        Map<String, Map<String, Map<String, Content>>> contents = (Map<String, Map<String, Map<String, Content>>>) site.get("archive");
        if (!contents.containsKey(contentClass)) {
            contents.put(contentClass, new TreeMap<String, Map<String, Content>>());
        }
        Map<String, Map<String, Content>> byClass = contents.get(contentClass);
        if (!byClass.containsKey(year)) {
            byClass.put(year, new TreeMap<String, Content>(new Comparator<String>() {
                @Override
                public int compare(String o1, String o2) {
                    try {
                        return Integer.valueOf(o1).compareTo(Integer.valueOf(o2));
                    } catch (Exception ex) {
                        return -1;
                    }
                }

                @Override
                public boolean equals(Object obj) {
                    return false;  //To change body of implemented methods use File | Settings | File Templates.
                }
            }));
        }
        Map<String, Content> byMonth = byClass.get(year);
        byMonth.put(month, content);
    }


    public void setSite(Site site) {
        this.site = site;
    }
}
