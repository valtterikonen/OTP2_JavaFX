package com.example.javafxhomeassignment0511;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Locale;
import java.util.ResourceBundle;

public class LanguageSelectionController {

    @FXML
    private ComboBox<String> languageComboBox;

    @FXML
    private Label labelFirstName;

    @FXML
    private Label labelLastName;

    @FXML
    private Label labelEmail;

    @FXML
    private Button saveButton;

    private ResourceBundle bundle;

    @FXML
    public void initialize() {
        ObservableList<String> languages = FXCollections.observableArrayList("English", "Farsi", "Japanese");
        languageComboBox.setItems(languages);

        // Default language
        loadLanguage("en");

        languageComboBox.setOnAction(event -> {
            String selectedLanguage = languageComboBox.getValue();
            switch (selectedLanguage) {
                case "English" -> loadLanguage("en");
                case "Farsi" -> loadLanguage("fa");
                case "Japanese" -> loadLanguage("ja");
            }
        });
    }

    private void loadLanguage(String languageCode) {
        bundle = ResourceBundle.getBundle("com/example/javafxhomeassignment0511/lang/lang", new Locale(languageCode));
        labelFirstName.setText(bundle.getString("label.firstName"));
        labelLastName.setText(bundle.getString("label.lastName"));
        labelEmail.setText(bundle.getString("label.email"));
        saveButton.setText(bundle.getString("button.save"));
    }
}
