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
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        try {
            FXMLLoader loader = new FXMLLoader(Navegacion.class.getResource(fxmlPath));
            Parent root = loader.load();
            stage.setScene(new Scene(root, w, h));
            stage.show();
        } catch (Exception ex) {
            ex.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error de navegación");
            alert.setHeaderText(null);
            alert.setContentText("No se pudo abrir la pantalla: " + fxmlPath + "\n" + ex.getMessage());
            alert.showAndWait();
        }
    }

    public static void salir(ActionEvent event) {
        SesionManager.getInstancia().cerrarSesion();
        irA("/vistasFinales/vistaPrincipal.fxml", event, 949, 533);
    }
}
