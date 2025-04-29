package com.jpmc.midascore;

public class IncentiveResponse {
    private double amount;

    public IncentiveResponse() {}

    public IncentiveResponse(double amount) {
        this.amount = amount;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
