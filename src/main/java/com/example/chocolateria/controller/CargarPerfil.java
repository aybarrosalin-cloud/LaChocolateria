package com.example.chocolateria.controller;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileInputStream;
import java.io.InputStream;

public class CargarPerfil {

    private static final String CARPETA_CLASSPATH = "/com/example/chocolateria/";
    // perfil.png = avatar genérico (sin usuario específico)
    // perfilr.png = foto de Rosalin Aybar (solo se carga si la DB lo indica)
    private static final String FOTO_DEFAULT       = CARPETA_CLASSPATH + "perfil.png";

    public static void aplicar(Label lblUsuario, ImageView imgPerfil) {
        SesionManager sesion = SesionManager.getInstancia();
        lblUsuario.setText(sesion.getUsuario());

        String ruta = sesion.getFotoPerfil();
        if (ruta != null && !ruta.isBlank()) {
            imgPerfil.setImage(cargarImagen(ruta));
        } else {
            imgPerfil.setImage(cargarDefault());
        }
    }

    private static Image cargarImagen(String ruta) {
        // Si no tiene separadores de directorio, es un nombre de archivo del classpath
        if (!ruta.contains("/") && !ruta.contains("\\")) {
            InputStream is = CargarPerfil.class.getResourceAsStream(CARPETA_CLASSPATH + ruta);
            if (is != null) return new Image(is);
        }
        // Ruta absoluta del sistema de archivos
        try (FileInputStream fis = new FileInputStream(ruta)) {
            return new Image(fis);
        } catch (Exception e) {
            return cargarDefault();
        }
    }

    private static Image cargarDefault() {
        return new Image(CargarPerfil.class.getResourceAsStream(FOTO_DEFAULT));
    }
}
