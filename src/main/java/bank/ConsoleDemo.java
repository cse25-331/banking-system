package bank;

import bank.model.*;

public class ConsoleDemo {
    public static void main(String[] args) {
        Individual thato = new Individual("C001", "thato", "pass123",
                "Gaborone", "71234567", "Thato", "Mokoena", true);

        SavingsAccount savings = new SavingsAccount("S001", "Main", "C001");
        InvestmentAccount investment = new InvestmentAccount("I001", "Main", "C001", 1000);
        ChequeAccount cheque = new ChequeAccount("Q001", "Main", "C001",
                "Acme Ltd", "Gaborone", "3901234", 8000);

        thato.addAccount(savings);
        thato.addAccount(investment);
        thato.addAccount(cheque);

        savings.deposit(1000);
        cheque.deposit(550);
        System.out.println("Savings withdrawal allowed? " + savings.withdraw(100));
        System.out.println("Cheque withdrawal allowed? " + cheque.withdraw(50));

        new MonthlyProcessor(thato.getAccounts()).runMonthlyProcess();

        for (Account a : thato.getAccounts()) {
            System.out.println(a.getAccountType() + " balance: " + a.getBalance());
            for (Transaction t : a.getTransactions()) {
                System.out.println("   " + t);
            }
        }
    }
}