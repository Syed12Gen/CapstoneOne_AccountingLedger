package com.ps;

import java.util.Scanner;

public class Menu {
    private TransactionManager transactionManager;

    public Menu(TransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    public void displayMainMenu() {
        Scanner scanner = new Scanner(System.in);
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
                    handleTransaction(scanner, false);
                    break;
                case "D":
                    handleTransaction(scanner, true);
                    break;
                case "R":
                    displayReportsMenu(scanner);
                    break;
                case "X":
                    System.out.println("Exiting program.");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        } while (!option.equalsIgnoreCase("X"));
        scanner.close();
    }

    private void handleTransaction(Scanner scanner, boolean isDeposit) {
        System.out.print("Enter vendor name: ");
        String vendor = scanner.next();
        System.out.print("Enter amount: ");
        double amount = scanner.nextDouble();
        if (!isDeposit) {
            amount = -amount; // Negative for payments
        }
        Transaction transaction = new Transaction("2023-04-15", "12:00:00", "Transaction", vendor, amount);
        transactionManager.addTransaction(transaction);
        System.out.println("Transaction recorded.");
    }

    private void displayReportsMenu(Scanner scanner) {
        // Implementation similar to previous example
    }
}
