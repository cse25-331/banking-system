package bank.dao;

import bank.model.*;

public class SampleData {
    private static final CustomerDAO customerDAO = new CustomerDAO();
    private static final AccountDAO accountDAO = new AccountDAO();

    public static void main(String[] args) {
        DatabaseManager.createTables();

        if (customerDAO.findAll().isEmpty()) {
            insertSampleData();
            System.out.println("10 sample customers inserted.");
        } else {
            System.out.println("Sample data is already in the database.");
        }

        for (Customer c : customerDAO.findAll()) {
            System.out.println(c.getCustomerId() + "  " + c.getDisplayName());
            for (Account a : c.getAccounts()) {
                System.out.println("     " + a.getAccountType() + " " + a.getAccountNumber()
                        + "  balance: " + a.getBalance()
                        + "  (" + a.getTransactions().size() + " transactions)");
            }
        }
    }

    private static void insertSampleData() {
        Individual c1 = person("C001", "thato", "Thato", "Mokoena", "Gaborone", "71234501", true);
        customerDAO.save(c1);
        SavingsAccount s1 = new SavingsAccount("S001", "Main", "C001");
        s1.deposit(1500);
        InvestmentAccount i1 = new InvestmentAccount("I001", "Main", "C001", 1000);
        ChequeAccount q1 = new ChequeAccount("Q001", "Main", "C001", "Acme Ltd", "Gaborone", "3901234", 8000);
        q1.deposit(8000, "Salary");
        q1.withdraw(1200);
        save(s1, i1, q1);

        Individual c2 = person("C002", "neo", "Neo", "Kgosi", "Francistown", "71234502", true);
        customerDAO.save(c2);
        SavingsAccount s2 = new SavingsAccount("S002", "Main", "C002");
        s2.deposit(800);
        ChequeAccount q2 = new ChequeAccount("Q002", "Main", "C002", "Power Corporation", "Francistown", "2410000", 12000);
        q2.deposit(12000, "Salary");
        q2.withdraw(3000);
        save(s2, q2);

        Individual c3 = person("C003", "lesedi", "Lesedi", "Molefe", "Maun", "71234503", false);
        customerDAO.save(c3);
        InvestmentAccount i3 = new InvestmentAccount("I003", "Main", "C003", 2500);
        i3.deposit(500);
        save(i3);

        Individual c4 = person("C004", "kabo", "Kabo", "Seretse", "Palapye", "71234504", false);
        customerDAO.save(c4);
        SavingsAccount s4 = new SavingsAccount("S004", "Main", "C004");
        s4.deposit(300);
        save(s4);

        Individual c5 = person("C005", "mpho", "Mpho", "Tau", "Serowe", "71234505", true);
        customerDAO.save(c5);
        ChequeAccount q5 = new ChequeAccount("Q005", "Main", "C005", "Retail Stores Ltd", "Serowe", "4630000", 6500);
        q5.deposit(6500, "Salary");
        save(q5);

        Individual c6 = person("C006", "boitumelo", "Boitumelo", "Dube", "Kasane", "71234506", false);
        customerDAO.save(c6);
        SavingsAccount s6 = new SavingsAccount("S006", "Main", "C006");
        s6.deposit(2200);
        InvestmentAccount i6 = new InvestmentAccount("I006", "Main", "C006", 600);
        save(s6, i6);

        Individual c7 = person("C007", "tebogo", "Tebogo", "Sithole", "Lobatse", "71234507", true);
        customerDAO.save(c7);
        ChequeAccount q7 = new ChequeAccount("Q007", "Main", "C007", "Clay Works", "Lobatse", "5330000", 4800);
        q7.deposit(4800, "Salary");
        q7.withdraw(800);
        save(q7);

        Individual c8 = person("C008", "kagiso", "Kagiso", "Ramotswe", "Selebi-Phikwe", "71234508", false);
        customerDAO.save(c8);
        SavingsAccount s8 = new SavingsAccount("S008", "Main", "C008");
        s8.deposit(150);
        save(s8);

        Organisation c9 = new Organisation("C009", "youthtrust", "pass123", "Kasane",
                "3901111", "Kalahari Youth Trust", "NGO-2019-044", "NGO");
        customerDAO.save(c9);
        SavingsAccount s9 = new SavingsAccount("S009", "Main", "C009");
        s9.deposit(5000);
        ChequeAccount q9 = new ChequeAccount("Q009", "Main", "C009");
        q9.deposit(9000);
        save(s9, q9);

        Organisation c10 = new Organisation("C010", "okavango", "pass123", "Maun",
                "6860000", "Okavango Traders (Pty) Ltd", "BW-2015-1182", "BUSINESS");
        customerDAO.save(c10);
        InvestmentAccount i10 = new InvestmentAccount("I010", "Main", "C010", 10000);
        ChequeAccount q10 = new ChequeAccount("Q010", "Main", "C010");
        q10.deposit(25000);
        q10.withdraw(4000);
        save(i10, q10);
    }

    private static Individual person(String id, String username, String first, String surname,
                                     String address, String phone, boolean employed) {
        return new Individual(id, username, "pass123", address, phone, first, surname, employed);
    }

    private static void save(Account... accounts) {
        for (Account a : accounts) {
            accountDAO.save(a);
        }
    }
}