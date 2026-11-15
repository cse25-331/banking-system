package bank.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Account {
    private static int transactionCounter = 1;

    private final String accountNumber;
    private double balance;
    private final String branch;
    private final LocalDate dateOpened;
    private final String customerId;
    private final List<Transaction> transactions = new ArrayList<>();

    public Account(String accountNumber, String branch, String customerId) {
        this.accountNumber = accountNumber;
        this.branch = branch;
        this.customerId = customerId;
        this.dateOpened = LocalDate.now();
        this.balance = 0;
    }

    // Overloaded: with and without a description
    public boolean deposit(double amount) {
        return deposit(amount, "Deposit");
    }

    public boolean deposit(double amount, String description) {
        if (amount <= 0) {
            return false;
        }
        addTransaction("DEPOSIT", amount, description);
        return true;
    }

    // Subclasses can override this (SavingsAccount refuses every withdrawal)
    public boolean withdraw(double amount) {
        if (amount <= 0 || amount > balance) {
            return false;
        }
        recordTransaction("WITHDRAWAL", amount);
        return true;
    }

    // The only way the balance changes (there is no setBalance)
    protected void recordTransaction(String type, double amount) {
        addTransaction(type, amount, type);
    }

    private void addTransaction(String type, double amount, String description) {
        if (type.equals("DEPOSIT") || type.equals("INTEREST")) {
            balance += amount;
        } else {
            balance -= amount;          // WITHDRAWAL or FEE
        }
        String id = String.format("T%04d", transactionCounter++);
        transactions.add(new Transaction(id, type, amount, balance, description));
    }

    public abstract String getAccountType();

    public double getBalance() { return balance; }
    public String getAccountNumber() { return accountNumber; }
    public String getBranch() { return branch; }
    public LocalDate getDateOpened() { return dateOpened; }
    public String getCustomerId() { return customerId; }

    public List<Transaction> getTransactions() {
        return Collections.unmodifiableList(transactions);
    }
}