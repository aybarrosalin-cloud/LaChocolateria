package com.example.chocolateria.application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class pagoVentaApplication extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        System.out.println(getClass().getResource("/vistasFinales/vistaPagoVenta.fxml"));

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/vistasFinales/vistaPagoVenta.fxml")
        );

        Scene scene = new Scene(loader.load());
        stage.setTitle("Recepcion de productos");
        stage.setScene(scene);
        stage.show();
    }
}