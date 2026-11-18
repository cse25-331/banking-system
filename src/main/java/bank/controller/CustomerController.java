package bank.controller;

import bank.dao.CustomerDAO;
import bank.model.Individual;
import bank.model.Organisation;

public class CustomerController {
    private final CustomerDAO customerDAO = new CustomerDAO();

    public Result registerIndividual(String username, String password, String firstName,
                                     String surname, String address, String contact,
                                     boolean employed) {
        if (isBlank(username) || isBlank(password) || isBlank(firstName)
                || isBlank(surname) || isBlank(address) || isBlank(contact)) {
            return Result.fail("Please fill in all the fields.");
        }
        if (customerDAO.findByUsername(username.trim()) != null) {
            return Result.fail("That username is already taken.");
        }
        String id = nextId();
        customerDAO.save(new Individual(id, username.trim(), password, address.trim(),
                contact.trim(), firstName.trim(), surname.trim(), employed));
        return Result.ok("Registered. Your customer ID is " + id + ".");
    }

    public Result registerOrganisation(String username, String password, String organisationName,
                                       String registrationNumber, String organisationType,
                                       String address, String contact) {
        if (isBlank(username) || isBlank(password) || isBlank(organisationName)
                || isBlank(registrationNumber) || isBlank(organisationType)
                || isBlank(address) || isBlank(contact)) {
            return Result.fail("Please fill in all the fields.");
        }
        if (customerDAO.findByUsername(username.trim()) != null) {
            return Result.fail("That username is already taken.");
        }
        String id = nextId();
        customerDAO.save(new Organisation(id, username.trim(), password, address.trim(),
                contact.trim(), organisationName.trim(), registrationNumber.trim(),
                organisationType));
        return Result.ok("Registered. Your customer ID is " + id + ".");
    }

    private String nextId() {
        return String.format("C%03d", customerDAO.findAll().size() + 1);
    }

    private boolean isBlank(String text) {
        return text == null || text.isBlank();
    }
}