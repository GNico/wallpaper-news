package com.gnico.main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    private static final String RSS_FEED_URL = "https://www.clarin.com/rss/lo-ultimo/";
    private static final String APP_NAME = "Wallpaper News";   
    
    public static void main(String[] args) {     
        logger.info("App started");        
        Application app = new Application(APP_NAME, RSS_FEED_URL);
        app.init();
        app.run();           
    }                 
 }
