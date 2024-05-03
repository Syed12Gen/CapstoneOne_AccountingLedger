package com.ps;

public class AmountRange {
    private double minAmount;
    private double maxAmount;

    public AmountRange(double minAmount, double maxAmount) {
        this.minAmount = minAmount;
        this.maxAmount = maxAmount;
    }

    public double getMinAmount() {
        return minAmount;
    }

    public double getMaxAmount() {
        return maxAmount;
    }

    public boolean includes(double amount) {
        return amount >= minAmount && amount <= maxAmount;
    }
}

