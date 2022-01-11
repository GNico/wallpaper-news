package com.gnico.feed;

import java.util.Date;

public class NewsEntry {

    private String title;
    private String description;
    private String link;
    private String imageUrl;
    private Date publishedDate;
    
    public NewsEntry() {      
    }
    
    public NewsEntry(String title, String description, String link, String imageUrl, Date publishedDate) {
        super();
        this.title = title;
        this.description = description;
        this.link = link;
        this.imageUrl = imageUrl;
        this.publishedDate = publishedDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Date getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(Date publishedDate) {
        this.publishedDate = publishedDate;
    }
    
    
    
}
