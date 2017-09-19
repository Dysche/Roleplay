package com.dysche.roleplay.permission.groups;

import org.cantaloupe.permission.group.Group;
import org.cantaloupe.text.Text;

public class Teen extends Group {
    @Override
    public void initialize() {
        
    }
    
    @Override
    public boolean showPrefix() {
        return true;
    }

    @Override
    public String getName() {
        return "teen";
    }

    @Override
    public Text getPrefix() {
        return Text.fromLegacy("&9Teen");
    }

    @Override
    public Text getDescription() {
        return null;
    }
}