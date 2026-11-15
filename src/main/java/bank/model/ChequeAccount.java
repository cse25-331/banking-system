package bank.model;

public class ChequeAccount extends Account {
    private static final double FEE_PER_HUNDRED = 1.00;

    private final String employerName;
    private final String employerAddress;
    private final String employerContact;
    private final double monthlySalary;

    // For an individual: employer details are required
    public ChequeAccount(String accountNumber, String branch, String customerId,
                         String employerName, String employerAddress,
                         String employerContact, double monthlySalary) {
        super(accountNumber, branch, customerId);
        this.employerName = employerName;
        this.employerAddress = employerAddress;
        this.employerContact = employerContact;
        this.monthlySalary = monthlySalary;
    }

    // For an organisation: no employer details
    public ChequeAccount(String accountNumber, String branch, String customerId) {
        this(accountNumber, branch, customerId, "", "", "", 0);
    }

    // BWP1 for every full BWP100 in the account
    public double calculateFee() {
        return Math.floor(getBalance() / 100) * FEE_PER_HUNDRED;
    }

    public void applyMonthlyFee() {
        double fee = calculateFee();
        if (fee > 0) {
            recordTransaction("FEE", fee);
        }
    }

    @Override
    public String getAccountType() {
        return "CHEQUE";
    }

    public String getEmployerName() { return employerName; }
    public String getEmployerAddress() { return employerAddress; }
    public String getEmployerContact() { return employerContact; }
    public double getMonthlySalary() { return monthlySalary; }
}