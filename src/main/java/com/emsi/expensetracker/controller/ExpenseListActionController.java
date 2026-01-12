package com.emsi.expensetracker.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.emsi.expensetracker.MainApp;
import com.emsi.expensetracker.model.Category;
import com.emsi.expensetracker.model.Expense;
import com.emsi.expensetracker.service.implementation.AuthService;
import com.emsi.expensetracker.service.implementation.CategoryService;
import com.emsi.expensetracker.service.implementation.ExpenseService;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class ExpenseListActionController {

    private final MainApp app;
    private final AuthService authService;
    private final ExpenseService expenseService;
    private final CategoryService categoryService;

    @FXML
    private TableView<Expense> expenseTable;
    @FXML
    private TableColumn<Expense, String> expenseColumn;
    @FXML
    private TableColumn<Expense, Double> amountColumn;
    @FXML
    private TableColumn<Expense, String> categoryColumn;
    @FXML
    private TableColumn<Expense, LocalDate> dateColumn;
    @FXML
    private TableColumn<Expense, Void> actionsColumn;

    public ExpenseListActionController(MainApp app, AuthService authService, ExpenseService expenseService, CategoryService categoryService) {
        this.app = app;
        this.authService = authService;
        this.expenseService = expenseService;
        this.categoryService = categoryService;
    }

    @FXML
    public void initialize() {
        // Set up column bindings
        expenseColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        categoryColumn.setCellValueFactory(cellData -> {
            Expense expense = cellData.getValue();
            Category category = categoryService.getCategoryById(expense.getCategoryId());
            String categoryName = category != null ? category.getName() : "Unknown";
            return new javafx.beans.property.SimpleStringProperty(categoryName);
        });

        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));

        // Set up action buttons column
        actionsColumn.setCellFactory(param -> new TableCell<>() {
            private final Button editButton = new Button("Edit");
            private final Button deleteButton = new Button("Delete");
            private final Button viewButton = new Button("View");

            {
                editButton.setOnAction(event -> handleEdit(getTableRow().getItem()));
                deleteButton.setOnAction(event -> handleDelete(getTableRow().getItem()));
                viewButton.setOnAction(event -> handleView(getTableRow().getItem()));
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox buttons = new HBox(5, viewButton, editButton, deleteButton);
                    setGraphic(buttons);
                }
            }
        });

        // Load expense data
        loadExpenses();
    }

    private void loadExpenses() {
        if (authService.getCurrentUser() != null) {
            List<Expense> expenses = expenseService.getExpensesByUserId(authService.getCurrentUser().getId());
            expenseTable.setItems(FXCollections.observableArrayList(expenses));
        }
    }

    private void handleEdit(Expense expense) {
        if (expense == null) {
            return;
        }

        ModifyExpenseController controller = new ModifyExpenseController(app, authService, expenseService, categoryService, expense);
        Scene scene = app.loadScene("/fxml/ExpenseFormView.fxml", controller);
        Stage stage = (Stage) expenseTable.getScene().getWindow();
        stage.setScene(scene);

    }

    private void handleDelete(Expense expense) {
        if (expense == null) {
            return;
        }

        // Confirm deletion
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Expense");
        alert.setHeaderText("Delete this expense?");
        alert.setContentText("Are you sure you want to delete this expense? This action cannot be undone.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            boolean deleted = expenseService.deleteExpense(expense.getId());
            if (deleted) {
                loadExpenses(); // Refresh table
                showInfo("Expense deleted successfully!");
            } else {
                showError("Failed to delete expense.");
            }
        }
    }

    private void handleView(Expense expense) {
        if (expense == null) {
            return;
        }

        // Get category name
        Category category = categoryService.getCategoryById(expense.getCategoryId());
        String categoryName = category != null ? category.getName() : "Unknown";

        // Show expense details
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Expense Details");
        alert.setHeaderText("Expense Information");
        alert.setContentText(
                "Description: " + expense.getDescription() + "\n"
                + "Amount: $" + String.format("%.2f", expense.getAmount()) + "\n"
                + "Category: " + categoryName + "\n"
                + "Date: " + expense.getDate()
        );
        alert.showAndWait();
    }

    @FXML
    private void handleBack() {
        MainController controller = app.createMainController();
        Scene scene = app.loadScene("/fxml/MainView.fxml", controller);
        Stage stage = (Stage) expenseTable.getScene().getWindow();
        stage.setScene(scene);
    }

    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
