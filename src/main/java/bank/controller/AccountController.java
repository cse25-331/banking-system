package bank.controller;

import bank.dao.AccountDAO;
import bank.model.Account;
import bank.model.ChequeAccount;
import bank.model.Customer;
import bank.model.Individual;
import bank.model.InvestmentAccount;
import bank.model.SavingsAccount;
import bank.model.Transaction;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

public class AccountController {
    private final Customer customer;
    private final AccountDAO accountDAO = new AccountDAO();

    public AccountController(Customer customer) {
        this.customer = customer;
    }

    public List<Account> getAccounts() {
        return customer.getAccounts();
    }

    public Result openAccount(String type, String initialDepositText, String employerName,
                              String employerAddress, String employerContact, String salaryText) {
        if (type == null) {
            return Result.fail("Choose an account type.");
        }
        if (customer.hasAccountOfType(type)) {
            return Result.fail("You already have a " + type.toLowerCase() + " account.");
        }
        String id = customer.getCustomerId();
        String number = prefix(type) + id.substring(1);
        Account account;

        if (type.equals("SAVINGS")) {
            account = new SavingsAccount(number, "Main", id);
        } else if (type.equals("INVESTMENT")) {
            Double deposit = parseAmount(initialDepositText);
            if (deposit == null || deposit < 500) {
                return Result.fail("An Investment account needs an opening deposit of at least BWP500.00.");
            }
            account = new InvestmentAccount(number, "Main", id, deposit);
        } else if (type.equals("CHEQUE")) {
            if (customer instanceof Individual person) {
                if (!person.isEmployed()) {
                    return Result.fail("A Cheque account is only for people who are working.");
                }
                Double salary = parseAmount(salaryText);
                if (isBlank(employerName) || isBlank(employerAddress)
                        || isBlank(employerContact) || salary == null) {
                    return Result.fail("Enter the employer name, address, contact and a valid monthly salary.");
                }
                account = new ChequeAccount(number, "Main", id, employerName.trim(),
                        employerAddress.trim(), employerContact.trim(), salary);
            } else {
                account = new ChequeAccount(number, "Main", id);
            }
        } else {
            return Result.fail("Choose an account type.");
        }

        customer.addAccount(account);
        accountDAO.save(account);
        return Result.ok(type.charAt(0) + type.substring(1).toLowerCase()
                + " account opened. Account number: " + number);
    }

    public Result deposit(String accountNumber, String amountText) {
        Account account = find(accountNumber);
        if (account == null) {
            return Result.fail("Choose an account.");
        }
        Double amount = parseAmount(amountText);
        if (amount == null) {
            return Result.fail("Enter a valid amount.");
        }
        if (!account.deposit(amount)) {
            return Result.fail("Enter an amount greater than zero.");
        }
        accountDAO.update(account);
        return Result.ok("Deposit successful. New balance: BWP" + money(account.getBalance()));
    }

    public Result withdraw(String accountNumber, String amountText) {
        Account account = find(accountNumber);
        if (account == null) {
            return Result.fail("Choose an account.");
        }
        if (account.getAccountType().equals("SAVINGS")) {
            return Result.fail("Withdrawals are not allowed on a Savings account.");
        }
        Double amount = parseAmount(amountText);
        if (amount == null || amount <= 0) {
            return Result.fail("Enter an amount greater than zero.");
        }
        if (amount > account.getBalance()) {
            return Result.fail("Insufficient funds.");
        }
        account.withdraw(amount);
        accountDAO.update(account);
        return Result.ok("Withdrawal successful. New balance: BWP" + money(account.getBalance()));
    }

    public double getBalance(String accountNumber) {
        Account account = find(accountNumber);
        return account == null ? 0 : account.getBalance();
    }

    public List<Transaction> getHistory(String accountNumber) {
        Account account = find(accountNumber);
        return account == null ? new ArrayList<>() : account.getTransactions();
    }

    public List<Transaction> getHistoryForMonth(String accountNumber, YearMonth month) {
        List<Transaction> result = new ArrayList<>();
        for (Transaction t : getHistory(accountNumber)) {
            if (YearMonth.from(t.getDate()).equals(month)) {
                result.add(t);
            }
        }
        return result;
    }

    private Account find(String accountNumber) {
        for (Account a : customer.getAccounts()) {
            if (a.getAccountNumber().equals(accountNumber)) {
                return a;
            }
        }
        return null;
    }

    private String prefix(String type) {
        if (type.equals("SAVINGS")) return "S";
        if (type.equals("INVESTMENT")) return "I";
        return "Q";
    }

    private Double parseAmount(String text) {
        if (text == null) {
            return null;
        }
        try {
            return Double.parseDouble(text.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private String money(double value) {
        return String.format("%.2f", value);
    }

    private boolean isBlank(String text) {
        return text == null || text.isBlank();
    }
}