package com.example.chocolateria.application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class mantenimientoMaquinariaApplication extends Application {

    @Override
    public void start(Stage stage) throws Exception {


        System.out.println(getClass().getResource("/vistas/vistaMantenimientoMaquinaria.fxml"));

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/vistas/vistaMantenimientoMaquinaria.fxml")
        );

        Scene scene = new Scene(loader.load());
        stage.setTitle("Mantenimiento de maquinarias");
        stage.setScene(scene);
        stage.show();
    }
}