package com.example.chocolateria.modelo;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class usuarioModelo {

    private final SimpleIntegerProperty idUsuario;
    private final SimpleStringProperty  usuario;
    private final SimpleStringProperty  rol;
    private final SimpleStringProperty  estado;

    public usuarioModelo(int idUsuario, String usuario, String rol, String estado) {
        this.idUsuario = new SimpleIntegerProperty(idUsuario);
        this.usuario   = new SimpleStringProperty(usuario);
        this.rol       = new SimpleStringProperty(rol);
        this.estado    = new SimpleStringProperty(estado);
    }

    public int    getIdUsuario() { return idUsuario.get(); }
    public String getUsuario()   { return usuario.get(); }
    public String getRol()       { return rol.get(); }
    public String getEstado()    { return estado.get(); }

    public void setRol(String v)    { rol.set(v); }
    public void setEstado(String v) { estado.set(v); }

    public SimpleIntegerProperty idUsuarioProperty() { return idUsuario; }
    public SimpleStringProperty  usuarioProperty()   { return usuario; }
    public SimpleStringProperty  rolProperty()       { return rol; }
    public SimpleStringProperty  estadoProperty()    { return estado; }
}
