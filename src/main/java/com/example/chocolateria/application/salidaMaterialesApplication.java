package com.example.chocolateria.application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class salidaMaterialesApplication extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/vistasFinales/vistaSalidaMateriales.fxml")
        );
        Scene scene = new Scene(loader.load());
        stage.setTitle("Salida de materiales para producción");
        stage.setScene(scene);
        stage.show();
    }
}
