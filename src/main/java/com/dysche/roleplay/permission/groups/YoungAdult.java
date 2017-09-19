package com.dysche.roleplay.permission.groups;

import org.cantaloupe.permission.group.Group;
import org.cantaloupe.text.Text;

public class YoungAdult extends Group {
    @Override
    public void initialize() {
        
    }
    
    @Override
    public boolean showPrefix() {
        return true;
    }

    @Override
    public String getName() {
        return "youngadult";
    }

    @Override
    public Text getPrefix() {
        return Text.fromLegacy("&dYoung-Adult");
    }

    @Override
    public Text getDescription() {
        return null;
    }
}