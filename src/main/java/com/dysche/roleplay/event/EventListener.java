package com.dysche.roleplay.event;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.cantaloupe.events.MongoConnectEvent;

import com.dysche.roleplay.Main;

public class EventListener implements Listener {
    @EventHandler
    public void onMongoConnect(MongoConnectEvent event) {
        Main.getInstance().getEconomy().getAccountManager().loadAccounts();
    }
}