package com.dysche.roleplay.economy;

import com.dysche.roleplay.economy.account.AccountManager;

public class Economy {
    private final AccountManager accountManager;
    
    public Economy() {
        this.accountManager = new AccountManager();
    }
    
    public AccountManager getAccountManager() {
        return this.accountManager;
    }
}