package bank.view;

import bank.controller.CustomerController;
import bank.controller.Result;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class RegisterView {
    private final VBox root = new VBox(10);

    public RegisterView(BankApp app) {
        CustomerController controller = new CustomerController();

        Label title = new Label("Register as a customer");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        TextField usernameField = field("Username");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        TextField firstNameField = field("First name");
        TextField surnameField = field("Surname");
        TextField addressField = field("Address");
        TextField contactField = field("Contact details");
        CheckBox employedBox = new CheckBox("I am employed");
        Label message = new Label();

        Button registerButton = new Button("Register");
        registerButton.setOnAction(e -> {
            Result result = controller.registerIndividual(usernameField.getText(),
                    passwordField.getText(), firstNameField.getText(), surnameField.getText(),
                    addressField.getText(), contactField.getText(), employedBox.isSelected());
            message.setText(result.getMessage());
            message.setStyle(result.isSuccess() ? "-fx-text-fill: green;" : "-fx-text-fill: red;");
        });

        Hyperlink backLink = new Hyperlink("Back to log in");
        backLink.setOnAction(e -> app.showLogin());

        root.setPadding(new Insets(30));
        root.setAlignment(Pos.CENTER);
        root.getChildren().addAll(title, usernameField, passwordField, firstNameField,
                surnameField, addressField, contactField, employedBox, registerButton,
                message, backLink);
    }

    private TextField field(String prompt) {
        TextField field = new TextField();
        field.setPromptText(prompt);
        return field;
    }

    public Parent getRoot() {
        return root;
    }
}