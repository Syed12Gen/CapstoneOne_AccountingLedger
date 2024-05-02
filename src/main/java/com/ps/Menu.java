package com.ps;
import java.util.List;
import java.util.Scanner;
import java.util.Locale;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Locale;
import java.util.stream.Collectors;


public class Menu {
    private TransactionManager transactionManager;
    private Scanner scanner;

    public Menu(TransactionManager transactionManager) {
        this.transactionManager = transactionManager;
        this.scanner = new Scanner(System.in);
        this.scanner.useLocale(Locale.US);  // Ensures that dot is used as decimal separator
    }

    public void displayMainMenu() {
        String option;
        do {
            System.out.println("\n**Home Menu**");
            System.out.println("P) Make Payment");
            System.out.println("D) Deposits");
            System.out.println("R) Reports");
            System.out.println("X) Exit");
            System.out.print("Enter option: ");
            option = scanner.next();

            switch (option.toUpperCase()) {
                case "P":
                    handleTransaction(false);
                    break;
                case "D":
                    handleTransaction(true);
                    break;
                case "R":
                    displayReportsMenu();
                    break;
                case "X":
                    System.out.println("Exiting program.");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        } while (!option.equalsIgnoreCase("X"));
    }

    private void handleTransaction(boolean isDeposit) {
        System.out.print("Enter vendor name: ");
        String vendor = scanner.next();
        System.out.print("Enter amount: ");
        if (!scanner.hasNextDouble()) {
            System.out.println("Invalid input. Please enter a numeric value.");
            scanner.next();  // Clear the wrong input
            return;
        }
        double amount = scanner.nextDouble();
        if (!isDeposit) {
            amount = -amount; // Negative for payments
        }
        Transaction transaction = new Transaction("2023-04-15", "12:00:00", "Transaction", vendor, amount);
        transactionManager.addTransaction(transaction);
        System.out.println("Transaction recorded.");
    }

    private void displayReportsMenu() {
        String option;
        do {
            System.out.println("\n**Reports Menu**");
            System.out.println("1) Month To Date");
            System.out.println("2) Previous Month");
            System.out.println("3) Year To Date");
            System.out.println("4) Previous Year");
            System.out.println("5) Search by Vendor");
            System.out.println("0) Back");

            System.out.print("Enter option: ");
            option = scanner.next();

            switch (option) {
                case "1":
                    System.out.println("Month to Date Report: " + generateReport("Month To Date"));
                    break;
                case "2":
                    System.out.println("Previous Month Report: " + generateReport("Previous Month"));
                    break;
                case "3":
                    System.out.println("Year to Date Report: " + generateReport("Year To Date"));
                    break;
                case "4":
                    System.out.println("Previous Year Report: " + generateReport("Previous Year"));
                    break;
                case "5":
                    System.out.print("Enter vendor name for report: ");
                    String vendor = scanner.next();
                    System.out.println("Report for Vendor '" + vendor + "': " + generateReportForVendor(vendor));
                    break;
                case "0":
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        } while (!option.equals("0"));
    }

    // Placeholder methods for report generation
    private String generateReport(String reportType) {
        // Placeholder for report generation logic based on reportType
        return reportType + " data";
    }

    private String generateReportForVendor(String vendor) {
        StringBuilder reportBuilder = new StringBuilder();
        List<Transaction> filteredTransactions = transactionManager.getTransactionsForVendor(vendor);

        if (filteredTransactions.isEmpty()) {
            return "No transactions found for vendor: " + vendor;
        }

        reportBuilder.append("Transactions for ").append(vendor).append(":\n");
        for (Transaction transaction : filteredTransactions) {
            reportBuilder.append(transaction.getDate())
                    .append(" | ").append(transaction.getDescription())
                    .append(" | $").append(transaction.getAmount())
                    .append("\n");
        }
        return reportBuilder.toString();
    }

}
