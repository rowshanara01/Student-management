package com.example.studentmanage;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Loading FXML file path
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main_view.fxml"));
            BorderPane root = loader.load();

            // Scene setup
            Scene scene = new Scene(root, 800, 600);
            primaryStage.setTitle("JavaFX Student CRUD Application");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch(Exception e) {
            System.err.println("Error loading FXML:");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}