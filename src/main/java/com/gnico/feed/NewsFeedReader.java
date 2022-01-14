package com.gnico.feed;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.sun.syndication.feed.synd.SyndEnclosure;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;


public class NewsFeedReader {
    
    private List<NewsEntry> newsEntries = new ArrayList<>();
    private URL feedLink;
    private int timeout;
    
    public NewsFeedReader(URL link) {
        feedLink = link;
        timeout = 0;
    }   
    
    public NewsFeedReader(URL link, int timeout) {
        this.feedLink = link;
        this.timeout = timeout;
    }  
    
    public List<NewsEntry> updateFeed() throws CouldNotFetchException {
        @SuppressWarnings("unchecked")
        List<SyndEntry> entries = this.fetch().getEntries();
        return this.parse(entries);
    }
    
    public SyndFeed fetch() throws CouldNotFetchException {
        try {
            URLConnection urlConnection = feedLink.openConnection();
            urlConnection.setConnectTimeout(timeout);
            urlConnection.setReadTimeout(timeout);           
            SyndFeedInput input = new SyndFeedInput();
            return input.build(new XmlReader(urlConnection));
        } catch (IOException e) {
            throw new CouldNotFetchException("There was a problem reading the stream of the URLConnection", e);
        } catch (IllegalArgumentException e) {
            throw new CouldNotFetchException("Feed type could not be understood", e);
        } catch (FeedException e) {
            throw new CouldNotFetchException("RSS feed could not be parsed", e);
        }
    }
        
    public List<NewsEntry> parse(List<SyndEntry> feedEntries) {
        final Iterator<SyndEntry> iter = feedEntries.iterator();
        while (iter.hasNext()) {
            final SyndEntry entry = iter.next();
            String img = null;
            if (!entry.getEnclosures().isEmpty()) {
                SyndEnclosure enclosure = (SyndEnclosure) entry.getEnclosures().get(0);
                img = enclosure.getUrl();
            }           
            NewsEntry ne = new NewsEntry(
                    entry.getTitle(),
                    entry.getDescription().getValue(),
                    entry.getLink(),
                    img,
                    entry.getPublishedDate()
            );
            this.newsEntries.add(ne);              
        }     
        return this.newsEntries;
    }        
       
}
