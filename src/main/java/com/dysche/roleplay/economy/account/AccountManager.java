package com.dysche.roleplay.economy.account;

import java.util.Optional;
import java.util.UUID;

import org.cantaloupe.Cantaloupe;
import org.cantaloupe.data.DataContainer;
import org.cantaloupe.database.MongoDB;
import org.cantaloupe.database.mongodb.Collection;
import org.cantaloupe.database.mongodb.Database;
import org.cantaloupe.player.Player;
import org.cantaloupe.service.services.MongoService;

public class AccountManager {
    private final DataContainer<UUID, Account> accounts;

    public AccountManager() {
        this.accounts = DataContainer.of();
    }

    public Account createAccount(Player player) {
        return this.createAccount(player.getUUID());
    }

    public Account createAccount(UUID holderUUID) {
        Account account = Account.of(holderUUID);
        this.accounts.put(holderUUID, account);

        this.saveAccount(holderUUID);

        return account;
    }

    public Account loadAccount(Player player) {
        return this.loadAccount(player.getUUID());
    }

    public Account loadAccount(UUID holderUUID) {
        MongoService service = Cantaloupe.getServiceManager().provide(MongoService.class);
        MongoDB connection = service.getConnection("main").get();
        Database database = connection.getDatabase("roleplay").get();
        Optional<Collection> collectionOpt = database.getCollection("accounts");

        if (collectionOpt.isPresent()) {
            for (DataContainer<String, Object> data : collectionOpt.get().retrieve(holderUUID.toString())) {
                this.accounts.put(holderUUID, Account.of(holderUUID, data.getGeneric("credit"), data.getGeneric("startAmount"), data.getGeneric("amount")));

                break;
            }
        }

        if (!this.hasAccount(holderUUID)) {
            return this.createAccount(holderUUID);
        } else {
            return this.getAccount(holderUUID);
        }
    }

    public void loadAccounts() {
        MongoService service = Cantaloupe.getServiceManager().provide(MongoService.class);
        MongoDB connection = service.getConnection("main").get();
        Database database = connection.getDatabase("roleplay").get();
        Optional<Collection> collectionOpt = database.getCollection("accounts");

        if (collectionOpt.isPresent()) {
            for (Player player : Cantaloupe.getPlayerManager().getPlayers()) {
                for (DataContainer<String, Object> data : collectionOpt.get().retrieve(player.getUUID().toString())) {
                    this.accounts.put(player.getUUID(), Account.of(player.getUUID(), data.getGeneric("credit"), data.getGeneric("startAmount"), data.getGeneric("amount")));

                    break;
                }
            }
        }
    }

    public void saveAccounts() {
        MongoService service = Cantaloupe.getServiceManager().provide(MongoService.class);
        MongoDB connection = service.getConnection("main").get();
        Database database = connection.getDatabase("roleplay").get();
        Optional<Collection> collectionOpt = database.getCollection("accounts");
        Collection collection = null;

        if (!collectionOpt.isPresent()) {
            collection = database.createCollection("accounts");
        } else {
            collection = collectionOpt.get();
        }

        for (Account account : this.accounts.valueSet()) {
            DataContainer<String, Object> container = DataContainer.of();
            container.put("holderUUID", account.getHolderUUID().toString());
            container.put("credit", account.allowNegativeBalance());
            container.put("startAmount", account.getBalance().getStartAmount());
            container.put("amount", account.getBalance().getAmount());

            collection.upsert(account.getHolderUUID().toString(), container);
        }
    }

    public void saveAccount(Player player) {
        this.saveAccount(player.getUUID());
    }

    public void saveAccount(UUID holderUUID) {
        if (this.accounts.containsKey(holderUUID)) {
            MongoService service = Cantaloupe.getServiceManager().provide(MongoService.class);
            MongoDB connection = service.getConnection("main").get();
            Database database = connection.getDatabase("roleplay").get();
            Optional<Collection> collectionOpt = database.getCollection("accounts");

            if (collectionOpt.isPresent()) {
                Account account = this.accounts.get(holderUUID);
                DataContainer<String, Object> container = DataContainer.of();
                container.put("holderUUID", account.getHolderUUID().toString());
                container.put("credit", account.allowNegativeBalance());
                container.put("startAmount", account.getBalance().getStartAmount());
                container.put("amount", account.getBalance().getAmount());

                collectionOpt.get().upsert(account.getHolderUUID().toString(), container);
            }
        }
    }

    public void deleteAccount(Player player) {
        this.deleteAccount(player.getUUID());
    }

    public void deleteAccount(UUID holderUUID) {
        MongoService service = Cantaloupe.getServiceManager().provide(MongoService.class);
        MongoDB connection = service.getConnection("main").get();
        Database database = connection.getDatabase("roleplay").get();
        Optional<Collection> collectionOpt = database.getCollection("accounts");

        if (collectionOpt.isPresent()) {
            collectionOpt.get().delete(holderUUID.toString());
        }

        this.accounts.remove(holderUUID);
    }

    public void unloadAccount(Player player) {
        this.unloadAccount(player.getUUID());
    }

    public void unloadAccount(UUID holderUUID) {
        if (this.accounts.containsKey(holderUUID)) {
            this.saveAccount(holderUUID);

            this.accounts.remove(holderUUID);
        }
    }

    public boolean hasAccount(Player player) {
        return this.hasAccount(player.getUUID());
    }

    public boolean hasAccount(UUID holderUUID) {
        return this.accounts.containsKey(holderUUID);
    }

    public boolean hasAccountStored(Player player) {
        return this.hasAccountStored(player.getUUID());
    }

    public boolean hasAccountStored(UUID holderUUID) {
        MongoService service = Cantaloupe.getServiceManager().provide(MongoService.class);
        MongoDB connection = service.getConnection("main").get();
        Database database = connection.getDatabase("roleplay").get();
        Optional<Collection> collectionOpt = database.getCollection("accounts");

        if (collectionOpt.isPresent()) {
            return collectionOpt.get().count(holderUUID.toString()) > 0;
        }

        return false;
    }

    public Account getAccount(Player player) {
        return this.getAccount(player.getUUID());
    }

    public Account getAccount(UUID holderUUID) {
        if (!this.hasAccount(holderUUID)) {
            if (!this.hasAccountStored(holderUUID)) {
                this.createAccount(holderUUID);
            } else {
                this.loadAccount(holderUUID);
            }
        }

        return this.accounts.get(holderUUID);
    }
}