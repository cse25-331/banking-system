package bank.view;

import bank.controller.LoginController;
import bank.dao.SampleData;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class BankApp extends Application {
    private Stage stage;
    private final LoginController loginController = new LoginController();

    @Override
    public void start(Stage stage) {
        this.stage = stage;
        SampleData.main(new String[0]);   // creates the tables and the 10 sample records (once)
        stage.setTitle("Banking System");
        showLogin();
        stage.show();
    }

    public void showLogin() {
        stage.setScene(new Scene(new LoginView(this, loginController).getRoot(), 420, 380));
    }

    public void showRegister() {
        stage.setScene(new Scene(new RegisterView(this).getRoot(), 420, 540));
    }

    public void showAccounts() {
        if (loginController.getCurrentCustomer() == null) {
            return;
        }
        stage.setScene(new Scene(new AccountView(this, loginController).getRoot(), 820, 680));
    }

    public static void main(String[] args) {
        launch(args);
    }
}