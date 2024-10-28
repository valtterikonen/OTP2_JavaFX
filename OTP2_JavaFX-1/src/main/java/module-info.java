module com.example.otp2_javafx1 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.otp2_javafx1 to javafx.fxml;
    exports com.example.otp2_javafx1;
}