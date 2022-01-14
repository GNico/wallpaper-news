package com.gnico.wallpaper;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.gnico.feed.NewsEntry;

public class NewsDrawingTool {
    private static final int MAX_ENTRIES = 10;    
    private static final double LEFT_MARGIN = 0.3;   //Percentage (val 0 to 1) 
    private static final int TOP_MARGIN = 20; //pixels
    private static final int BOTTOM_MARGIN = 60; 
    private static final int ENTRY_PICTURE_WIDTH = 600;
    private static final int ENTRY_PICTURE_HEIGHT = 338;
    private static final double PICTURE_SCALE_FACTOR = 0.25;
    
    private BufferedImage image;
    private String defaultIconPath;  
              

    public NewsDrawingTool(String backgroundPath, String defaultIconPath) {
        this.defaultIconPath = defaultIconPath;
        try (InputStream inputImage = getClass().getResourceAsStream(backgroundPath)){
            image = ImageIO.read(inputImage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void drawNewsEntries(List<NewsEntry> newsEntries) {
        Font font = new Font("Arial", Font.PLAIN, 16);
        Graphics g = image.getGraphics();
        g.setColor(Color.WHITE);
        for (int i = 0; i < MAX_ENTRIES; i++) {              
            g.setFont(font);
            drawNewsEntry(g, 
                    newsEntries.get(i), 
                    getEntryStartX(),  
                    getEntryStartY(i), 
                    getEntryWidth(), 
                    getEntryHeight());
        }
    }
    
    private int getEntryStartX() {
        return (int) (image.getWidth() *  LEFT_MARGIN);
    }
    
    private int getEntryStartY(int i) {
        return TOP_MARGIN + ((image.getHeight() - 60 ) / MAX_ENTRIES) * i ;
    }
             
    private int getEntryWidth() {
        return (int) (image.getWidth() *  0.6);
    }
    
    private int getEntryHeight() {
        return (image.getHeight() - TOP_MARGIN - BOTTOM_MARGIN) / MAX_ENTRIES;
    }
    
    private void drawNewsEntry(Graphics g, NewsEntry newsEntry,  int x, int y, int w, int h) {
        LocalTime time = LocalDateTime.ofInstant(newsEntry.getPublishedDate().toInstant(),
                ZoneId.systemDefault()).toLocalTime();
        
        ImageIcon icon = (newsEntry.getImageUrl() == null || newsEntry.getImageUrl().isEmpty())
                ? createDefaultImageIcon() 
                : createImageIcon(newsEntry.getImageUrl(), "picture");                   
        icon = rescaleImageIcon(icon);
        
        JLabel article = new JLabel("<html><h2><span style='background-color: red'>" 
                + time.truncatedTo(ChronoUnit.MINUTES) + "</span> " + newsEntry.getTitle() +
                "</h2><div>" + newsEntry.getDescription() + "</div></html>", icon, javax.swing.SwingConstants.LEFT);      
        article.setVerticalTextPosition(SwingConstants.TOP);
        article.setBounds(0, 0, w, h);
        article.setForeground(g.getColor());
        article.setFont(g.getFont());
        article.setVerticalAlignment(SwingConstants.NORTH);
        Graphics g2 = g.create(x, y, w, h);                
        article.paint(g2);          
    }
        
    
    private ImageIcon createDefaultImageIcon() {
        ImageIcon icon = null;
        try (InputStream defaultIconImage = getClass().getResourceAsStream(defaultIconPath)){
            BufferedImage buffered = ImageIO.read(defaultIconImage);
            icon = new ImageIcon(buffered, "default pic");
            icon = rescaleImageIcon(icon);
        } catch (IOException e) {
            e.printStackTrace();
        }  
        return icon;
    }
     
    private ImageIcon createImageIcon(String path, String description) {        
        ImageIcon icon = null;
        URL url;
        try {
            url = new URL(path);
            Image imagen = ImageIO.read(url);
            if (imagen != null) {
                icon = new ImageIcon(imagen, description);
            }
        }  catch (IOException e) {
            e.printStackTrace();
        }        
        return icon;
    }
    
    private ImageIcon rescaleImageIcon(ImageIcon icon) {
        if (icon == null) return icon;
        Image iconImg = icon.getImage();
        int newWidth = (int) (ENTRY_PICTURE_WIDTH * PICTURE_SCALE_FACTOR);
        int newHeight = (int) (ENTRY_PICTURE_HEIGHT * PICTURE_SCALE_FACTOR);
        Image resizedImgIcon = iconImg.getScaledInstance(newWidth, newHeight,  java.awt.Image.SCALE_SMOOTH); 
        return new ImageIcon(resizedImgIcon);
    }      
    
    public void displayImage() {
        JLabel picLabel = new JLabel(new ImageIcon(image));
        JPanel jPanel = new JPanel();
        jPanel.add(picLabel);
        JFrame f = new JFrame();
        f.setSize(new Dimension(image.getWidth(), image.getHeight()));
        f.add(jPanel);
        f.setVisible(true); 
    }
    
    public void saveImage(String path) {
        File filePath = new File(path);
        try {
            ImageIO.write(image, "bmp", filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }  
    }
}
