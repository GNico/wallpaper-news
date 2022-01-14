package com.gnico.main;

import java.awt.Desktop;
import java.awt.event.ItemEvent;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
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
import com.gnico.feed.NewsFeedReader;
import com.gnico.feed.NewsEntry;
import com.gnico.utils.TrayIconManager;
import com.gnico.wallpaper.WallpaperChanger;

public class Application implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    private final TrayIconManager trayIconManager = new TrayIconManager(this.appName, "/resources/appicon.png");      
    //private final TrayIconManager trayIconManager = new TrayIconManager(this.appName, "/appicon.png");        
    private ScheduledExecutorService scheduler;    
    private MultiCommand dynamicLinks = null;
    private String rssFeedUrl;
    private String appName;

    
    public Application(String appName, String rssFeedUrl) {
        this.appName = appName; 
        this.rssFeedUrl = rssFeedUrl;
    }
    
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
            newsEntries = this.updateHeadlines(this.rssFeedUrl);
            if (!newsEntries.isEmpty()) {
                WallpaperChanger wpc = new WallpaperChanger();                
                wpc.updateWallpaper(newsEntries);
                createLinkCommands(newsEntries);
            }
        } catch (CouldNotFetchException e) {
            logger.error(e.getMessage());
            clear();
        } catch (MalformedURLException e) {
            logger.error("Wrong URL format");
            clear();
        }       
    }
    
    public List<NewsEntry> updateHeadlines(String feedPath) throws CouldNotFetchException, MalformedURLException {
        NewsFeedReader fr = new NewsFeedReader(new URL(feedPath), 5000);      
        return fr.updateFeed();     
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
    
}

