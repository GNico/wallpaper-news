package com.gnico.utils;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gnico.commands.Command;

public class TrayIconManager {
    private static final Logger logger = LoggerFactory.getLogger(TrayIconManager.class);

    private String programName;
    private String imgPath;
    private TrayIcon trayIcon;
    private final PopupMenu popup = new PopupMenu();
    
    public TrayIconManager() {
    }
    
    public TrayIconManager(String programName, String imgPath) {       
        this.programName = programName;
        this.imgPath = imgPath;
    }
    
    public void createTrayIcon() {
        if (!SystemTray.isSupported()) {
            logger.error("System tray not supported");
            return;
        }               
        SystemTray tray = SystemTray.getSystemTray();
        try (InputStream inputImage = getClass().getResourceAsStream(imgPath) ) {            
            Image image = ImageIO.read(inputImage);
            trayIcon = new TrayIcon(image, programName, popup);
            trayIcon.setImageAutoSize(true);                  
            trayIcon.setPopupMenu(popup);
            tray.add(trayIcon);           
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (AWTException e) {
            logger.error("TrayIcon could not be added");
        }         
    }
           
    public void addPopupItem(Command command) {
        popup.add(command.createMenuItem());
    }
    
    public void addPopupItem(Command command, int index) {
        popup.insert(command.createMenuItem(), index);
    }
    
    public void addPopupItems(List<Command> commands) {
        for (Command command : commands) {
            MenuItem item = command.createMenuItem();
            popup.add(item);
        } 
    }
    
    public void removePopupItem(MenuItem menuItem) {
        popup.remove(menuItem);
    }
    
    public void removeTrayIcon() {
        SystemTray tray = SystemTray.getSystemTray();
        tray.remove(trayIcon);
    }       
}
