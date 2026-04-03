package com.example.chocolateria.application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class maquinariaApplication extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        System.out.println(getClass().getResource("/vistasFinales/vistaRegistroMaquinaria.fxml"));
        System.out.println(getClass().getResource("/vistasFinales/vistaRegistroMaquinaria.fxml"));

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/vistasFinales/vistaRegistroMaquinaria.fxml")
        );

        Scene scene = new Scene(loader.load());
        stage.setTitle("Registro de Maquinaria");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}