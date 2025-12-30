package com.emsi.expensetracker;

import com.emsi.expensetracker.controller.CategoryController;
import com.emsi.expensetracker.controller.LoginController;
import com.emsi.expensetracker.controller.SaveExpenseController;
import com.emsi.expensetracker.controller.ExpenseListActionController;
import com.emsi.expensetracker.util.DatabaseConnection;
import com.emsi.expensetracker.dao.implementation.AuthDAO;
import com.emsi.expensetracker.dao.implementation.ExpenseDAO;
import com.emsi.expensetracker.dao.implementation.CategoryDAO;
import com.emsi.expensetracker.service.implementation.AuthService;
import com.emsi.expensetracker.service.implementation.ExpenseService;
import com.emsi.expensetracker.service.implementation.CategoryService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class MainApp extends Application {

    private final DatabaseConnection dbConnection = new DatabaseConnection();
    private final AuthDAO authDAO = new AuthDAO(dbConnection);
    private final ExpenseDAO expenseDAO = new ExpenseDAO(dbConnection);
    private final CategoryDAO categoryDAO = new CategoryDAO(dbConnection);

    private final AuthService authService = new AuthService(authDAO);
    private final ExpenseService expenseService = new ExpenseService(expenseDAO);
    private final CategoryService categoryService = new CategoryService(categoryDAO);

    @Override
    public void init() {
        dbConnection.initialize();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        showLoginView(primaryStage);
    }

    public void showLoginView(Stage primaryStage) {
        LoginController controller = new LoginController(this, authService);
        Scene scene = loadScene("/fxml/LoginView.fxml", controller);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Expense Tracker - Login");
        primaryStage.show();
    }

    public SaveExpenseController createSaveExpenseController() {
        return new SaveExpenseController(this, authService, expenseService, categoryService);
    }

    public ExpenseListActionController createExpenseListController() {
        return new ExpenseListActionController(this, authService, expenseService, categoryService);
    }

    public CategoryController createCategoryController() {
        return new CategoryController(this, categoryService, authService);
    }



    @Override
    public void stop() {
        dbConnection.shutdown();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public Scene loadScene(String fxmlFile, Object controller) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            loader.setController(controller);
            Parent root = loader.load();
            return new Scene(root);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
