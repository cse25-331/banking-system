package bank.controller;

import bank.dao.AccountDAO;
import bank.dao.CustomerDAO;
import bank.model.Account;
import bank.model.Customer;
import bank.model.MonthlyProcessor;

import java.util.ArrayList;
import java.util.List;

public class MonthlyController {
    private final CustomerDAO customerDAO = new CustomerDAO();
    private final AccountDAO accountDAO = new AccountDAO();

    public Result runMonthlyProcess() {
        List<Account> all = new ArrayList<>();
        for (Customer c : customerDAO.findAll()) {
            all.addAll(c.getAccounts());
        }
        new MonthlyProcessor(all).runMonthlyProcess();
        for (Account a : all) {
            accountDAO.update(a);
        }
        return Result.ok("Monthly interest and fees applied to " + all.size() + " accounts.");
    }
}