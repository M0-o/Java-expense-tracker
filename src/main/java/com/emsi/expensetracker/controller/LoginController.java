package com.emsi.expensetracker.controller;

import com.emsi.expensetracker.service.implementation.AuthService;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import com.emsi.expensetracker.MainApp;

public class LoginController {

    private final AuthService authService;
    private final MainApp app;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label errorLabel;

    public LoginController(MainApp app, AuthService authService) {
        this.authService = authService;
        this.app = app;
    }

    @FXML
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showError("Please fill all fields");
            return;
        }

        if (authService.login(username, password)) {
            loadMainView();
        } else {
            showError("Invalid username or password");
        }
    }

    @FXML
    private void handleRegister() {

        RegisterController controller = new RegisterController(app ,authService);
        Scene scene = app.loadScene("/fxml/RegisterView.fxml", controller);
        Stage stage = (Stage) usernameField.getScene().getWindow();
        stage.setScene(scene);

    }

    private void loadMainView() {
        MainController controller = new MainController(app , authService);
        Scene scene = app.loadScene("/fxml/MainView.fxml", controller);
        Stage stage = (Stage) usernameField.getScene().getWindow();
        stage.setScene(scene);
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }
}
