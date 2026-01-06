package com.emsi.expensetracker.controller;

import java.time.LocalDate;
import java.util.List;

import com.emsi.expensetracker.MainApp;
import com.emsi.expensetracker.model.Category;
import com.emsi.expensetracker.model.Expense;
import com.emsi.expensetracker.model.User;
import com.emsi.expensetracker.service.implementation.AuthService;
import com.emsi.expensetracker.service.implementation.CategoryService;
import com.emsi.expensetracker.service.implementation.ExpenseService;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ModifyExpenseController {

    private final AuthService authService;
    private final ExpenseService expenseService;
    private final CategoryService categoryService;
    private final MainApp app;
    private final Expense expense;

    @FXML
    private ChoiceBox<Category> categoryChoice;
    @FXML
    private TextField amountField;
    @FXML
    private DatePicker dateField;
    @FXML
    private TextArea descriptionField;
    @FXML
    private Label errorLabel;
    @FXML
    private Button confirmationButton;

    public ModifyExpenseController(MainApp app, AuthService authService, ExpenseService expenseService, CategoryService categoryService, Expense expense) {
        this.authService = authService;
        this.expenseService = expenseService;
        this.app = app;
        this.categoryService = categoryService;
        this.expense = expense;
    }

    @FXML
    void initialize() {
        User currentUser = authService.getCurrentUser();
        if (currentUser == null) {
            System.err.println("Error: No user logged in!");
            errorLabel.setText("Error: No user logged in. Please log in again.");
            errorLabel.setVisible(true);
            return;
        }
        List<Category> categories = categoryService.getAvailableCategories(currentUser.getId());
        categoryChoice.getItems().addAll(categories);
        categoryChoice.setValue(categoryService.getCategoryById(expense.getCategoryId()));
        amountField.setText(String.valueOf(expense.getAmount()));
        descriptionField.setText(expense.getDescription());
        dateField.setValue(expense.getDate());
    }

    @FXML
    private void handleSaveExpense() {
        Category category = categoryChoice.getValue();
        String amountText = amountField.getText().trim();
        String description = descriptionField.getText().trim();
        LocalDate date = dateField.getValue();

        // Validation
        if (category == null) {
            showError("Please select a category");
            return;
        }

        if (amountText.isEmpty()) {
            showError("Amount is required");
            return;
        }

        if (date == null) {
            showError("Please select a date");
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(amountText);
            if (amount <= 0) {
                showError("Amount must be greater than zero");
                return;
            }
        } catch (NumberFormatException e) {
            showError("Invalid amount format");
            return;
        }

        User currentUser = authService.getCurrentUser();
        if (currentUser == null) {
            showError("Session expired. Please log in again.");
            return;
        }

        this.expense.setDescription(description);
        this.expense.setAmount(amount);
        this.expense.setCategoryId(category.getId());
        this.expense.setDate(date);

        boolean result = expenseService.updateExpense(this.expense);

        if (result) {
            showSuccess("Expense updated successfully!");
            // Navigate back after a short delay so user can see the success message
            javafx.animation.PauseTransition pause = new javafx.animation.PauseTransition(javafx.util.Duration.seconds(1.5));
            pause.setOnFinished(event -> navigateToExpenseListView());
            pause.play();
        } else {
            showError("Failed to save expense. Please try again.");
        }
    }

    private void navigateToExpenseListView() {
        ExpenseListActionController controller = app.createExpenseListController();
        Scene scene = app.loadScene("/fxml/ExpenseListView.fxml", controller);
        Stage stage = (Stage) amountField.getScene().getWindow();
        stage.setScene(scene);
    }

    @FXML
    private void handleBack() {
        navigateToExpenseListView();
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
