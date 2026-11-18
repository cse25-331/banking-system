package bank.dao;

import bank.model.Account;
import bank.model.ChequeAccount;
import bank.model.InvestmentAccount;
import bank.model.SavingsAccount;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class AccountDAO {
    private final TransactionDAO transactionDAO = new TransactionDAO();

    // CREATE (the account row first, then its transactions)
    public void save(Account a) {
        String sql = """
            INSERT INTO accounts (account_number, account_type, branch, balance, date_opened,
                customer_id, employer_name, employer_address, employer_contact, monthly_salary)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)""";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, a.getAccountNumber());
            ps.setString(2, a.getAccountType());
            ps.setString(3, a.getBranch());
            ps.setDouble(4, a.getBalance());
            ps.setDate(5, Date.valueOf(a.getDateOpened()));
            ps.setString(6, a.getCustomerId());
            if (a instanceof ChequeAccount c) {
                ps.setString(7, c.getEmployerName());
                ps.setString(8, c.getEmployerAddress());
                ps.setString(9, c.getEmployerContact());
                ps.setDouble(10, c.getMonthlySalary());
            } else {
                ps.setNull(7, Types.VARCHAR);
                ps.setNull(8, Types.VARCHAR);
                ps.setNull(9, Types.VARCHAR);
                ps.setNull(10, Types.DOUBLE);
            }
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Could not save account " + a.getAccountNumber(), e);
        }
        transactionDAO.saveAll(a);
    }

    // READ
    public Account findByNumber(String accountNumber) {
        String sql = "SELECT * FROM accounts WHERE account_number = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, accountNumber);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return fromRow(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Could not read account", e);
        }
        return null;
    }

    public List<Account> findByCustomer(String customerId) {
        String sql = "SELECT * FROM accounts WHERE customer_id = ? ORDER BY account_number";
        List<Account> list = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, customerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(fromRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Could not read accounts", e);
        }
        return list;
    }

    // UPDATE (saves the new balance and any new transactions)
    public void update(Account a) {
        String sql = "UPDATE accounts SET balance = ? WHERE account_number = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, a.getBalance());
            ps.setString(2, a.getAccountNumber());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Could not update account", e);
        }
        transactionDAO.saveAll(a);
    }

    // DELETE
    public void delete(String accountNumber) {
        transactionDAO.deleteByAccount(accountNumber);
        String sql = "DELETE FROM accounts WHERE account_number = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, accountNumber);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Could not delete account", e);
        }
    }

    public void deleteByCustomer(String customerId) {
        for (Account a : findByCustomer(customerId)) {
            delete(a.getAccountNumber());
        }
    }

    // Rebuilds the right kind of account from a database row
    private Account fromRow(ResultSet rs) throws SQLException {
        String number = rs.getString("account_number");
        String type = rs.getString("account_type");
        String branch = rs.getString("branch");
        String customerId = rs.getString("customer_id");

        Account account = switch (type) {
            case "SAVINGS" -> new SavingsAccount(number, branch, customerId);
            case "INVESTMENT" -> new InvestmentAccount(number, branch, customerId);
            default -> new ChequeAccount(number, branch, customerId,
                    blank(rs.getString("employer_name")),
                    blank(rs.getString("employer_address")),
                    blank(rs.getString("employer_contact")),
                    rs.getDouble("monthly_salary"));
        };
        account.restoreState(rs.getDouble("balance"),
                rs.getDate("date_opened").toLocalDate(),
                transactionDAO.findByAccount(number));
        return account;
    }

    private String blank(String value) {
        return value == null ? "" : value;
    }
}