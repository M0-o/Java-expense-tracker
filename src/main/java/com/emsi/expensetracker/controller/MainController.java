package com.emsi.expensetracker.controller;

import java.net.URL;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import com.emsi.expensetracker.MainApp;
import com.emsi.expensetracker.model.Category;
import com.emsi.expensetracker.model.Expense;
import com.emsi.expensetracker.service.implementation.AuthService;
import com.emsi.expensetracker.service.implementation.CategoryService;
import com.emsi.expensetracker.service.implementation.ExpenseService;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class MainController implements Initializable {

    AuthService authService;
    ExpenseService expenseService;
    CategoryService categoryService;
    MainApp app;

    public MainController(MainApp app, AuthService authService, ExpenseService expenseService, CategoryService categoryService) {
        this.authService = authService;
        this.expenseService = expenseService;
        this.categoryService = categoryService;
        this.app = app;
    }

    @FXML
    private Label userLabel;

    @FXML
    private PieChart expensePieChart;

    @FXML
    private ComboBox<String> dateFilterComboBox;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (authService.getCurrentUser() != null) {
            userLabel.setText("Welcome, " + authService.getCurrentUser().getUsername() + "!");

            // Initialize date filter options
            ObservableList<String> filterOptions = FXCollections.observableArrayList(
                "All Time",
                "Last 7 Days",
                "Last 30 Days",
                "Last 3 Months",
                "Last 6 Months",
                "Last Year"
            );
            dateFilterComboBox.setItems(filterOptions);
            dateFilterComboBox.setValue("All Time");

            // Add listener to update pie chart when filter changes
            dateFilterComboBox.setOnAction(event -> loadExpensePieChart());

            // Disable pie chart animation to prevent fade out on data change
            expensePieChart.setAnimated(false);

            // Defer initial chart loading until scene is fully rendered
            Platform.runLater(this::loadExpensePieChart);
        }
    }

    private LocalDate getFilterStartDate() {
        String filter = dateFilterComboBox.getValue();
        LocalDate today = LocalDate.now();

        return switch (filter) {
            case "Last 7 Days" -> today.minusDays(7);
            case "Last 30 Days" -> today.minusDays(30);
            case "Last 3 Months" -> today.minusMonths(3);
            case "Last 6 Months" -> today.minusMonths(6);
            case "Last Year" -> today.minusYears(1);
            default -> null; // "All Time" - no filter
        };
    }

    private void loadExpensePieChart() {
        // Clear existing data first
        expensePieChart.getData().clear();

        int userId = authService.getCurrentUser().getId();
        List<Expense> expenses = expenseService.getExpensesByUserId(userId);

        // Apply date filter
        LocalDate filterStartDate = getFilterStartDate();
        if (filterStartDate != null) {
            expenses = expenses.stream()
                .filter(expense -> expense.getDate() != null && !expense.getDate().isBefore(filterStartDate))
                .collect(Collectors.toList());
        }

        // Group expenses by category
        Map<Integer, Double> categoryTotals = new HashMap<>();
        for (Expense expense : expenses) {
            int categoryId = expense.getCategoryId();
            categoryTotals.merge(categoryId, expense.getAmount(), Double::sum);
        }

        // Create pie chart data
        for (Map.Entry<Integer, Double> entry : categoryTotals.entrySet()) {
            Category category = categoryService.getCategoryById(entry.getKey());
            String categoryName = category != null ? category.getName() : "Unknown";
            expensePieChart.getData().add(new PieChart.Data(categoryName + " ($" + String.format("%.2f", entry.getValue()) + ")", entry.getValue()));
        }

        if (expensePieChart.getData().isEmpty()) {
            expensePieChart.getData().add(new PieChart.Data("No expenses yet", 1));
        }

        // Update chart title based on filter
        String filterText = dateFilterComboBox.getValue();
        expensePieChart.setTitle("Expenses by Category" + (filterText.equals("All Time") ? "" : " (" + filterText + ")"));
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
