package com.example.javafxfuelconsumption;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.ResourceBundle;

public class FuelApplication extends Application {

    @FXML
    private Label fuelLabel;
    private TextField fuelField;
    @FXML
    private Label distanceLabel;
    private TextField distanceField;
    @FXML
    private Label resultLabel;

    private ResourceBundle bundle;
    private Locale currentLocale;

    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(FuelApplication.class.getResource("fuel.fxml"));
            stage.setTitle("Valtteri Ikonen");

            fuelLabel = new Label();
            fuelField = new TextField();
            distanceLabel = new Label();
            distanceField = new TextField();
            resultLabel = new Label();

            Button calculateButton = new Button("Calculate");
            calculateButton.setOnAction(event -> calculate());

            Button enButton = new Button("English");
            Button frButton = new Button("French");
            Button jaButton = new Button("Japanese");
            Button faButton = new Button("Persian");

            enButton.setOnAction(event -> loadLanguage("en"));
            frButton.setOnAction(event -> loadLanguage("fr"));
            jaButton.setOnAction(event -> loadLanguage("ja"));
            faButton.setOnAction(event -> loadLanguage("fa"));

            VBox layout = new VBox(10, distanceLabel, distanceField, fuelLabel, fuelField, calculateButton, resultLabel, enButton, frButton, jaButton, faButton);
            Scene scene = new Scene(layout, 300, 400);
            stage.setScene(scene);
            stage.show();

            // Initialize default language
            loadLanguage("en");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadLanguage(String language) {
        currentLocale = new Locale(language);
        bundle = ResourceBundle.getBundle("bundle", currentLocale);
        updateLabels();
    }

    private void updateLabels() {
        fuelLabel.setText(bundle.getString("fuel"));
        distanceLabel.setText(bundle.getString("distance"));
        resultLabel.setText(bundle.getString("result"));
        resultLabel.setText("");
    }

    private void calculate() {
        try {
            double fuel = Double.parseDouble(fuelField.getText());
            double distance = Double.parseDouble(distanceField.getText());

            if (distance == 0) {
                resultLabel.setText(bundle.getString("error"));
                return;
            }

            // Fuel consumption calculation (liters per 100 kilometers)
            double consumptionPer100Km = (fuel / distance) * 100;

            // Fuel cost calculation (assuming cost per liter is 1.67)
            double fuelCost = fuel * 1.67;

            // Format the result according to the locale
            NumberFormat numberFormat = NumberFormat.getNumberInstance(currentLocale);
            NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(currentLocale);

            String consumptionText = numberFormat.format(consumptionPer100Km) + " L/100km";
            String costText = currencyFormat.format(fuelCost);

            resultLabel.setText(bundle.getString("result") + ": " + consumptionText + " (" + costText + ")");
        } catch (NumberFormatException e) {
            resultLabel.setText(bundle.getString("error"));
        }
    }

    public static void main(String[] args) {
        launch(FuelApplication.class);
    }
}
