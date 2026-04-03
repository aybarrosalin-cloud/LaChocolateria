package com.example.chocolateria.modelo;

import javafx.beans.property.*;
import java.time.LocalDate;

public class envioModelo {

    private final SimpleIntegerProperty            idEnvio;
    private final SimpleIntegerProperty            idCliente;
    private final SimpleStringProperty             cliente;
    private final SimpleObjectProperty<LocalDate>  fechaEnvio;
    private final SimpleObjectProperty<LocalDate>  fechaEntrega;
    private final SimpleStringProperty             transportista;
    private final SimpleStringProperty             temperaturaTransporte;
    private final SimpleStringProperty             estado;
    private final SimpleStringProperty             numeroGuia;
    private final SimpleStringProperty             provincia;
    private final SimpleStringProperty             ciudad;
    private final SimpleStringProperty             direccion;

    public envioModelo(int idEnvio, int idCliente, String cliente,
                       LocalDate fechaEnvio, LocalDate fechaEntrega,
                       String transportista, String temperaturaTransporte,
                       String estado, String numeroGuia,
                       String provincia, String ciudad, String direccion) {
        this.idEnvio               = new SimpleIntegerProperty(idEnvio);
        this.idCliente             = new SimpleIntegerProperty(idCliente);
        this.cliente               = new SimpleStringProperty(cliente);
        this.fechaEnvio            = new SimpleObjectProperty<>(fechaEnvio);
        this.fechaEntrega          = new SimpleObjectProperty<>(fechaEntrega);
        this.transportista         = new SimpleStringProperty(transportista);
        this.temperaturaTransporte = new SimpleStringProperty(temperaturaTransporte);
        this.estado                = new SimpleStringProperty(estado);
        this.numeroGuia            = new SimpleStringProperty(numeroGuia);
        this.provincia             = new SimpleStringProperty(provincia);
        this.ciudad                = new SimpleStringProperty(ciudad);
        this.direccion             = new SimpleStringProperty(direccion);
    }

    public int       getIdEnvio()               { return idEnvio.get(); }
    public int       getIdCliente()             { return idCliente.get(); }
    public String    getCliente()               { return cliente.get(); }
    public LocalDate getFechaEnvio()            { return fechaEnvio.get(); }
    public LocalDate getFechaEntrega()          { return fechaEntrega.get(); }
    public String    getTransportista()         { return transportista.get(); }
    public String    getTemperaturaTransporte() { return temperaturaTransporte.get(); }
    public String    getEstado()                { return estado.get(); }
    public String    getNumeroGuia()            { return numeroGuia.get(); }
    public String    getProvincia()             { return provincia.get(); }
    public String    getCiudad()               { return ciudad.get(); }
    public String    getDireccion()             { return direccion.get(); }

    public void setCliente(String v)               { cliente.set(v); }
    public void setFechaEnvio(LocalDate v)         { fechaEnvio.set(v); }
    public void setFechaEntrega(LocalDate v)       { fechaEntrega.set(v); }
    public void setTransportista(String v)         { transportista.set(v); }
    public void setTemperaturaTransporte(String v) { temperaturaTransporte.set(v); }
    public void setEstado(String v)                { estado.set(v); }
    public void setNumeroGuia(String v)            { numeroGuia.set(v); }
    public void setProvincia(String v)             { provincia.set(v); }
    public void setCiudad(String v)               { ciudad.set(v); }
    public void setDireccion(String v)             { direccion.set(v); }

    public SimpleIntegerProperty            idEnvioProperty()               { return idEnvio; }
    public SimpleIntegerProperty            idClienteProperty()             { return idCliente; }
    public SimpleStringProperty             clienteProperty()               { return cliente; }
    public SimpleObjectProperty<LocalDate>  fechaEnvioProperty()            { return fechaEnvio; }
    public SimpleObjectProperty<LocalDate>  fechaEntregaProperty()          { return fechaEntrega; }
    public SimpleStringProperty             transportistaProperty()         { return transportista; }
    public SimpleStringProperty             temperaturaTransporteProperty() { return temperaturaTransporte; }
    public SimpleStringProperty             estadoProperty()                { return estado; }
    public SimpleStringProperty             numeroGuiaProperty()            { return numeroGuia; }
    public SimpleStringProperty             provinciaProperty()             { return provincia; }
    public SimpleStringProperty             ciudadProperty()               { return ciudad; }
    public SimpleStringProperty             direccionProperty()             { return direccion; }
}
