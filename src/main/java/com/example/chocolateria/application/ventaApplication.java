package com.example.chocolateria.application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ventaApplication extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        System.out.println(getClass().getResource("/vistas/vistaVenta.fxml"));

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/vistas/vistaVenta.fxml")
        );

        Scene scene = new Scene(loader.load());
        stage.setTitle("Venta");
        stage.setScene(scene);
        stage.show();
    }
}