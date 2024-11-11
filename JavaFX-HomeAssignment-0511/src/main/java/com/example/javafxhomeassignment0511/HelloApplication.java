package com.example.javafxhomeassignment0511;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class HelloApplication extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/javafxhomeassignment0511/LanguageSelectionUI.fxml"));
        Parent root = loader.load();
        primaryStage.setScene(new Scene(root));
        primaryStage.setTitle("Language Selection Example");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
