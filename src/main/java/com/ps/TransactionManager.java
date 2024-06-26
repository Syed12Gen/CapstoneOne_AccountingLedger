package com.ps;

import java.io.*;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class TransactionManager {
    //
    private ArrayList<Transaction> transactions;
    private String filepath;

    public TransactionManager(String filepath) {
        this.transactions = new ArrayList<>();
        this.filepath = filepath;
    }

    public void loadTransactions() {
        transactions.clear();
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

    public List<Transaction> getMonthToDateTransactions() {
        List<Transaction> monthToDateTransactions = new ArrayList<>();
        LocalDate startOfMonth = LocalDate.now().withDayOfMonth(1);
        LocalDate today = LocalDate.now();
        DateRange dateRange = new DateRange(startOfMonth, today);

        loadTransactions();

        for (Transaction transaction : transactions) {
            LocalDate transactionDate = LocalDate.parse(transaction.getDate(), DateTimeFormatter.ofPattern(Constants.DATE_FORMAT));
            if (dateRange.includes(transactionDate)) {
                monthToDateTransactions.add(transaction);
            }
        }

        return monthToDateTransactions;
    }

    //YearToDate Method

    public List<Transaction> getYearToDateTransactions() {
        List<Transaction> yearToDateTransactions = new ArrayList<>();
        LocalDate today = LocalDate.now();
        LocalDate startOfYear = today.withDayOfYear(1);
        DateRange dateRange = new DateRange(startOfYear, today);

        loadTransactions();

        for (Transaction transaction : transactions) {
            LocalDate transactionDate = LocalDate.parse(transaction.getDate(), DateTimeFormatter.ofPattern(Constants.DATE_FORMAT));
            if (dateRange.includes(transactionDate)) {
                yearToDateTransactions.add(transaction);
            }
        }

        return yearToDateTransactions;
    }

    //new method getPreviousMonthTransactions

    public List<Transaction> getPreviousMonthTransactions() {
        List<Transaction> previousMonthTransactions = new ArrayList<>();
        LocalDate now = LocalDate.now();

        LocalDate startOfPreviousMonth = now
                .minusMonths(1)
                .withDayOfMonth(1);

        Month previousMonth = now
                .minusMonths(1)
                .getMonth();

        LocalDate endOfPreviousMonth = now
                .minusMonths(1)
                .withDayOfMonth(previousMonth.length(isLeapYear(startOfPreviousMonth.getYear())));

        DateRange dateRange = new DateRange(startOfPreviousMonth, endOfPreviousMonth);

        for (Transaction transaction : transactions) {
            LocalDate transactionDate = LocalDate.parse(transaction.getDate(), DateTimeFormatter.ofPattern(Constants.DATE_FORMAT));
            if (dateRange.includes(transactionDate)) {
                previousMonthTransactions.add(transaction);
            }
        }

        return previousMonthTransactions;
    }

    //new method previousYearTransactions
    public List<Transaction> getPreviousYearTransactions() {
        List<Transaction> previousYearTransactions = new ArrayList<>();
        LocalDate now = LocalDate.now();

        LocalDate startOfPreviousYear = now
                .minusYears(1)
                .withMonth(1)
                .withDayOfMonth(1);

        LocalDate previousYear = now.minusYears(1);
        LocalDate endOfPreviousYear;

        if (isLeapYear(previousYear.getYear())) {
            endOfPreviousYear = previousYear.withDayOfYear(366);
        } else {
            endOfPreviousYear = previousYear.withDayOfYear(365);
        }

        DateRange dateRange = new DateRange(startOfPreviousYear, endOfPreviousYear);

        for (Transaction transaction : transactions) {
            LocalDate transactionDate = LocalDate.parse(transaction.getDate(), DateTimeFormatter.ofPattern(Constants.DATE_FORMAT));
            if (dateRange.includes(transactionDate)) {
                previousYearTransactions.add(transaction);
            }
        }

        return previousYearTransactions;
    }

    //FilteredTransactions
    public List<Transaction> getFilteredTransactions(Map<TransactionSearchFilter, Object> filters) {
        loadTransactions();
        Set<Transaction> filteredTransactions = new HashSet<>(transactions);

        for (Map.Entry<TransactionSearchFilter, Object> entry : filters.entrySet()) {
            TransactionSearchFilter filter = entry.getKey();
            Object value = entry.getValue();

            switch (filter) {
                case DESCRIPTION:
                    String descriptionFilter = (String) value;
                    filteredTransactions.retainAll(filterByDescription(descriptionFilter, transactions));
                    break;
                case AMOUNT_RANGE:
                    AmountRange amountRangeFilter = (AmountRange) value;
                    filteredTransactions.retainAll(filterByAmountRange(amountRangeFilter, transactions));
                    break;
                case VENDOR:
                    String vendorFilter = (String) value;
                    filteredTransactions.retainAll(filterByVendor(vendorFilter, transactions));
                    break;
                case DATE_RANGE:
                    DateRange dateRangeFilter = (DateRange) value;
                    filteredTransactions.retainAll(filterByDateRange(dateRangeFilter, transactions));
                    break;
                // Add more cases for other filters if needed
                default:
                    throw new IllegalArgumentException("Unsupported filter: " + filter);
            }
        }

        return new ArrayList<>(filteredTransactions);
    }


    //FilterByVendor Method

    public List<Transaction> filterByVendor(String vendorFilter, List<Transaction> transactions) {
        List<Transaction> filteredTransactions = new ArrayList<>();

        for (Transaction transaction : transactions) {
            if (transaction.getVendor().equals(vendorFilter)) {
                filteredTransactions.add(transaction);
            }
        }

        return filteredTransactions;
    }

    //filterByDate Method

    public List<Transaction> filterByDateRange(DateRange dateRange, List<Transaction> transactions) {
        List<Transaction> filteredTransactions = new ArrayList<>();

        for (Transaction transaction : transactions) {
            LocalDate transactionDate =
                    LocalDate.parse(transaction.getDate(), DateTimeFormatter.ofPattern(Constants.DATE_FORMAT));
            if (dateRange.includes(transactionDate)) {
                filteredTransactions.add(transaction);
            }
        }

        return filteredTransactions;
    }

    //filterByAmount Method

    public List<Transaction> filterByAmountRange(AmountRange amountRange, List<Transaction> transactions) {
        List<Transaction> filteredTransactions = new ArrayList<>();

        for (Transaction transaction : transactions) {
            if (amountRange.includes(transaction.getAmount())) {
                filteredTransactions.add(transaction);
            }
        }

        return filteredTransactions;
    }

    //filterByDescription

    public List<Transaction> filterByDescription(String descriptionFilter, List<Transaction> transactions) {
        List<Transaction> filteredTransactions = new ArrayList<>();

        for (Transaction transaction : transactions) {
            if (transaction.getDescription().equals(descriptionFilter)) {
                filteredTransactions.add(transaction);
            }
        }

        return filteredTransactions;
    }


    //leapYear Method
    private static boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
    }


}
