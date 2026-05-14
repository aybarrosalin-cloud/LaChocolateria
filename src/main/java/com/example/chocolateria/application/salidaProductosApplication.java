package com.example.chocolateria.application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class salidaProductosApplication extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/vistasFinales/vistaSalidaProductos.fxml")
        );
        Scene scene = new Scene(loader.load());
        stage.setTitle("Salida de productos por ventas");
        stage.setScene(scene);
        stage.show();
    }
}
