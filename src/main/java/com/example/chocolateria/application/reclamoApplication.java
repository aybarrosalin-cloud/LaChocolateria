package com.example.chocolateria.application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class reclamoApplication extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        // Línea de debug (MUY IMPORTANTE)
        System.out.println(getClass().getResource("/vistas/vistaGestionDeReclamos.fxml"));

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/vistas/vistaGestionDeReclamos.fxml")
        );

        Scene scene = new Scene(loader.load());
        stage.setTitle("Gestión de Reclamos");
        stage.setScene(scene);
        stage.show();
    }
}