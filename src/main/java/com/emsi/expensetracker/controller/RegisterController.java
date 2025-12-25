package com.emsi.expensetracker.controller;

import com.emsi.expensetracker.service.implementation.AuthService;
import javafx.fxml.FXML;

import javafx.scene.control.*;
import javafx.stage.Stage;
import com.emsi.expensetracker.MainApp;

public class RegisterController {
    AuthService authService ;
    MainApp app ;

    @FXML private TextField usernameField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private Label errorLabel;

    public RegisterController(MainApp app ,AuthService authService) {
        this.authService = authService;
        this.app = app ;
    }

    @FXML
    private void handleRegister() {
        String username = usernameField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showError("Username and password are required");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showError("Passwords do not match");
            return;
        }

        if (password.length() < 6) {
            showError("Password must be at least 6 characters");
            return;
        }

        if (authService.register(username, password, email)) {
            showSuccess("Account created! Redirecting to login...");
            new Thread(() -> {
                try {
                    Thread.sleep(1500);
                    javafx.application.Platform.runLater(this::handleBackToLogin);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        } else {
            showError("Username already exists");
        }
    }

    @FXML
    private void handleBackToLogin() {
        Stage stage = (Stage) usernameField.getScene().getWindow();
        app.showLoginView(stage);
    }

    private void showError(String message) {
        errorLabel.setStyle("-fx-text-fill: red;");
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }

    private void showSuccess(String message) {
        errorLabel.setStyle("-fx-text-fill: green;");
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }
}
