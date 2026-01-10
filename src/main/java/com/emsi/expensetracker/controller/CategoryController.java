package com.emsi.expensetracker.controller;

import java.util.List;

import com.emsi.expensetracker.MainApp;
import com.emsi.expensetracker.model.Category;
import com.emsi.expensetracker.model.User;
import com.emsi.expensetracker.service.implementation.CategoryService;
import com.emsi.expensetracker.service.implementation.AuthService;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.control.ListView;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class CategoryController {

    private final MainApp app;
    private final CategoryService categoryService;
    private final AuthService authService;

    @FXML
    private TextField categoryNameField;
    @FXML
    private TextArea categoryDescriptionField;
    @FXML
    private ListView<Category> categoryListView;
    @FXML
    private Label errorLabel;

    public CategoryController(MainApp app, CategoryService categoryService, AuthService authService) {
        this.app = app;
        this.categoryService = categoryService;
        this.authService = authService;
    }

    @FXML
    void initialize() {
        User currentUser = authService.getCurrentUser();
        if (currentUser != null) {
            List<Category> categories = categoryService.getAvailableCategories(currentUser.getId());
            categoryListView.getItems().setAll(categories);
        } else {
            errorLabel.setText("No user logged in.");
            errorLabel.setVisible(true);
        }
    }

    @FXML
    private void handleAddCategory() {
        String name = categoryNameField.getText().trim();
        String description = categoryDescriptionField.getText().trim();
        User currentUser = authService.getCurrentUser();

        if (name.isEmpty()) {
            showError("Category name is required.");
            return;
        }
        if (currentUser == null) {
            showError("No user logged in.");
            return;
        }

        Category category = new Category(name, description, currentUser.getId());
        boolean result = categoryService.createCategory(name, description, currentUser.getId());

        if (result) {
            showSuccess("Category added.");
            categoryListView.getItems().add(category);
            categoryNameField.clear();
            categoryDescriptionField.clear();
        } else {
            showError("Failed to add category.");
        }
    }

    @FXML
    private void handleDeleteCategory() {
        User currentUser = authService.getCurrentUser();
        Category selected = categoryListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Select a category to delete.");
            return;
        }
        boolean result = categoryService.deleteCategory(selected.getId(), currentUser.getId());
        if (result) {
            showSuccess("Category deleted.");
            categoryListView.getItems().remove(selected);
        } else {
            showError("Failed to delete category.");
        }
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

    @FXML
    private void handleBack() {
        navigateToMainView();
    }

    private void navigateToMainView() {
        MainController controller = app.createMainController();
        Scene scene = app.loadScene("/fxml/MainView.fxml", controller);
        Stage stage = (Stage) categoryNameField.getScene().getWindow();
        stage.setScene(scene);
    }
}
