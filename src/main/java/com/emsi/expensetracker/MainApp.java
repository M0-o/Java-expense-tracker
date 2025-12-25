
package com.emsi.expensetracker;

import com.emsi.expensetracker.controller.LoginController;
import com.emsi.expensetracker.util.DatabaseConnection;
import com.emsi.expensetracker.dao.implementation.AuthDAO;
import com.emsi.expensetracker.service.implementation.AuthService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class MainApp extends Application {

    private final DatabaseConnection dbConnection = new DatabaseConnection();
    private final AuthService authService = new AuthService(new AuthDAO(dbConnection));

    @Override
    public void init() {
        dbConnection.initialize();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        showLoginView(primaryStage);
    }

    public void showLoginView(Stage primaryStage){
        LoginController controller = new LoginController(this ,authService);
        Scene scene = loadScene("/fxml/LoginView.fxml", controller);
        primaryStage.setScene(scene);
        primaryStage.show();
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