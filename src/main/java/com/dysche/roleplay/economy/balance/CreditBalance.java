package com.dysche.roleplay.economy.balance;

public class CreditBalance extends Balance {
    private CreditBalance(double startAmount, double amount) {
        super(startAmount, amount);
    }

    public static CreditBalance of(double startAmount, double amount) {
        return new CreditBalance(startAmount, amount);
    }

    public static CreditBalance of(double startAmount) {
        return new CreditBalance(startAmount, startAmount);
    }

    public static CreditBalance of() {
        return new CreditBalance(0.0D, 0.0D);
    }

    @Override
    public boolean withdraw(double amount) {
        if (this.amount - amount > 0) {
            this.amount -= amount;

            return true;
        }

        return false;
    }
}