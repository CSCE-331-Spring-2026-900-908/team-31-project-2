module com.example.team31project2 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;

    opens com.example.team31project2 to javafx.fxml;
    exports com.example.team31project2;

    opens com.example.team31project2.controller to javafx.fxml;
    exports com.example.team31project2.controller;

    
}