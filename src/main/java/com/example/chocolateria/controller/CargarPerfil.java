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
        try {
            SesionManager sesion = SesionManager.getInstancia();
            if (lblUsuario != null) lblUsuario.setText(sesion.getUsuario());
            if (imgPerfil == null) return;

            String ruta = sesion.getFotoPerfil();
            Image img = (ruta != null && !ruta.isBlank()) ? cargarImagen(ruta) : cargarDefault();
            if (img != null) imgPerfil.setImage(img);
        } catch (Exception ignored) {
            // En Scene Builder no hay sesion activa; se ignora para no romper la vista
        }
    }

    private static Image cargarImagen(String ruta) {
        if (!ruta.contains("/") && !ruta.contains("\\")) {
            InputStream is = CargarPerfil.class.getResourceAsStream(CARPETA_CLASSPATH + ruta);
            if (is != null) return new Image(is);
        }
        try (FileInputStream fis = new FileInputStream(ruta)) {
            return new Image(fis);
        } catch (Exception e) {
            return cargarDefault();
        }
    }

    private static Image cargarDefault() {
        InputStream is = CargarPerfil.class.getResourceAsStream(FOTO_DEFAULT);
        return is != null ? new Image(is) : null; // null si no existe (p.ej. en Scene Builder)
    }
}
