package com.example.chocolateria;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class style {

    public static ImageView crearBanner(String rutaImagen, double ancho, double alto) {
        try {
            ImageView imageView = new ImageView(new Image(rutaImagen));
            imageView.setFitWidth(ancho);
            imageView.setFitHeight(alto);
            imageView.setPreserveRatio(false);
            return imageView;
        } catch (Exception e) {
            System.err.println("Error al cargar banner: " + e.getMessage());
            return new ImageView();
        }
    }
}