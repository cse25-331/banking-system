package bank.model;

public class InvestmentAccount extends Account implements InterestBearing {
    private static final double INTEREST_RATE = 0.05;              // 5% per month
    private static final double MINIMUM_OPENING_DEPOSIT = 500.00;

    public InvestmentAccount(String accountNumber, String branch,
                             String customerId, double initialDeposit) {
        super(accountNumber, branch, customerId);
        if (initialDeposit < MINIMUM_OPENING_DEPOSIT) {
            throw new IllegalArgumentException(
                "An Investment account needs an opening deposit of at least BWP" + MINIMUM_OPENING_DEPOSIT);
        }
        deposit(initialDeposit, "Initial deposit");
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
        return "INVESTMENT";
    }
}