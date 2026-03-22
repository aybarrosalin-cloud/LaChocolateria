package com.example.chocolateria.application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class solicitudProduccionApplication extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        System.out.println(getClass().getResource("/vistas/vistaSolicitudDeProduccion.fxml"));
        System.out.println(getClass().getResource("/vistas/vistaSolicitudDeProduccion.fxml"));

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/vistas/vistaSolicitudDeProduccion.fxml")
        );

        Scene scene = new Scene(loader.load());
        stage.setTitle("Solicitud de Producción");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}