package bank.view;

import bank.controller.LoginController;
import bank.controller.Result;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class LoginView {
    private final VBox root = new VBox(12);

    public LoginView(BankApp app, LoginController controller) {
        Label title = new Label("Banking System");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        Label message = new Label();

        Button loginButton = new Button("Log in");
        loginButton.setDefaultButton(true);
        loginButton.setOnAction(e -> {
            Result result = controller.login(usernameField.getText(), passwordField.getText());
            if (result.isSuccess()) {
                app.showAccounts();
            } else {
                message.setText(result.getMessage());
                message.setStyle("-fx-text-fill: red;");
            }
        });

        Hyperlink registerLink = new Hyperlink("New customer? Register");
        registerLink.setOnAction(e -> app.showRegister());

        root.setPadding(new Insets(30));
        root.setAlignment(Pos.CENTER);
        root.getChildren().addAll(title, usernameField, passwordField, loginButton, message, registerLink);
    }

    public Parent getRoot() {
        return root;
    }
}