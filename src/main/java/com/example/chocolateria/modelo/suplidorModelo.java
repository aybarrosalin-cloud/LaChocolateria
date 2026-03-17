package com.example.chocolateria.modelo;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class suplidorModelo {

    private StringProperty nombre;
    private StringProperty rnc;
    private StringProperty telefono;
    private StringProperty correo;
    private StringProperty ciudad;

    public suplidorModelo(String nombre, String rnc, String telefono, String correo, String ciudad) {
        this.nombre = new SimpleStringProperty(nombre);
        this.rnc = new SimpleStringProperty(rnc);
        this.telefono = new SimpleStringProperty(telefono);
        this.correo = new SimpleStringProperty(correo);
        this.ciudad = new SimpleStringProperty(ciudad);
    }

    // GETTERS NORMALES (opcionales pero recomendados)

    public String getNombre() {
        return nombre.get();
    }

    public String getRnc() {
        return rnc.get();
    }

    public String getTelefono() {
        return telefono.get();
    }

    public String getCorreo() {
        return correo.get();
    }

    public String getCiudad() {
        return ciudad.get();
    }

    // PROPERTY METHODS (NECESARIAS PARA LA TABLA)

    public StringProperty nombreProperty() {
        return nombre;
    }

    public StringProperty rncProperty() {
        return rnc;
    }

    public StringProperty telefonoProperty() {
        return telefono;
    }

    public StringProperty correoProperty() {
        return correo;
    }

    public StringProperty ciudadProperty() {
        return ciudad;
    }
}