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
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

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
    private TextArea descriptionField;
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
        if (currentUser == null) {
            System.err.println("Error: No user logged in!");
            errorLabel.setText("Error: No user logged in. Please log in again.");
            errorLabel.setVisible(true);
            return;
        }
        List<Category> categories = categoryService.getAvailableCategories(currentUser.getId());
        categoryChoice.getItems().addAll(categories);

        // Restrict DatePicker to only allow current and past dates
        dateField.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.isAfter(LocalDate.now()));
            }
        });
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

        if(date.isAfter(LocalDate.now())) {
            showError("Select a valid past date");
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

        Expense expense = new Expense(description, amount, category.getId(), date, currentUser.getId());
        boolean result = expenseService.createExpense(expense);

        if (result) {
            showSuccess("Expense saved successfully!");
            clearForm();
            // Optional: Navigate back to expense list or main view after a delay
            new Thread(() -> {
                try {
                    Thread.sleep(1500);
                    javafx.application.Platform.runLater(this::navigateToMainView);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        } else {
            showError("Failed to save expense. Please try again.");
        }
    }

    private void clearForm() {
        amountField.clear();
        descriptionField.clear();
        dateField.setValue(null);
        categoryChoice.setValue(null);
        errorLabel.setVisible(false);
    }

    private void navigateToMainView() {
        MainController controller = app.createMainController();
        Scene scene = app.loadScene("/fxml/MainView.fxml", controller);
        Stage stage = (Stage) amountField.getScene().getWindow();
        stage.setScene(scene);
    }

    @FXML
    private void handleBack() {
        navigateToMainView();
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
