package bank;

import bank.controller.*;

public class ControllerDemo {
    public static void main(String[] args) {
        LoginController login = new LoginController();
        System.out.println(login.login("thato", "wrong").getMessage());
        System.out.println(login.login("thato", "pass123").getMessage());

        AccountController accounts = new AccountController(login.getCurrentCustomer());
        System.out.println(accounts.deposit("S001", "250").getMessage());
        System.out.println(accounts.withdraw("S001", "100").getMessage());
        System.out.println(accounts.withdraw("Q001", "50").getMessage());
        System.out.println(accounts.deposit("Q001", "abc").getMessage());
    }
}