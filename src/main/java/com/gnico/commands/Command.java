package com.gnico.commands;

import java.awt.MenuItem;

public abstract class Command {

    protected String name;
    protected MenuItem menuItem; 

    public abstract MenuItem createMenuItem();
    
    protected Command(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public MenuItem getMenuItem() {
        return this.menuItem;
    }
}
