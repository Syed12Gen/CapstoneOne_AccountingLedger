package com.ps;

public class Main {
    public static void main(String[] args) {
        TransactionManager transactionManager = new TransactionManager("transactions.txt");
        transactionManager.loadTransactions();
        Menu menu = new Menu(transactionManager);
        menu.displayMainMenu();

    }
}
