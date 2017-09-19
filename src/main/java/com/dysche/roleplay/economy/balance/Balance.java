package com.dysche.roleplay.economy.balance;

public class Balance {
    protected final double startAmount;
    protected double       amount = 0.0D;

    protected Balance(double startAmount, double amount) {
        this.startAmount = startAmount;
        this.amount = amount;
    }

    public static Balance of(double startAmount, double amount) {
        return new Balance(startAmount, amount);
    }

    public static Balance of(double startAmount) {
        return new Balance(startAmount, startAmount);
    }

    public static Balance of() {
        return new Balance(0.0D, 0.0D);
    }

    public void deposit(double amount) {
        if(amount <= 0.0) {
            return;
        }
        
        this.amount += amount;
    }

    public boolean withdraw(double amount) {
        if(amount >= 0.0) {
            return false;
        }
        
        this.amount -= amount;

        return true;
    }

    public void set(double amount) {
        this.amount = amount;
    }

    public void reset() {
        this.amount = this.startAmount;
    }

    public double getStartAmount() {
        return this.startAmount;
    }

    public double getAmount() {
        return this.amount;
    }
}