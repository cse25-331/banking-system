package bank.dao;

import bank.model.Account;
import bank.model.Transaction;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAO {

    // CREATE (MERGE adds a new row, or updates it if the id already exists)
    public void save(Transaction t, String accountNumber) {
        String sql = """
            MERGE INTO transactions (transaction_id, account_number, transaction_date,
                type, amount, balance_after, description)
            KEY (transaction_id)
            VALUES (?, ?, ?, ?, ?, ?, ?)""";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, t.getTransactionId());
            ps.setString(2, accountNumber);
            ps.setDate(3, Date.valueOf(t.getDate()));
            ps.setString(4, t.getType());
            ps.setDouble(5, t.getAmount());
            ps.setDouble(6, t.getBalanceAfter());
            ps.setString(7, t.getDescription());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Could not save transaction " + t.getTransactionId(), e);
        }
    }

    public void saveAll(Account account) {
        for (Transaction t : account.getTransactions()) {
            save(t, account.getAccountNumber());
        }
    }

    // READ
    public List<Transaction> findByAccount(String accountNumber) {
        String sql = """
            SELECT * FROM transactions WHERE account_number = ?
            ORDER BY transaction_date, LENGTH(transaction_id), transaction_id""";
        List<Transaction> list = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, accountNumber);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Transaction(
                            rs.getString("transaction_id"),
                            rs.getDate("transaction_date").toLocalDate(),
                            rs.getString("type"),
                            rs.getDouble("amount"),
                            rs.getDouble("balance_after"),
                            rs.getString("description")));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Could not read transactions", e);
        }
        return list;
    }

    // DELETE
    public void deleteByAccount(String accountNumber) {
        String sql = "DELETE FROM transactions WHERE account_number = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, accountNumber);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Could not delete transactions", e);
        }
    }
}