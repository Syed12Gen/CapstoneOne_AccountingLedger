package com.ps;

//This is "Main" clas will handle the user interface and interaction.

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        TransactionManager transactionManager = new TransactionManager("transactions.txt");
        transactionManager.loadTransactions();
        Menu menu = new Menu(transactionManager);
        menu.displayMainMenu();

    }
}
