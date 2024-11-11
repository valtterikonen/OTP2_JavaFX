module com.example.javafxhomeassignment0511 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.javafxhomeassignment0511 to javafx.fxml;
    exports com.example.javafxhomeassignment0511;
}