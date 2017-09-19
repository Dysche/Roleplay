package com.dysche.roleplay.permission.groups;

import org.cantaloupe.permission.group.Group;
import org.cantaloupe.text.Text;

public class Child extends Group {
    @Override
    public void initialize() {
        
    }
    
    @Override
    public boolean showPrefix() {
        return true;
    }

    @Override
    public String getName() {
        return "child";
    }

    @Override
    public Text getPrefix() {
        return Text.fromLegacy("&bChild");
    }

    @Override
    public Text getDescription() {
        return null;
    }
}