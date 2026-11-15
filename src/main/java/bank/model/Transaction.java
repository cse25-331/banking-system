package bank.model;

import java.time.LocalDate;

public class Transaction {
    private final String transactionId;
    private final LocalDate date;
    private final String type;          // DEPOSIT, WITHDRAWAL, INTEREST or FEE
    private final double amount;
    private final double balanceAfter;
    private final String description;

    public Transaction(String transactionId, String type, double amount,
                       double balanceAfter, String description) {
        this.transactionId = transactionId;
        this.date = LocalDate.now();
        this.type = type;
        this.amount = amount;
        this.balanceAfter = balanceAfter;
        this.description = description;
    }

    public String getTransactionId() { return transactionId; }
    public LocalDate getDate() { return date; }
    public String getType() { return type; }
    public double getAmount() { return amount; }
    public double getBalanceAfter() { return balanceAfter; }
    public String getDescription() { return description; }

    @Override
    public String toString() {
        return date + " " + type + " " + amount + " balance: " + balanceAfter;
    }
}