package com.example.chocolateria.application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class suplidorApplication extends Application {

        @Override
        public void start(Stage stage) throws Exception {


            System.out.println(getClass().getResource("/vistas/vistaSuplidor.fxml"));

            FXMLLoader loader = new FXMLLoader(

                    getClass().getResource("/vistas/vistaSuplidor.fxml")
            );

            Scene scene = new Scene(loader.load());
            stage.setTitle("Sistema de Suplidores");
            stage.setScene(scene);
            stage.show();
        }
    }