package com.example.chocolateria.controller;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileInputStream;

public class CargarPerfil {

    private static final String FOTO_DEFAULT = "/com/example/chocolateria/perfilr.png";

    public static void aplicar(Label lblUsuario, ImageView imgPerfil) {
        SesionManager sesion = SesionManager.getInstancia();
        lblUsuario.setText(sesion.getUsuario());

        String ruta = sesion.getFotoPerfil();
        if (ruta != null && !ruta.isBlank()) {
            try (FileInputStream fis = new FileInputStream(ruta)) {
                imgPerfil.setImage(new Image(fis));
            } catch (Exception e) {
                imgPerfil.setImage(cargarDefault());
            }
        } else {
            imgPerfil.setImage(cargarDefault());
        }
    }

    private static Image cargarDefault() {
        return new Image(CargarPerfil.class.getResourceAsStream(FOTO_DEFAULT));
    }
}
