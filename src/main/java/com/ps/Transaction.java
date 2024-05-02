package com.ps;

//This class manages the transactions, including adding transactions and generating reports.


import java.util.Objects;

//Declaring all variables mentioned for txt file
public class Transaction {
    private String date;
    private String time;
    private String description;
    private String vendor;
    private double amount;

    //Create Constructors
    public Transaction(String date, String time, String description, String vendor, double amount) {
        this.date = date;
        this.time = time;
        this.description = description;
        this.vendor = vendor;
        this.amount = amount;

    }

    //Getters for Transaction fields
    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getDescription() {
        return description;
    }

    public String getVendor() {
        return vendor;
    }

    public double getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return date + " | " + time + " | " + description + " | " + vendor + " | $ " + amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return Double.compare(amount, that.amount) == 0 && Objects.equals(date, that.date) && Objects.equals(time, that.time) && Objects.equals(description, that.description) && Objects.equals(vendor, that.vendor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, time, description, vendor, amount);
    }
}


