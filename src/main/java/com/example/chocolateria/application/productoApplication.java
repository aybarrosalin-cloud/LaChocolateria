package com.example.chocolateria.application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class productoApplication extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        // Línea de debug para verificar que el FXML se carga correctamente
        System.out.println(getClass().getResource("/vistas/vistaRegistroDeProductos.fxml"));

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/vistas/vistaRegistroDeProductos.fxml")
        );

        Scene scene = new Scene(loader.load());
        stage.setTitle("Registro de Productos");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}