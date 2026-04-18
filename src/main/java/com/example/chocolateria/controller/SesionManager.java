package com.example.chocolateria.controller;

public class SesionManager {

    private static SesionManager instancia;

    private String usuario;
    private String fotoPerfil;
    private String rol;

    private SesionManager() {}

    public static SesionManager getInstancia() {
        if (instancia == null) {
            instancia = new SesionManager();
        }
        return instancia;
    }

    public void iniciarSesion(String usuario, String fotoPerfil, String rol) {
        this.usuario    = usuario;
        this.fotoPerfil = fotoPerfil;
        this.rol        = rol != null ? rol : "Usuario";
    }

    public String getUsuario() {
        return usuario != null ? usuario : "";
    }

    public String getFotoPerfil() {
        return fotoPerfil;
    }

    public String getRol() {
        return rol != null ? rol : "";
    }

    public void cerrarSesion() {
        this.usuario    = null;
        this.fotoPerfil = null;
        this.rol        = null;
    }
}
