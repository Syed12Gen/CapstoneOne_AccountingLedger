package com.ps;
import java.time.LocalDateTime;
import java.util.*;

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
        String vendor = scanner.nextLine();
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
        scanner.nextLine(); // clear the new line character from the scanner

        System.out.print("Enter description: ");
        String description = scanner.nextLine();

        LocalDateTime now = LocalDateTime.now();
        String formattedTimeString = now.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        String formattedDateString = now.format(DateTimeFormatter.ofPattern(Constants.DATE_FORMAT));
        Transaction transaction = new Transaction
                (formattedDateString, formattedTimeString, description, vendor, amount);
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

            scanner.nextLine();
            switch (option) {
                case "1":
                    System.out.println("Month to Date Report: \n");
                    generateReport("Month To Date");
                    break;
                case "2":
                    System.out.println("Previous Month Report: \n");
                    generateReport("Previous Month");
                    break;
                case "3":
                    System.out.println("Year to Date Report: \n");
                    generateReport("Year To Date");
                    break;
                case "4":
                    System.out.println("Previous Year Report: \n");
                    generateReport("Previous Year");
                    break;
                case "5":
                    System.out.print("Enter vendor name for report: ");
                    String vendor = scanner.nextLine();
                    System.out.println("Report for Vendor '" + vendor + "': \n");
                    generateReportForVendor(vendor);
                    break;
                case "0":
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        } while (!option.equals("0"));
    }

    private void generateReport(String reportType) {
        List<Transaction> transactions = new ArrayList<>();

        transactions = switch (reportType) {
            case "Month To Date" -> transactionManager.getMonthToDateTransactions();
            case "Previous Month" -> transactionManager.getPreviousMonthTransactions();
            case "Year To Date" -> transactionManager.getYearToDateTransactions();
            case "Previous Year" -> transactionManager.getPreviousYearTransactions();
            default -> transactions;
        };


        if(transactions.isEmpty()) {
            System.out.println("No transactions found for selected report type.");
            return;
        }

        OutputFormatter.printOutput(transactions);
    }

    private void generateReportForVendor(String vendor) {
        List<Transaction> filteredTransactions = transactionManager.filterByVendor(vendor, transactionManager.getTransactions());

        if (filteredTransactions.isEmpty()) {
            System.out.println("No transactions found for vendor: " + vendor);
        }

        OutputFormatter.printOutput(filteredTransactions);
    }


}
