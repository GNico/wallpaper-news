package com.gnico.main;

import java.awt.Desktop;
import java.awt.event.ItemEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gnico.commands.BasicCommand;
import com.gnico.commands.Command;
import com.gnico.commands.MultiCommand;
import com.gnico.commands.ToggleCommand;
import com.gnico.feed.CouldNotFetchException;
import com.gnico.feed.FeedReader;
import com.gnico.feed.NewsEntry;
import com.gnico.utils.TrayIconManager;
import com.gnico.wallpaper.WallpaperChanger;

public class Application implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    private static final String RSS_FEED_URL = "https://www.clarin.com/rss/lo-ultimo/";
    private static final String APP_ICON_NAME = "Headlines Wallpaper";    
  //  private final TrayIconManager trayIconManager = new TrayIconManager(APP_ICON_NAME, "/resources/appicon.png");        
    private final TrayIconManager trayIconManager = new TrayIconManager(APP_ICON_NAME, "/appicon.png");        
    private ScheduledExecutorService scheduler;    
    private MultiCommand dynamicLinks = null;

    
    public void init() {
        List<Command> commands = new ArrayList<>();
        commands.add(new BasicCommand("Update now", e -> this.run() ));
        commands.add(new BasicCommand("Clear", e -> this.clear() ));
        commands.add(new ToggleCommand("Automatic updates",  e -> {
            if (ItemEvent.SELECTED == e.getStateChange()) 
                startScheduler();
            else 
                stopScheduler(); 
         }));       
        commands.add(new BasicCommand("Exit", e -> System.exit(1) ));        
        trayIconManager.createTrayIcon();
        trayIconManager.addPopupItems(commands);
        
    }
        
    public void run() {
        logger.info("Updating news...");
        List<NewsEntry> newsEntries;
        try {
            newsEntries = this.readFeed();
            if (!newsEntries.isEmpty()) {
                WallpaperChanger wpc = new WallpaperChanger();                
                wpc.updateWallpaper(newsEntries);
                createLinkCommands(newsEntries);
            }
        } catch (CouldNotFetchException e) {
            System.out.println("read exception");
            clear();
        }       
    }
        
    public void createLinkCommands(List<NewsEntry> newsEntries) {
        removeLinkCommands();               
        Desktop desktop = Desktop.getDesktop();
        dynamicLinks = new MultiCommand("Links");
        for (NewsEntry entry : newsEntries) {            
            dynamicLinks.addCommand(new BasicCommand(entry.getTitle(), e -> {
                try {
                    desktop.browse(new URI(entry.getLink()));
                } catch (IOException | URISyntaxException e1) {
                    e1.printStackTrace();
                }
            } ));            
        }   
        trayIconManager.addPopupItem(dynamicLinks, 0);        
    }
    
    public void removeLinkCommands() {
        if (dynamicLinks != null) {
            trayIconManager.removePopupItem(dynamicLinks.getMenuItem());
        } 
    }
        
    public void clear() {
        WallpaperChanger wpc = new WallpaperChanger();
        wpc.clearWallpaper();
        removeLinkCommands();
    }
    
    public void startScheduler() {
        scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(this, 0, 15, TimeUnit.MINUTES);
    }
    
    public void stopScheduler() {
        scheduler.shutdown();
    }
    
    public List<NewsEntry> readFeed() throws CouldNotFetchException {
        FeedReader fr = new FeedReader(RSS_FEED_URL);      
        return fr.fetch();     
    }    
}

