package com.gnico.commands;

import java.awt.CheckboxMenuItem;
import java.awt.MenuItem;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.function.Consumer;

public class ToggleCommand extends Command implements ItemListener{

    private Consumer<ItemEvent> action;
    
    public ToggleCommand(String name, Consumer<ItemEvent> action) {               
        super(name);
        this.action = action;
    }

   
    @Override
    public void itemStateChanged(ItemEvent e) {
        this.action.accept(e);
        
    }
    
    public MenuItem createMenuItem() {
        CheckboxMenuItem item = new CheckboxMenuItem(this.getName());
        item.addItemListener(this);
        return item;
    }
    
    
}
