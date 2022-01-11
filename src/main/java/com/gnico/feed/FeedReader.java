package com.gnico.feed;

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.sun.syndication.feed.synd.SyndEnclosure;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;


public class FeedReader {
    
    private List<NewsEntry> newsEntries = new ArrayList<>();
    private String feedLink;
    
    public FeedReader(String link) {
        feedLink = link;
    }
    
    public List<NewsEntry> fetch() throws CouldNotFetchException {
        try {
            URL feedSource = new URL(this.feedLink);
            SyndFeedInput input = new SyndFeedInput();
            SyndFeed feed = input.build(new XmlReader(feedSource)); 
            
            @SuppressWarnings("rawtypes")
            final Iterator iter = feed.getEntries().iterator();
            while (iter.hasNext()) {
                final SyndEntry entry = (SyndEntry) iter.next();
                SyndEnclosure enclosure = (SyndEnclosure) entry.getEnclosures().get(0);
                String img = enclosure.getUrl();
                NewsEntry ne = new NewsEntry(
                        entry.getTitle(),
                        entry.getDescription().getValue(),
                        entry.getLink(),
                        img,
                        entry.getPublishedDate()
                );
                this.newsEntries.add(ne);              
            }                  
        } catch (Exception e) {
            throw new CouldNotFetchException("Could not get RSS feed", e);
        } 
        return this.newsEntries;
    }

}
