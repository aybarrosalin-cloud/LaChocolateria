module com.example.chocolateria {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;

    opens com.example.chocolateria to javafx.fxml;
    opens com.example.chocolateria.controller to javafx.fxml;

    exports com.example.chocolateria;
    exports com.example.chocolateria.application;
    exports com.example.chocolateria.modelo;
    opens com.example.chocolateria.modelo to javafx.fxml;
    exports com.example.chocolateria.controller;
    opens com.example.chocolateria.application to javafx.fxml;
    exports com.example.chocolateria.baseDeDatos.prueba;
    opens com.example.chocolateria.baseDeDatos.prueba to javafx.fxml;
}