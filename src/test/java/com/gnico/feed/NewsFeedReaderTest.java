package com.gnico.feed;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

class NewsFeedReaderTest {

    @Test
    void testParsingEmptyList() throws MalformedURLException {
        NewsFeedReader fr = new NewsFeedReader(new URL("http://test"));      
        List<SyndEntry> listToParse = new ArrayList<>();
        assertTrue(fr.parse(listToParse).isEmpty());
    }
            
    @Test
    void testFetchFromFile() throws CouldNotFetchException {
        URL url = getClass().getClassLoader().getResource("examplefeed.xml");
        NewsFeedReader fr = new NewsFeedReader(url);

        SyndFeed feed = fr.fetch();        
        assertEquals("Example feed title", feed.getTitle());
        assertEquals("https://feedlink", feed.getLink());
        assertEquals("Example feed description", feed.getDescription());
    }
    
    @Test
    void testFetchWrongFormat()  {
        URL url = getClass().getClassLoader().getResource("wrongformat.xml");
        NewsFeedReader fr = new NewsFeedReader(url);
        assertThrows(CouldNotFetchException.class, () -> fr.fetch());        
    }
    
    @Test
    void testFetchNonExistentSource() throws MalformedURLException  {
        NewsFeedReader fr = new NewsFeedReader(new URL("https://nonexistent"), 1000);
        assertThrows(CouldNotFetchException.class, () -> fr.fetch());        
    }
    
    @SuppressWarnings({ "unchecked", "deprecation" })
    @Test
    void testParseFeed() throws IllegalArgumentException, FeedException, IOException {        
        SyndFeed feed = readFeedFromFile("examplefeed.xml"); 
        NewsFeedReader fr = new NewsFeedReader(new URL("http://test"));
        List<NewsEntry> results = fr.parse(feed.getEntries());        
        
        assertEquals(10, results.size());
        assertEquals("First title", results.get(0).getTitle());
        assertEquals("First description", results.get(0).getDescription());
        assertEquals("https://firstlink.html", results.get(0).getLink());
        assertEquals("https://firstimage.jpg", results.get(0).getImageUrl());
        assertEquals(new Date("Fri, 14 Jan 2022 18:00:00 -0300"), results.get(0).getPublishedDate());
    }
    
    @SuppressWarnings("unchecked")
    @Test
    void testParseItemWithNoImage() throws IllegalArgumentException, FeedException, IOException {
        SyndFeed feed = readFeedFromFile("examplefeed.xml"); 
        NewsFeedReader fr = new NewsFeedReader(new URL("http://test"));
        List<NewsEntry> results = fr.parse(feed.getEntries());   
        
        //second result has no image
        assertEquals("Second title", results.get(1).getTitle());
        assertEquals("", results.get(1).getImageUrl());
    }
    
         
  //manual fetching to isolate other methods
    private SyndFeed readFeedFromFile(String file) throws IllegalArgumentException, FeedException, IOException {
        URL url = getClass().getClassLoader().getResource(file);
        SyndFeedInput input = new SyndFeedInput();
        return input.build(new XmlReader(url));
    }
   
}
