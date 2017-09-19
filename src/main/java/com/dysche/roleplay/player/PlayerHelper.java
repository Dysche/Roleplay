package com.dysche.roleplay.player;

import java.util.Optional;

import org.cantaloupe.Cantaloupe;
import org.cantaloupe.data.DataContainer;
import org.cantaloupe.database.MongoDB;
import org.cantaloupe.database.mongodb.Collection;
import org.cantaloupe.database.mongodb.Database;
import org.cantaloupe.permission.group.GroupManager;
import org.cantaloupe.service.services.MongoService;

public class PlayerHelper {
    public static void registerPlayer(RoleplayWrapper wrapper) {
        wrapper.setAgeGroup(GroupManager.getGroup("youngadult").get());

        savePlayer(wrapper);
    }

    public static void savePlayer(RoleplayWrapper wrapper) {
        MongoService service = Cantaloupe.getServiceManager().provide(MongoService.class);
        MongoDB connection = service.getConnection("main").get();
        Database database = connection.getDatabase("roleplay").get();
        Optional<Collection> collectionOpt = database.getCollection("players");

        if (collectionOpt.isPresent()) {
            DataContainer<String, Object> container = DataContainer.of();
            container.put("ageGroup", wrapper.getAgeGroup().getName());

            collectionOpt.get().upsert(wrapper.getPlayer().getUUID().toString(), container);
        }
    }

    public static void loadPlayer(RoleplayWrapper wrapper) {
        MongoService service = Cantaloupe.getServiceManager().provide(MongoService.class);
        MongoDB connection = service.getConnection("main").get();
        Database database = connection.getDatabase("roleplay").get();
        Optional<Collection> collectionOpt = database.getCollection("players");

        if (collectionOpt.isPresent()) {
            for (DataContainer<String, Object> data : collectionOpt.get().retrieve(wrapper.getPlayer().getUUID().toString())) {
                wrapper.setAgeGroup(GroupManager.getGroup(data.getGeneric("ageGroup")).get());

                break;
            }
        }
    }

    public static boolean isRegistered(RoleplayWrapper wrapper) {
        MongoService service = Cantaloupe.getServiceManager().provide(MongoService.class);
        MongoDB connection = service.getConnection("main").get();
        Database database = connection.getDatabase("roleplay").get();
        Optional<Collection> collectionOpt = database.getCollection("players");

        if (collectionOpt.isPresent()) {
            return collectionOpt.get().count(wrapper.getPlayer().getUUID().toString()) > 0;
        }

        return false;
    }
}