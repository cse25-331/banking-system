package bank.model;

public class SavingsAccount extends Account implements InterestBearing {
    private static final double INTEREST_RATE = 0.0005;   // 0.05% per month

    public SavingsAccount(String accountNumber, String branch, String customerId) {
        super(accountNumber, branch, customerId);
    }

    @Override
    public boolean withdraw(double amount) {
        return false;   // Savings accounts never allow withdrawals
    }

    @Override
    public double calculateInterest() {
        return getBalance() * INTEREST_RATE;
    }

    @Override
    public void payInterest() {
        double interest = calculateInterest();
        if (interest > 0) {
            recordTransaction("INTEREST", interest);
        }
    }

    @Override
    public String getAccountType() {
        return "SAVINGS";
    }
}