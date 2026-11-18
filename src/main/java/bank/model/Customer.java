package bank.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Customer extends User {
    private final String customerId;
    private final String address;
    private final String contactDetails;
    private final List<Account> accounts = new ArrayList<>();

    public Customer(String customerId, String username, String password,
                    String address, String contactDetails) {
        super(customerId, username, password, "CUSTOMER");
        this.customerId = customerId;
        this.address = address;
        this.contactDetails = contactDetails;
    }

    // A customer may hold only one account of each type
    public boolean addAccount(Account account) {
        if (hasAccountOfType(account.getAccountType())) {
            return false;
        }
        accounts.add(account);
        return true;
    }

    public boolean hasAccountOfType(String type) {
        for (Account a : accounts) {
            if (a.getAccountType().equals(type)) {
                return true;
            }
        }
        return false;
    }

    public List<Account> getAccounts() {
        return Collections.unmodifiableList(accounts);
    }

    public abstract String getDisplayName();

    public String getCustomerId() { return customerId; }
    public String getAddress() { return address; }
    public String getContactDetails() { return contactDetails; }
}