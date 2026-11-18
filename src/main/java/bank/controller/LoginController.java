package bank.controller;

import bank.dao.CustomerDAO;
import bank.model.Customer;

public class LoginController {
    private final CustomerDAO customerDAO = new CustomerDAO();
    private Customer currentCustomer;

    public Result login(String username, String password) {
        if (username == null || username.isBlank() || password == null || password.isBlank()) {
            return Result.fail("Enter your username and password.");
        }
        Customer customer = customerDAO.findByUsername(username.trim());
        if (customer != null && customer.login(username.trim(), password)) {
            currentCustomer = customer;
            return Result.ok("Welcome, " + customer.getDisplayName());
        }
        return Result.fail("Invalid username or password.");
    }

    public void logout() {
        if (currentCustomer != null) {
            currentCustomer.logout();
            currentCustomer = null;
        }
    }

    public Customer getCurrentCustomer() { return currentCustomer; }
}