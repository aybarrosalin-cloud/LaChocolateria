package com.example.chocolateria.application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class facturacionApplication extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        // Línea de debug (MUY IMPORTANTE)
        System.out.println(getClass().getResource("/vistas/vistaFacturacion.fxml"));

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/vistas/vistaFacturacion.fxml")
        );

        Scene scene = new Scene(loader.load());
        stage.setTitle("Facturacion");
        stage.setScene(scene);
        stage.show();
    }
}