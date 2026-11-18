package bank.model;

import java.util.List;

public class MonthlyProcessor {
    private final List<Account> accounts;

    public MonthlyProcessor(List<Account> accounts) {
        this.accounts = accounts;
    }

    // Interest first, then the Cheque fee (the client's rule)
    public void runMonthlyProcess() {
        payAllInterest();
        applyAllFees();
    }

    public void payAllInterest() {
        for (Account account : accounts) {
            if (account instanceof InterestBearing ib) {
                ib.payInterest();
            }
        }
    }

    public void applyAllFees() {
        for (Account account : accounts) {
            if (account instanceof ChequeAccount cheque) {
                cheque.applyMonthlyFee();
            }
        }
    }
}