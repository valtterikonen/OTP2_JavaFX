package com.example.otp2_javafx1;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.util.Locale;
import java.util.ResourceBundle;

public class HelloController {

    // Injected Label for displaying welcome text
    @FXML
    private Label welcomeText;

    // Injected Label for displaying additional messages
    @FXML
    private Label lbl;

    // ResourceBundle for loading language-specific messages
    private ResourceBundle bundle;

    // Locale for specifying language and country
    private Locale locale;

    // Method to handle button click event for "Hello" button
    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }

    // Method to load language-specific messages based on language and country
    @FXML
    private void loadLanguage(String lang, String country) {
        locale = new Locale(lang, country);
        bundle = ResourceBundle.getBundle("message", locale);
        lbl.setText(bundle.getString("greeting"));
    }

    // Method to handle button click event for English language button
    @FXML
    public void onbtnENClick(ActionEvent actionEvent) {
        loadLanguage("en", "UK");
    }

    // Method to handle button click event for French language button
    @FXML
    public void onbtnFRClick(ActionEvent actionEvent) {
        loadLanguage("fr", "FR");
    }

    // Method to handle button click event for Japanese language button
    @FXML
    public void onbtnJPClick(ActionEvent actionEvent) {
        loadLanguage("ja", "JP");
    }

    // Method to handle button click event for Persian language button
    @FXML
    public void onbtnIRClick(ActionEvent actionEvent) {
        loadLanguage("fa", "IR");
    }
}

