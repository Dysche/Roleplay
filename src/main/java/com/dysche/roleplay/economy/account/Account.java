package com.dysche.roleplay.economy.account;

import java.util.Optional;
import java.util.UUID;

import org.cantaloupe.Cantaloupe;
import org.cantaloupe.data.DataContainer;
import org.cantaloupe.database.MongoDB;
import org.cantaloupe.database.mongodb.Collection;
import org.cantaloupe.database.mongodb.Database;
import org.cantaloupe.service.services.MongoService;

import com.dysche.roleplay.economy.balance.Balance;
import com.dysche.roleplay.economy.balance.CreditBalance;

public class Account {
    private final UUID holderUUID;
    private Balance    balance = null;

    protected Account(UUID holderUUID, Balance balance) {
        this.holderUUID = holderUUID;
        this.balance = balance;
    }

    public static Account of(UUID holderUUID, boolean credit, double startAmount, double amount) {
        return new Account(holderUUID, credit ? CreditBalance.of(startAmount, amount) : Balance.of(startAmount, amount));
    }

    public static Account of(UUID holderUUID, boolean credit, double amount) {
        return new Account(holderUUID, credit ? CreditBalance.of(amount) : Balance.of(amount));
    }

    public static Account of(UUID holderUUID, boolean credit) {
        return new Account(holderUUID, credit ? CreditBalance.of() : Balance.of());
    }

    public static Account of(UUID holderUUID, double startAmount, double amount) {
        return new Account(holderUUID, Balance.of(startAmount, amount));
    }

    public static Account of(UUID holderUUID, double amount) {
        return new Account(holderUUID, Balance.of(amount));
    }

    public static Account of(UUID holderUUID) {
        return new Account(holderUUID, Balance.of());
    }

    public void deposit(double amount) {
        this.balance.deposit(amount);

        this.update();
    }

    public boolean withdraw(double amount) {
        boolean successful = this.balance.withdraw(amount);

        this.update();

        return successful;
    }

    public void update() {
        MongoService service = Cantaloupe.getServiceManager().provide(MongoService.class);
        MongoDB connection = service.getConnection("main").get();
        Database database = connection.getDatabase("roleplay").get();
        Optional<Collection> collectionOpt = database.getCollection("accounts");

        if (collectionOpt.isPresent()) {
            DataContainer<String, Object> container = DataContainer.of();
            container.put("holderUUID", this.getHolderUUID().toString());
            container.put("credit", this.allowNegativeBalance());
            container.put("startAmount", this.getBalance().getStartAmount());
            container.put("amount", this.getBalance().getAmount());

            collectionOpt.get().upsert(this.getHolderUUID().toString(), container);
        }
    }

    public boolean allowNegativeBalance() {
        return this.balance instanceof CreditBalance;
    }

    public UUID getHolderUUID() {
        return this.holderUUID;
    }

    public Balance getBalance() {
        return this.balance;
    }
}