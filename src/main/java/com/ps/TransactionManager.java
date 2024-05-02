package com.ps;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionManager {
    //
    private ArrayList<Transaction> transactions;
    private String filepath;

    public TransactionManager(String filepath) {
        this.transactions = new ArrayList<>();
        this.filepath = filepath;
    }

    public void loadTransactions() {
        try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {
            String line;
            boolean isFirstLine = true;  // Add a flag to track the first line (header)
            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;  // Skip the first line as it contains headers
                    continue;
                }
                String[] data = line.split("\\|");
                if (data.length == 5) {
                    String date = data[0];
                    String time = data[1];
                    String description = data[2];
                    String vendor = data[3];
                    double amount = Double.parseDouble(data[4]);
                    transactions.add(new Transaction(date, time, description, vendor, amount));
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
        }
    }

    public void saveTransactions() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filepath))) {
            for (Transaction transaction : transactions) {
                // Create a formatted string for each transaction
                String transactionText = String.format("%s|%s|%s|%s|%.2f",
                        transaction.getDate(), transaction.getTime(), transaction.getDescription(),
                        transaction.getVendor(), transaction.getAmount());
                pw.println(transactionText);
            }
        } catch (IOException e) {
            System.err.println("Error writing to the file: " + e.getMessage());
        }
    }

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
        saveTransactions(); // Save each time a new transaction is added
    }

    public ArrayList<Transaction> getTransactions() {
        return transactions;
    }

    // Method to get transactions for a specific vendor
    public List<Transaction> getTransactionsForVendor(String vendor) {
        List<Transaction> filteredTransactions = new ArrayList<>();
        for (Transaction transaction : transactions) {
            if (transaction.getVendor().equalsIgnoreCase(vendor)) {
                filteredTransactions.add(transaction);
            }
        }
        return filteredTransactions;
    }
}
