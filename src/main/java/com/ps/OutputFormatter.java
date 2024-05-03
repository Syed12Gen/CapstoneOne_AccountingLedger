package com.ps;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OutputFormatter {
    public final static String[] TABLE_HEADERS = new String[] {"Date", "Time", "Description", "Vendor", "Amount"};

    public static void printOutput(List<Transaction> transactions) {
        List<List<String>> transactionFormatData = new ArrayList<>();
        transactionFormatData.add(Arrays.asList(TABLE_HEADERS));

        for(Transaction transaction: transactions) {
            List<String> transactionTokens = new ArrayList<>();
            transactionTokens.add(transaction.getDate() + "  | ");
            transactionTokens.add(transaction.getTime() + "  | ");
            transactionTokens.add(transaction.getDescription() + "  | ");
            transactionTokens.add(transaction.getVendor() + "   | ");
            transactionTokens.add(transaction.getAmount() + "");
            transactionFormatData.add(transactionTokens);
        }

        // Find the maximum length of each column
        int[] maxLengths = new int[transactionFormatData.get(0).size()];
        for (List<String> row : transactionFormatData) {
            for (int i = 0; i < row.size(); i++) {
                maxLengths[i] = Math.max(maxLengths[i], row.get(i).length());
            }
        }

        // Print the table
        for (List<String> row : transactionFormatData) {
            for (int i = 0; i < row.size(); i++) {
                System.out.print(padRight(row.get(i), maxLengths[i] + 2)); // Add padding of 2 for each cell
            }
            System.out.println();
        }
    }

    private static String padRight(String s, int length) {
        return String.format("%-" + length + "s", s);
    }
}