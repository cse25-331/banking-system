package bank.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
    private static final String URL = "jdbc:h2:./data/bankdb";   // saved in the data folder
    private static final String USER = "sa";
    private static final String PASSWORD = "";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void createTables() {
        String customers = """
            CREATE TABLE IF NOT EXISTS customers (
                customer_id VARCHAR(20) PRIMARY KEY,
                customer_type VARCHAR(15) NOT NULL,
                username VARCHAR(30) NOT NULL UNIQUE,
                password VARCHAR(50) NOT NULL,
                address VARCHAR(100),
                contact_details VARCHAR(50),
                first_name VARCHAR(40),
                surname VARCHAR(40),
                employed BOOLEAN,
                organisation_name VARCHAR(80),
                registration_number VARCHAR(30),
                organisation_type VARCHAR(15)
            )""";

        String accounts = """
            CREATE TABLE IF NOT EXISTS accounts (
                account_number VARCHAR(20) PRIMARY KEY,
                account_type VARCHAR(15) NOT NULL,
                branch VARCHAR(40),
                balance DOUBLE NOT NULL,
                date_opened DATE NOT NULL,
                customer_id VARCHAR(20) NOT NULL,
                employer_name VARCHAR(60),
                employer_address VARCHAR(100),
                employer_contact VARCHAR(30),
                monthly_salary DOUBLE,
                FOREIGN KEY (customer_id) REFERENCES customers(customer_id),
                UNIQUE (customer_id, account_type)
            )""";

        String transactions = """
            CREATE TABLE IF NOT EXISTS transactions (
                transaction_id VARCHAR(40) PRIMARY KEY,
                account_number VARCHAR(20) NOT NULL,
                transaction_date DATE NOT NULL,
                type VARCHAR(15) NOT NULL,
                amount DOUBLE NOT NULL,
                balance_after DOUBLE NOT NULL,
                description VARCHAR(100),
                FOREIGN KEY (account_number) REFERENCES accounts(account_number)
            )""";

        try (Connection conn = getConnection(); Statement st = conn.createStatement()) {
            st.execute(customers);
            st.execute(accounts);
            st.execute(transactions);
        } catch (SQLException e) {
            throw new RuntimeException("Could not create the tables", e);
        }
    }

    public static void main(String[] args) {
        createTables();
        System.out.println("Database ready: tables created");
    }
}