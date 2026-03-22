package com.example.chocolateria.modelo;

public class empleadoModelo {

    private int id;
    private String nombre;
    private String apellido;
    private String tipoEmpleado;
    private String cedula;
    private String telefono;
    

    public empleadoModelo() {}

    public empleadoModelo(String nombre, String apellido, String tipoEmpleado, String cedula, String telefono) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.tipoEmpleado = tipoEmpleado;
        this.cedula = cedula;
        this.telefono = telefono;
    }

    public String getNombre() { return nombre; }
    public String getApellido() { return apellido; }
    public String getTipoEmpleado() { return tipoEmpleado; }
    public String getCedula() { return cedula; }
    public String getTelefono() { return telefono; }

    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    public void setTipoEmpleado(String tipoEmpleado) { this.tipoEmpleado = tipoEmpleado; }
    public void setCedula(String cedula) { this.cedula = cedula; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
}