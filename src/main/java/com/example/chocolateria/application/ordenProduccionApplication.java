package com.example.chocolateria.application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ordenProduccionApplication extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        System.out.println(getClass().getResource("/vistasFinales/vistaOrdenProduccion.fxml"));

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/vistasFinales/vistaOrdenProduccion.fxml")
        );

        Scene scene = new Scene(loader.load());
        stage.setTitle("Orden de produccion");
        stage.setScene(scene);
        stage.show();
    }
}