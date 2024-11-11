module com.example.javafxfuelconsumption {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.javafxfuelconsumption to javafx.fxml;
    exports com.example.javafxfuelconsumption;
}