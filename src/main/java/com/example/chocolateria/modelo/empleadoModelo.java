package com.example.chocolateria.modelo;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class empleadoModelo {

    private final SimpleIntegerProperty idEmpleado;
    private final SimpleStringProperty  nombre;
    private final SimpleStringProperty  apellido;
    private final SimpleStringProperty  cedula;
    private final SimpleStringProperty  telefono;
    private final SimpleStringProperty  tipoEmpleado;
    private final SimpleStringProperty  rol;
    private final SimpleStringProperty  estado;

    public empleadoModelo(int idEmpleado, String nombre, String apellido, String cedula,
                          String telefono, String tipoEmpleado, String rol, String estado) {
        this.idEmpleado   = new SimpleIntegerProperty(idEmpleado);
        this.nombre       = new SimpleStringProperty(nombre);
        this.apellido     = new SimpleStringProperty(apellido);
        this.cedula       = new SimpleStringProperty(cedula);
        this.telefono     = new SimpleStringProperty(telefono);
        this.tipoEmpleado = new SimpleStringProperty(tipoEmpleado);
        this.rol          = new SimpleStringProperty(rol);
        this.estado       = new SimpleStringProperty(estado);
    }

    public int    getIdEmpleado()   { return idEmpleado.get(); }
    public String getNombre()       { return nombre.get(); }
    public String getApellido()     { return apellido.get(); }
    public String getCedula()       { return cedula.get(); }
    public String getTelefono()     { return telefono.get(); }
    public String getTipoEmpleado() { return tipoEmpleado.get(); }
    public String getRol()          { return rol.get(); }
    public String getEstado()       { return estado.get(); }

    public void setNombre(String v)       { nombre.set(v); }
    public void setApellido(String v)     { apellido.set(v); }
    public void setCedula(String v)       { cedula.set(v); }
    public void setTelefono(String v)     { telefono.set(v); }
    public void setTipoEmpleado(String v) { tipoEmpleado.set(v); }
    public void setRol(String v)          { rol.set(v); }
    public void setEstado(String v)       { estado.set(v); }

    public SimpleIntegerProperty idEmpleadoProperty()   { return idEmpleado; }
    public SimpleStringProperty  nombreProperty()       { return nombre; }
    public SimpleStringProperty  apellidoProperty()     { return apellido; }
    public SimpleStringProperty  cedulaProperty()       { return cedula; }
    public SimpleStringProperty  telefonoProperty()     { return telefono; }
    public SimpleStringProperty  tipoEmpleadoProperty() { return tipoEmpleado; }
    public SimpleStringProperty  rolProperty()          { return rol; }
    public SimpleStringProperty  estadoProperty()       { return estado; }
}