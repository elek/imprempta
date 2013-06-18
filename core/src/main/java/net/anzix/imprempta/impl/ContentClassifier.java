package net.anzix.imprempta.impl;

import com.google.inject.Inject;
import net.anzix.imprempta.api.*;
import net.anzix.imprempta.api.header.HeaderExtension;

import java.util.*;

/**
 * Create helper variables in site to access content per type.
 */
public class ContentClassifier implements Transformer {

    @Inject
    Site site;

    @Override
    public void transform(TextContent content) {
        String class_ = (String) content.get(Header.CLASS);
        if (class_ != null) {
            String key = useListNameConvention(class_);
            String headerKey = useHeaderNameConvention(class_);
            if (site.get(key) == null) {
                site.put(key, new ArrayList<Content>());
            }
            if (site.get(headerKey) == null) {
                site.put(headerKey, new HashSet<HeaderExtension>());
            }

            //content list
            List<Content> list = ((List<Content>) site.get(key));
            list.add(content);

            //TODO refactor it to make it faster
            Collections.sort(list, new Comparator<Content>() {
                @Override
                public int compare(Content o1, Content o2) {
                    Date d1 = (Date) o1.get(Header.DATE);
                    Date d2 = (Date) o2.get(Header.DATE);
                    if (d1 == null) {
                        return 1;
                    } else if (d2 == null) {
                        return -1;
                    } else {
                        return d1.compareTo(d2) * -1;
                    }
                }
            });

            //header list
            HashSet<HeaderExtension> extensions = (HashSet<HeaderExtension>) site.get(headerKey);
            for (HeaderExtension ext : content.getHeaderExtensions()) {
                extensions.add(ext);
            }
        }
    }

    public String useListNameConvention(String contentClass) {
        return contentClass + "List";
    }

    public String useHeaderNameConvention(String contentClass) {
        return contentClass + "HeaderExtensions";
    }
}
