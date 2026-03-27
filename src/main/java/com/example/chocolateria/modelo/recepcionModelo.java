package com.example.chocolateria.modelo;

import javafx.beans.property.*;

import java.time.LocalDate;

public class recepcionModelo {

    private final SimpleIntegerProperty            idRecepcion;
    private final SimpleStringProperty             rncProveedor;
    private final SimpleStringProperty             numeroOrden;
    private final SimpleIntegerProperty            codigoOrden;
    private final SimpleObjectProperty<LocalDate>  fechaRecepcion;
    private final SimpleDoubleProperty             montoTotal;
    private final SimpleStringProperty             observaciones;

    public recepcionModelo() {
        this(0, "", "", 0, null, 0.0, "");
    }

    public recepcionModelo(int idRecepcion, String rncProveedor, String numeroOrden,
                           int codigoOrden, LocalDate fechaRecepcion,
                           double montoTotal, String observaciones) {
        this.idRecepcion    = new SimpleIntegerProperty(idRecepcion);
        this.rncProveedor   = new SimpleStringProperty(rncProveedor);
        this.numeroOrden    = new SimpleStringProperty(numeroOrden);
        this.codigoOrden    = new SimpleIntegerProperty(codigoOrden);
        this.fechaRecepcion = new SimpleObjectProperty<>(fechaRecepcion);
        this.montoTotal     = new SimpleDoubleProperty(montoTotal);
        this.observaciones  = new SimpleStringProperty(observaciones);
    }

    public int       getIdRecepcion()   { return idRecepcion.get(); }
    public String    getRncProveedor()  { return rncProveedor.get(); }
    public String    getNumeroOrden()   { return numeroOrden.get(); }
    public int       getCodigoOrden()   { return codigoOrden.get(); }
    public LocalDate getFechaRecepcion(){ return fechaRecepcion.get(); }
    public double    getMontoTotal()    { return montoTotal.get(); }
    public String    getObservaciones() { return observaciones.get(); }

    public void setRncProveedor(String v)    { rncProveedor.set(v); }
    public void setNumeroOrden(String v)     { numeroOrden.set(v); }
    public void setCodigoOrden(int v)        { codigoOrden.set(v); }
    public void setFechaRecepcion(LocalDate v){ fechaRecepcion.set(v); }
    public void setMontoTotal(double v)      { montoTotal.set(v); }
    public void setObservaciones(String v)   { observaciones.set(v); }

    public SimpleIntegerProperty           idRecepcionProperty()   { return idRecepcion; }
    public SimpleStringProperty            rncProveedorProperty()  { return rncProveedor; }
    public SimpleStringProperty            numeroOrdenProperty()   { return numeroOrden; }
    public SimpleIntegerProperty           codigoOrdenProperty()   { return codigoOrden; }
    public SimpleObjectProperty<LocalDate> fechaRecepcionProperty(){ return fechaRecepcion; }
    public SimpleDoubleProperty            montoTotalProperty()    { return montoTotal; }
    public SimpleStringProperty            observacionesProperty() { return observaciones; }
}