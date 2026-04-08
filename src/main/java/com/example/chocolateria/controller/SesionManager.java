package com.example.chocolateria.controller;

public class SesionManager {

    private static SesionManager instancia;

    private String usuario;
    private String fotoPerfil;

    private SesionManager() {}

    public static SesionManager getInstancia() {
        if (instancia == null) {
            instancia = new SesionManager();
        }
        return instancia;
    }

    public void iniciarSesion(String usuario, String fotoPerfil) {
        this.usuario = usuario;
        this.fotoPerfil = fotoPerfil;
    }

    public String getUsuario() {
        return usuario != null ? usuario : "";
    }

    public String getFotoPerfil() {
        return fotoPerfil;
    }

    public void cerrarSesion() {
        this.usuario = null;
        this.fotoPerfil = null;
    }
}
