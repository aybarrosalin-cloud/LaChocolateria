package com.example.chocolateria.modelo;

public class loginModelo {

    private int idUsuario;
    private String usuario;
    private String password;
    private String fotoPerfil;

    public loginModelo(int idUsuario, String usuario, String password, String fotoPerfil) {
        this.idUsuario = idUsuario;
        this.usuario = usuario;
        this.password = password;
        this.fotoPerfil = fotoPerfil;
    }

    public int getIdUsuario() { return idUsuario; }
    public String getUsuario() { return usuario; }
    public String getPassword() { return password; }
    public String getFotoPerfil() { return fotoPerfil; }
}