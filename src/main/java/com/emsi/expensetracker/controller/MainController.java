package com.emsi.expensetracker.controller;

import com.emsi.expensetracker.service.AuthService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML private Label userLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (AuthService.getCurrentUser() != null) {
            userLabel.setText("Welcome, " + AuthService.getCurrentUser().getUsername() + "!");
        }
    }

    @FXML
    private void handleLogout() {
        AuthService.logout();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/LoginView.fxml"));
            Stage stage = (Stage) userLabel.getScene().getWindow();
            stage.setScene(new Scene(root, 600, 400));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
