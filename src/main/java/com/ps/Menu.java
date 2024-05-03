package com.ps;

import java.time.LocalDateTime;
import java.util.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

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
            System.out.println("S) Search");
            System.out.println("X) Exit");
            System.out.print("Enter option: ");
            option = scanner.nextLine();

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
                case "S":
                    searchTransactions();
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


        if (transactions.isEmpty()) {
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


    private void searchTransactions() {
        Map<TransactionSearchFilter, Object> filters = new HashMap<>();
        while (true) {
            System.out.println("\n**Search Menu**");
            System.out.println("Please select a filter to add. " +
                    "When you are done adding filters, press the X key to search using the selected filters: ");
            System.out.println("1. Description");
            System.out.println("2. Vendor");
            System.out.println("3. Amount range");
            System.out.println("4. Date range");
            System.out.println("0. Go back");

            String filter = scanner.nextLine();

            if (filter.equalsIgnoreCase("X")) {
                break;
            }

            if (filter.equalsIgnoreCase("0")) {
                return;
            }

            TransactionSearchFilter selectedFilter = switch (filter) {
                case "1" -> TransactionSearchFilter.DESCRIPTION;
                case "2" -> TransactionSearchFilter.VENDOR;
                case "3" -> TransactionSearchFilter.AMOUNT_RANGE;
                case "4" -> TransactionSearchFilter.DATE_RANGE;
                default -> null;
            };

            if (selectedFilter == null) {
                System.out.println("You have selected an invalid filter. Please try again.");
                continue;
            }

            if (selectedFilter.equals(TransactionSearchFilter.DATE_RANGE)) {
                System.out.print("Please enter the minimum date in the format YYYY-MM-DD: ");
                LocalDate minDate = LocalDate.parse(scanner.nextLine(), DateTimeFormatter.ofPattern(Constants.DATE_FORMAT));
                System.out.print("Please enter the maximum date in the format YYYY-MM-DD: ");
                LocalDate maxDate = LocalDate.parse(scanner.nextLine(), DateTimeFormatter.ofPattern(Constants.DATE_FORMAT));
                DateRange dateRange = new DateRange(minDate, maxDate);
                filters.put(selectedFilter, dateRange);
                System.out.println("Added date range filter"); //filter message
            }

            if (selectedFilter.equals(TransactionSearchFilter.AMOUNT_RANGE)) {
                System.out.print("Please enter the minimum amount: ");
                double minAmount = scanner.nextDouble();
                scanner.nextLine(); // clear the trailing new line character
                System.out.print("Please enter the maximum amount: ");
                double maxAmount = scanner.nextDouble();
                scanner.nextLine(); // clear the trailing new line character
                AmountRange amountRange = new AmountRange(minAmount, maxAmount);
                filters.put(selectedFilter, amountRange);
                System.out.println("Added amount range filter"); //filter message
            }

            if (selectedFilter.equals(TransactionSearchFilter.DESCRIPTION)) {
                System.out.print("Please enter the description filter: ");
                String descriptionFilter = scanner.nextLine();
                filters.put(selectedFilter, descriptionFilter);
                System.out.println("Added description range filter"); //filter message
            }

            if (selectedFilter.equals(TransactionSearchFilter.VENDOR)) {
                System.out.print("Please enter the vendor filter: ");
                String vendorFilter = scanner.nextLine();
                filters.put(selectedFilter, vendorFilter);
                System.out.println("Added vendor filter"); //filter message
            }
        }


        List<Transaction> foundTransactions = transactionManager.getFilteredTransactions(filters);
        System.out.println("Found " + foundTransactions.size() + " transactions matching selected filter(s): \n");
        if(foundTransactions.isEmpty()) {
            return;
        }
        OutputFormatter.printOutput(foundTransactions);
    }
}
