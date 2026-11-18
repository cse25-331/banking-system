package bank.dao;

import bank.model.Account;
import bank.model.Customer;
import bank.model.Individual;
import bank.model.Organisation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO {
    private final AccountDAO accountDAO = new AccountDAO();

    // CREATE
    public void save(Customer c) {
        String sql = """
            INSERT INTO customers (customer_id, customer_type, username, password, address,
                contact_details, first_name, surname, employed,
                organisation_name, registration_number, organisation_type)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)""";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getCustomerId());
            ps.setString(3, c.getUsername());
            ps.setString(4, c.getPassword());
            ps.setString(5, c.getAddress());
            ps.setString(6, c.getContactDetails());
            if (c instanceof Individual i) {
                ps.setString(2, "INDIVIDUAL");
                ps.setString(7, i.getFirstName());
                ps.setString(8, i.getSurname());
                ps.setBoolean(9, i.isEmployed());
                ps.setNull(10, Types.VARCHAR);
                ps.setNull(11, Types.VARCHAR);
                ps.setNull(12, Types.VARCHAR);
            } else if (c instanceof Organisation o) {
                ps.setString(2, "ORGANISATION");
                ps.setNull(7, Types.VARCHAR);
                ps.setNull(8, Types.VARCHAR);
                ps.setNull(9, Types.BOOLEAN);
                ps.setString(10, o.getDisplayName());
                ps.setString(11, o.getRegistrationNumber());
                ps.setString(12, o.getOrganisationType());
            }
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Could not save customer " + c.getCustomerId(), e);
        }
    }

    // READ
    public Customer findById(String customerId) {
        return findOne("customer_id", customerId);
    }

    public Customer findByUsername(String username) {
        return findOne("username", username);
    }

    public List<Customer> findAll() {
        List<Customer> list = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM customers ORDER BY customer_id");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(fromRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Could not read customers", e);
        }
        return list;
    }

    // UPDATE
    public void updateContactDetails(String customerId, String address, String contactDetails) {
        String sql = "UPDATE customers SET address = ?, contact_details = ? WHERE customer_id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, address);
            ps.setString(2, contactDetails);
            ps.setString(3, customerId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Could not update customer", e);
        }
    }

    // DELETE (the customer's accounts and transactions go first)
    public void delete(String customerId) {
        accountDAO.deleteByCustomer(customerId);
        String sql = "DELETE FROM customers WHERE customer_id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, customerId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Could not delete customer", e);
        }
    }

    private Customer findOne(String column, String value) {
        String sql = "SELECT * FROM customers WHERE " + column + " = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, value);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return fromRow(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Could not read customer", e);
        }
        return null;
    }

    // Rebuilds an Individual or an Organisation, and loads the customer's accounts
    private Customer fromRow(ResultSet rs) throws SQLException {
        String id = rs.getString("customer_id");
        Customer c;
        if (rs.getString("customer_type").equals("INDIVIDUAL")) {
            c = new Individual(id, rs.getString("username"), rs.getString("password"),
                    rs.getString("address"), rs.getString("contact_details"),
                    rs.getString("first_name"), rs.getString("surname"),
                    rs.getBoolean("employed"));
        } else {
            c = new Organisation(id, rs.getString("username"), rs.getString("password"),
                    rs.getString("address"), rs.getString("contact_details"),
                    rs.getString("organisation_name"), rs.getString("registration_number"),
                    rs.getString("organisation_type"));
        }
        for (Account a : accountDAO.findByCustomer(id)) {
            c.addAccount(a);
        }
        return c;
    }
}