package com.example.chocolateria.modelo;

import javafx.beans.property.*;
import java.time.LocalDate;

public class ordenClienteModelo {

    private final SimpleIntegerProperty            idOrden;
    private final SimpleIntegerProperty            idCliente;
    private final SimpleStringProperty             cliente;
    private final SimpleObjectProperty<LocalDate>  fechaRegistro;
    private final SimpleObjectProperty<LocalDate>  fechaEntrega;
    private final SimpleStringProperty             metodoPago;
    private final SimpleStringProperty             estado;
    private final SimpleStringProperty             observaciones;

    public ordenClienteModelo(int idOrden, int idCliente, String cliente,
                               LocalDate fechaRegistro, LocalDate fechaEntrega,
                               String metodoPago, String estado, String observaciones) {
        this.idOrden       = new SimpleIntegerProperty(idOrden);
        this.idCliente     = new SimpleIntegerProperty(idCliente);
        this.cliente       = new SimpleStringProperty(cliente);
        this.fechaRegistro = new SimpleObjectProperty<>(fechaRegistro);
        this.fechaEntrega  = new SimpleObjectProperty<>(fechaEntrega);
        this.metodoPago    = new SimpleStringProperty(metodoPago);
        this.estado        = new SimpleStringProperty(estado);
        this.observaciones = new SimpleStringProperty(observaciones);
    }

    public int       getIdOrden()       { return idOrden.get(); }
    public int       getIdCliente()     { return idCliente.get(); }
    public String    getCliente()       { return cliente.get(); }
    public LocalDate getFechaRegistro() { return fechaRegistro.get(); }
    public LocalDate getFechaEntrega()  { return fechaEntrega.get(); }
    public String    getMetodoPago()    { return metodoPago.get(); }
    public String    getEstado()        { return estado.get(); }
    public String    getObservaciones() { return observaciones.get(); }

    public void setCliente(String v)          { cliente.set(v); }
    public void setFechaRegistro(LocalDate v) { fechaRegistro.set(v); }
    public void setFechaEntrega(LocalDate v)  { fechaEntrega.set(v); }
    public void setMetodoPago(String v)       { metodoPago.set(v); }
    public void setEstado(String v)           { estado.set(v); }
    public void setObservaciones(String v)    { observaciones.set(v); }

    public SimpleIntegerProperty           idOrdenProperty()       { return idOrden; }
    public SimpleIntegerProperty           idClienteProperty()     { return idCliente; }
    public SimpleStringProperty            clienteProperty()       { return cliente; }
    public SimpleObjectProperty<LocalDate> fechaRegistroProperty() { return fechaRegistro; }
    public SimpleObjectProperty<LocalDate> fechaEntregaProperty()  { return fechaEntrega; }
    public SimpleStringProperty            metodoPagoProperty()    { return metodoPago; }
    public SimpleStringProperty            estadoProperty()        { return estado; }
    public SimpleStringProperty            observacionesProperty() { return observaciones; }
}
