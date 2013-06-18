package net.anzix.imprempta.impl;

import com.google.inject.Inject;
import net.anzix.imprempta.api.Header;
import net.anzix.imprempta.api.Site;
import net.anzix.imprempta.api.TextContent;
import net.anzix.imprempta.api.Transformer;

import java.util.Date;

/**
 * update the date of the youngest content to the site.
 */
public class SiteUpdated implements Transformer {

    @Inject
    Site site;

    @Override
    public void transform(TextContent content) {
        Date currentSiteDate = (Date) site.get(Header.DATE);
        Date pageDate = (Date) content.get(Header.DATE);
        if (pageDate != null) {
            if (currentSiteDate == null || currentSiteDate.compareTo(pageDate) < 0) {
                site.put(Header.DATE, pageDate);
            }
        }
    }
}
