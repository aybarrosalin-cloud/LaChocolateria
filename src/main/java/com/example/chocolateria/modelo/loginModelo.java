package com.example.chocolateria.modelo;

public class loginModelo {

    private int idUsuario;
    private String usuario;
    private String password;

    public loginModelo(int idUsuario, String usuario, String password) {
        this.idUsuario = idUsuario;
        this.usuario = usuario;
        this.password = password;
    }

    public int getIdUsuario() { return idUsuario; }
    public String getUsuario() { return usuario; }
    public String getPassword() { return password; }
}