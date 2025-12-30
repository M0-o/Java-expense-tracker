package com.emsi.expensetracker.controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.emsi.expensetracker.MainApp;
import com.emsi.expensetracker.service.implementation.AuthService;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class MainController implements Initializable {

    AuthService authService;
    MainApp app;

    public MainController(MainApp app, AuthService authService) {
        this.authService = authService;
        this.app = app;
    }

    @FXML
    private Label userLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (authService.getCurrentUser() != null) {
            userLabel.setText("Welcome, " + authService.getCurrentUser().getUsername() + "!");
        }
    }

    @FXML
    private void handleViewExpenses() {
        ExpenseListActionController controller = app.createExpenseListController();
        Scene scene = app.loadScene("/fxml/ExpenseListView.fxml", controller);
        Stage stage = (Stage) userLabel.getScene().getWindow();
        stage.setScene(scene);
    }

    @FXML
    private void handleAddExpense() {
        SaveExpenseController controller = app.createSaveExpenseController();
        Scene scene = app.loadScene("/fxml/ExpenseFormView.fxml", controller);
        Stage stage = (Stage) userLabel.getScene().getWindow();
        stage.setScene(scene);
    }



    @FXML
    private void handleViewCategories() {
        CategoryController controller = app.createCategoryController();
        Scene scene = app.loadScene("/fxml/CategoryFormView.fxml", controller);
        Stage stage = (Stage) userLabel.getScene().getWindow();
        stage.setScene(scene);
    }

    @FXML
    private void handleLogout() {
        LoginController controller = new LoginController(app, authService);
        Scene scene = app.loadScene("/fxml/LoginView.fxml", controller);
        Stage stage = (Stage) userLabel.getScene().getWindow();
        stage.setScene(scene);
        authService.logout();
    }

}
