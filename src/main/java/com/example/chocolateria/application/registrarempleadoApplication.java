
package com.example.chocolateria.application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class registrarempleadoApplication extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        System.out.println(getClass().getResource("/vistas/vistaRegistroEmpleado.fxml"));
        // Linea de debug (MUY IMPORTANTE)
        System.out.println(getClass().getResource("vistas/vistaRegistroEmpleado.fxml"));

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/vistas/vistaRegistroEmpleado.fxml")
        );
        System.out.println(getClass().getResource("/vistas/vistaRegistroEmpleado.fxml"));
        Scene scene = new Scene(loader.load());
        stage.setTitle("Empleados");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}