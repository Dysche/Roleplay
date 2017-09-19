package com.dysche.roleplay.permission.groups;

import org.cantaloupe.permission.group.Group;
import org.cantaloupe.text.Text;

public class Elder extends Group {
    @Override
    public void initialize() {
        
    }
    
    @Override
    public boolean showPrefix() {
        return true;
    }

    @Override
    public String getName() {
        return "elder";
    }

    @Override
    public Text getPrefix() {
        return Text.fromLegacy("&cElder");
    }

    @Override
    public Text getDescription() {
        return null;
    }
}