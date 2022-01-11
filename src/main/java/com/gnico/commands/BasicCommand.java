package com.gnico.commands;

import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.function.Consumer;

public class BasicCommand extends Command implements ActionListener {

    private Consumer<ActionEvent> action;
    
    public BasicCommand(String name, Consumer<ActionEvent> action) {               
        super(name);
        this.action = action;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.action.accept(e);        
    }
    
    public MenuItem createMenuItem() {
        MenuItem item = new MenuItem(this.name);
        item.addActionListener(this);
        return item;
    }

       
}
