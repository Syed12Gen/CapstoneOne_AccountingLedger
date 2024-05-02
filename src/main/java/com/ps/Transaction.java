package com.ps;

//This class manages the transactions, including adding transactions and generating reports.


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

}

