package com.emsi.expensetracker.controller;


import com.emsi.expensetracker.model.Expense;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TableCell;


public class ExpenseListActionController {

    @FXML
    private TableView<Expense> expenseTable;
    @FXML
    private TableColumn<Expense, String> expenseColumn;
    @FXML
    private TableColumn<Expense, Double> amountColumn;
    @FXML
    private TableColumn<Expense, String> categoryColumn;
    @FXML
    private TableColumn<Expense, String> dateColumn;
    @FXML
    private TableColumn<Expense, Void> actionsColumn;


    @FXML
    public void initialize() {
        expenseColumn.setCellValueFactory(new PropertyValueFactory<>("expense"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));

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
    }

    private void handleEdit(Expense expense) {
        // Open edit dialog and update expense
    }

    private void handleDelete(Expense expense) {
        // Delete expense from database and refresh table
    }

    private void handleView(Expense expense) {
        // Show expense details in a new dialog
    }
}