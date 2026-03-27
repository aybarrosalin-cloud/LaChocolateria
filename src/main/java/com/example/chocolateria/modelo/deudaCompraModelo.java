package com.example.chocolateria.modelo;

import javafx.beans.property.*;

import java.time.LocalDate;

public class deudaCompraModelo {

    private final SimpleIntegerProperty           idDeuda;
    private final SimpleStringProperty            numeroOrden;
    private final SimpleIntegerProperty           idRecepcion;
    private final SimpleStringProperty            rncSuplidor;
    private final SimpleObjectProperty<LocalDate> fechaDeuda;
    private final SimpleDoubleProperty            montoTotal;
    private final SimpleDoubleProperty            montoPagado;
    private final SimpleDoubleProperty            montoPendiente;
    private final SimpleStringProperty            estado;
    private final SimpleStringProperty            observaciones;

    public deudaCompraModelo(int idDeuda, String numeroOrden, int idRecepcion,
                             String rncSuplidor, LocalDate fechaDeuda,
                             double montoTotal, double montoPagado, double montoPendiente,
                             String estado, String observaciones) {
        this.idDeuda        = new SimpleIntegerProperty(idDeuda);
        this.numeroOrden    = new SimpleStringProperty(numeroOrden);
        this.idRecepcion    = new SimpleIntegerProperty(idRecepcion);
        this.rncSuplidor    = new SimpleStringProperty(rncSuplidor);
        this.fechaDeuda     = new SimpleObjectProperty<>(fechaDeuda);
        this.montoTotal     = new SimpleDoubleProperty(montoTotal);
        this.montoPagado    = new SimpleDoubleProperty(montoPagado);
        this.montoPendiente = new SimpleDoubleProperty(montoPendiente);
        this.estado         = new SimpleStringProperty(estado);
        this.observaciones  = new SimpleStringProperty(observaciones);
    }

    public int       getIdDeuda()        { return idDeuda.get(); }
    public String    getNumeroOrden()    { return numeroOrden.get(); }
    public int       getIdRecepcion()    { return idRecepcion.get(); }
    public String    getRncSuplidor()    { return rncSuplidor.get(); }
    public LocalDate getFechaDeuda()     { return fechaDeuda.get(); }
    public double    getMontoTotal()     { return montoTotal.get(); }
    public double    getMontoPagado()    { return montoPagado.get(); }
    public double    getMontoPendiente() { return montoPendiente.get(); }
    public String    getEstado()         { return estado.get(); }
    public String    getObservaciones()  { return observaciones.get(); }

    public void setMontoPagado(double v)    { montoPagado.set(v); }
    public void setMontoPendiente(double v) { montoPendiente.set(v); }
    public void setEstado(String v)         { estado.set(v); }

    public SimpleIntegerProperty           idDeudaProperty()        { return idDeuda; }
    public SimpleStringProperty            numeroOrdenProperty()    { return numeroOrden; }
    public SimpleIntegerProperty           idRecepcionProperty()    { return idRecepcion; }
    public SimpleStringProperty            rncSuplidorProperty()    { return rncSuplidor; }
    public SimpleObjectProperty<LocalDate> fechaDeudaProperty()     { return fechaDeuda; }
    public SimpleDoubleProperty            montoTotalProperty()     { return montoTotal; }
    public SimpleDoubleProperty            montoPagadoProperty()    { return montoPagado; }
    public SimpleDoubleProperty            montoPendienteProperty() { return montoPendiente; }
    public SimpleStringProperty            estadoProperty()         { return estado; }
    public SimpleStringProperty            observacionesProperty()  { return observaciones; }
}