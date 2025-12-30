package com.emsi.expensetracker.controller;

import java.time.LocalDate;
import java.util.Locale;

import com.emsi.expensetracker.service.implementation.AuthService;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import com.emsi.expensetracker.MainApp;
import com.emsi.expensetracker.service.implementation.ExpenseService;
import com.emsi.expensetracker.model.Expense;
import com.emsi.expensetracker.model.Category;

import java.util.List;

import com.emsi.expensetracker.service.implementation.CategoryService;
import com.emsi.expensetracker.model.User;

public class SaveExpenseController {

    private final AuthService authService;
    private final ExpenseService expenseService;
    private final CategoryService categoryService;
    private final MainApp app;

    @FXML
    private ChoiceBox<Category> categoryChoice;
    @FXML
    private TextField amountField;
    @FXML
    private DatePicker dateField;
    @FXML
    private TextField descriptionField;
    @FXML
    private Label errorLabel;
    @FXML
    private Button confirmationButton;

    public SaveExpenseController(MainApp app, AuthService authService, ExpenseService expenseService, CategoryService categoryService) {
        this.authService = authService;
        this.expenseService = expenseService;
        this.app = app;
        this.categoryService = categoryService;
    }

    @FXML
    void initialize() {
        User currentUser = authService.getCurrentUser();
        List<Category> categories = categoryService.getAvailableCategories(currentUser.getId());
        categoryChoice.getItems().addAll(categories);
    }

    @FXML
    private void handleSaveExpense() {
        Category category = categoryChoice.getValue();
        String amountText = amountField.getText().trim();
        String description = descriptionField.getText().trim();
        LocalDate date = dateField.getValue();

        if (amountText.isEmpty()) {
            showError("amount are required");
            return;
        }
        double amount;
        try {
            amount = Double.parseDouble(amountText);
        } catch (NumberFormatException e) {
            showError("Invalid amount format");
            return;
        }

        Expense expense = new Expense(description, amount, category.getId(), date, authService.getCurrentUser().getId());
        boolean result = expenseService.createExpense(expense);

    }

    private void showError(String message) {
        errorLabel.setStyle("-fx-text-fill: red;");
        errorLabel.setText(message);
    }

    private void showSuccess(String message) {
        errorLabel.setStyle("-fx-text-fill: green;");
        errorLabel.setText(message);
    }
}
