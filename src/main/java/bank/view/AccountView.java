package bank.view;

import bank.controller.AccountController;
import bank.controller.LoginController;
import bank.controller.Result;
import bank.model.Account;
import bank.model.Customer;
import bank.model.Transaction;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

import java.time.YearMonth;
import java.time.format.DateTimeParseException;

public class AccountView {
    private final VBox root = new VBox(12);
    private final AccountController accounts;

    private final ComboBox<Account> accountBox = new ComboBox<>();
    private final Label balanceLabel = new Label("Balance: -");
    private final TextField amountField = new TextField();
    private final TextField monthField = new TextField();
    private final Label message = new Label();
    private final TableView<Transaction> table = new TableView<>();

    private final ChoiceBox<String> typeBox =
            new ChoiceBox<>(FXCollections.observableArrayList("SAVINGS", "INVESTMENT", "CHEQUE"));
    private final TextField depositField = new TextField();
    private final TextField employerField = new TextField();
    private final TextField employerAddressField = new TextField();
    private final TextField employerContactField = new TextField();
    private final TextField salaryField = new TextField();

    public AccountView(BankApp app, LoginController login) {
        Customer customer = login.getCurrentCustomer();
        accounts = new AccountController(customer);

        // Top bar
        Label welcome = new Label("Welcome, " + customer.getDisplayName());
        welcome.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        Button logoutButton = new Button("Log out");
        logoutButton.setOnAction(e -> {
            login.logout();
            app.showLogin();
        });
        HBox topBar = new HBox(10, welcome, spacer, logoutButton);
        topBar.setAlignment(Pos.CENTER_LEFT);

        // Choose an account
        accountBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(Account a) {
                return a == null ? "" : a.getAccountType() + "  " + a.getAccountNumber();
            }

            @Override
            public Account fromString(String text) {
                return null;
            }
        });
        accountBox.setOnAction(e -> showSelected());
        HBox accountRow = new HBox(10, new Label("Account:"), accountBox, balanceLabel);
        accountRow.setAlignment(Pos.CENTER_LEFT);

        // Deposit and withdraw
        amountField.setPromptText("Amount (BWP)");
        Button depositButton = new Button("Deposit");
        depositButton.setOnAction(e -> finish(accounts.deposit(selectedNumber(), amountField.getText())));
        Button withdrawButton = new Button("Withdraw");
        withdrawButton.setOnAction(e -> finish(accounts.withdraw(selectedNumber(), amountField.getText())));
        HBox moneyRow = new HBox(10, amountField, depositButton, withdrawButton);

        // Transaction history by month
        table.getColumns().addAll(column("Date", "date"), column("Type", "type"),
                column("Amount", "amount"), column("Balance after", "balanceAfter"),
                column("Description", "description"));
        table.setPrefHeight(200);
        monthField.setPromptText("Month, e.g. 2026-11 (blank = all)");
        Button showButton = new Button("Show");
        showButton.setOnAction(e -> showMonth());
        HBox monthRow = new HBox(10, new Label("Transaction history"), monthField, showButton);
        monthRow.setAlignment(Pos.CENTER_LEFT);

        // Open a new account
        typeBox.setValue("SAVINGS");
        depositField.setPromptText("Opening deposit (Investment)");
        employerField.setPromptText("Employer name (Cheque)");
        employerAddressField.setPromptText("Employer address");
        employerContactField.setPromptText("Employer contact");
        salaryField.setPromptText("Monthly salary");
        Button openButton = new Button("Open account");
        openButton.setOnAction(e -> finish(accounts.openAccount(typeBox.getValue(),
                depositField.getText(), employerField.getText(), employerAddressField.getText(),
                employerContactField.getText(), salaryField.getText())));
        Label openTitle = new Label("Open a new account");
        openTitle.setStyle("-fx-font-weight: bold;");
        HBox openRow1 = new HBox(10, typeBox, depositField, openButton);
        HBox openRow2 = new HBox(10, employerField, employerAddressField,
                employerContactField, salaryField);

        root.setPadding(new Insets(20));
        root.getChildren().addAll(topBar, accountRow, moneyRow, message, monthRow, table,
                openTitle, openRow1, openRow2);

        refresh(null);
    }

    private String selectedNumber() {
        Account a = accountBox.getValue();
        return a == null ? null : a.getAccountNumber();
    }

    private void refresh(String keepNumber) {
        accountBox.getItems().setAll(accounts.getAccounts());
        Account choose = null;
        for (Account a : accountBox.getItems()) {
            if (a.getAccountNumber().equals(keepNumber)) {
                choose = a;
            }
        }
        if (choose == null && !accountBox.getItems().isEmpty()) {
            choose = accountBox.getItems().get(0);
        }
        accountBox.setValue(choose);
        showSelected();
    }

    private void showSelected() {
        String number = selectedNumber();
        if (number == null) {
            balanceLabel.setText("Balance: -");
            table.getItems().clear();
            return;
        }
        balanceLabel.setText(String.format("Balance: BWP%.2f", accounts.getBalance(number)));
        table.getItems().setAll(accounts.getHistory(number));
    }

    private void showMonth() {
        String number = selectedNumber();
        String text = monthField.getText().trim();
        if (number == null || text.isEmpty()) {
            showSelected();
            return;
        }
        try {
            table.getItems().setAll(accounts.getHistoryForMonth(number, YearMonth.parse(text)));
        } catch (DateTimeParseException e) {
            show("Enter the month as yyyy-MM, for example 2026-11.", false);
        }
    }

    private void finish(Result result) {
        show(result.getMessage(), result.isSuccess());
        refresh(selectedNumber());
    }

    private void show(String text, boolean success) {
        message.setText(text);
        message.setStyle(success ? "-fx-text-fill: green;" : "-fx-text-fill: red;");
    }

    private TableColumn<Transaction, Object> column(String title, String property) {
        TableColumn<Transaction, Object> column = new TableColumn<>(title);
        column.setCellValueFactory(new PropertyValueFactory<>(property));
        column.setPrefWidth(140);
        return column;
    }

    public Parent getRoot() {
        return root;
    }
}