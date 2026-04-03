package com.example.chocolateria.application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class envioApplication extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        System.out.println(getClass().getResource("/vistasFinales/vistaGestionEnvios.fxml"));
        System.out.println(getClass().getResource("/vistasFinales/vistaGestionEnvios.fxml"));

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/vistasFinales/vistaGestionEnvios.fxml")
        );

        Scene scene = new Scene(loader.load());
        stage.setTitle("Registro de Suplidor");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}