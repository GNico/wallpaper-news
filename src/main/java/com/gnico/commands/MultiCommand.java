package com.gnico.commands;

import java.awt.Menu;
import java.awt.MenuItem;
import java.util.ArrayList;
import java.util.List;

public class MultiCommand extends Command {

    List<Command> commands = new ArrayList<>();
    
    public MultiCommand(String name) {               
        super(name);
    }
    
    @Override
    public MenuItem createMenuItem() {
        Menu subMenu = new Menu(this.name);
        for (Command c : commands) {
            subMenu.add(c.createMenuItem());
        }
        
        this.menuItem = subMenu;
        return subMenu;
    }
    
    public void addCommand(Command command) {
        this.commands.add(command);
    }
}

