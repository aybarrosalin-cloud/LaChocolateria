package com.example.chocolateria.application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ordenClienteApplication extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        System.out.println(getClass().getResource("/vistas/vistaOrdenCliente.fxml"));

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/vistas/vistaOrdenCliente.fxml")
        );

        Scene scene = new Scene(loader.load());
        stage.setTitle("Orden de Cliente");
        stage.setScene(scene);
        stage.show();
    }
}