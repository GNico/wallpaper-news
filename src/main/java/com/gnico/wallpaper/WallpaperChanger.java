package com.gnico.wallpaper;

import java.util.List;

import com.gnico.feed.NewsEntry;
import com.gnico.utils.WindowsUtils;

public class WallpaperChanger {
        
    //private NewsDrawingTool newsDrawingTool = new NewsDrawingTool("/resources/stars.bmp");    
    private NewsDrawingTool newsDrawingTool = new NewsDrawingTool("/stars.bmp");    
    private static final String GENERATED_WALLPAPER_FILENAME = "generatedWallpaper.bmp";
        
    
    public void updateWallpaper(List<NewsEntry> newsEntries) {       
        prepareImage(newsEntries);
        saveImage();
        WindowsUtils.setImageAsWallpaper(GENERATED_WALLPAPER_FILENAME);
    }
    
    public void clearWallpaper() {
        saveImage();
        WindowsUtils.setImageAsWallpaper(GENERATED_WALLPAPER_FILENAME);
    }
      
    public void prepareImage(List<NewsEntry> newsEntries) {
        newsDrawingTool.drawNewsEntries(newsEntries);
    }
    
    public void saveImage() {        
        newsDrawingTool.saveImage(GENERATED_WALLPAPER_FILENAME);
    }
    
    public void displayImage() {
        newsDrawingTool.displayImage();
    }
}
