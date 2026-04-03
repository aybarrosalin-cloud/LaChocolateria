package com.example.chocolateria.modelo;

import javafx.beans.property.*;

public class suplidorModelo {

    private final SimpleIntegerProperty idSuplidor;
    private final SimpleStringProperty  nombre;
    private final SimpleStringProperty  apellido;
    private final SimpleStringProperty  rnc;
    private final SimpleStringProperty  telefono;
    private final SimpleStringProperty  correo;
    private final SimpleStringProperty  ciudad;

    public suplidorModelo(int idSuplidor, String nombre, String apellido,
                          String rnc, String telefono, String correo, String ciudad) {
        this.idSuplidor = new SimpleIntegerProperty(idSuplidor);
        this.nombre     = new SimpleStringProperty(nombre);
        this.apellido   = new SimpleStringProperty(apellido);
        this.rnc        = new SimpleStringProperty(rnc);
        this.telefono   = new SimpleStringProperty(telefono);
        this.correo     = new SimpleStringProperty(correo);
        this.ciudad     = new SimpleStringProperty(ciudad);
    }

    public int    getIdSuplidor() { return idSuplidor.get(); }
    public String getNombre()     { return nombre.get(); }
    public String getApellido()   { return apellido.get(); }
    public String getRnc()        { return rnc.get(); }
    public String getTelefono()   { return telefono.get(); }
    public String getCorreo()     { return correo.get(); }
    public String getCiudad()     { return ciudad.get(); }

    public void setNombre(String v)   { nombre.set(v); }
    public void setApellido(String v) { apellido.set(v); }
    public void setRnc(String v)      { rnc.set(v); }
    public void setTelefono(String v) { telefono.set(v); }
    public void setCorreo(String v)   { correo.set(v); }
    public void setCiudad(String v)   { ciudad.set(v); }

    public SimpleIntegerProperty idSuplidorProperty() { return idSuplidor; }
    public SimpleStringProperty  nombreProperty()     { return nombre; }
    public SimpleStringProperty  apellidoProperty()   { return apellido; }
    public SimpleStringProperty  rncProperty()        { return rnc; }
    public SimpleStringProperty  telefonoProperty()   { return telefono; }
    public SimpleStringProperty  correoProperty()     { return correo; }
    public SimpleStringProperty  ciudadProperty()     { return ciudad; }
}
