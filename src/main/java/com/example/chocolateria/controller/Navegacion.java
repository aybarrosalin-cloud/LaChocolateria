package com.example.chocolateria.controller;

import javafx.application.Platform;
import javafx.concurrent.Task;
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
        // Guardamos la referencia al Stage antes de salir del hilo FX
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // Cargamos el FXML + initialize() (con sus queries SQL) en un hilo de fondo
        Task<Parent> tarea = new Task<>() {
            @Override
            protected Parent call() throws Exception {
                FXMLLoader loader = new FXMLLoader(Navegacion.class.getResource(fxmlPath));
                return loader.load();
            }
        };

        // Cuando termina, cambiamos la escena en el hilo FX (sin freeze)
        tarea.setOnSucceeded(e -> {
            stage.setScene(new Scene(tarea.getValue(), w, h));
            stage.show();
        });

        tarea.setOnFailed(e -> {
            Throwable ex = tarea.getException();
            ex.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error de navegación");
            alert.setHeaderText(null);
            alert.setContentText("No se pudo abrir la pantalla: " + fxmlPath + "\n" + ex.getMessage());
            alert.showAndWait();
        });

        Thread hilo = new Thread(tarea);
        hilo.setDaemon(true); // se cierra solo cuando se cierra la app
        hilo.start();
    }
}
