package com.example.chocolateria.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class Navegacion {

    public static void irA(String fxmlPath, ActionEvent event) {
        irA(fxmlPath, event, 1020, 720);
    }

    public static void irA(String fxmlPath, ActionEvent event, double w, double h) {
        try {
            FXMLLoader loader = new FXMLLoader(Navegacion.class.getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root, w, h));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error de navegación");
            alert.setHeaderText(null);
            alert.setContentText("No se pudo abrir la pantalla: " + fxmlPath + "\n" + e.getMessage());
            alert.showAndWait();
        }
    }
}
