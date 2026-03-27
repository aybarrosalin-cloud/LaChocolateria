package com.example.chocolateria.modelo;

import javafx.beans.property.*;

import java.time.LocalDate;

public class abonoCompraModelo {

    private final SimpleIntegerProperty           idAbono;
    private final SimpleIntegerProperty           idDeuda;
    private final SimpleObjectProperty<LocalDate> fechaAbono;
    private final SimpleDoubleProperty            montoAbono;
    private final SimpleStringProperty            metodoPago;
    private final SimpleStringProperty            numeroReferencia;
    private final SimpleStringProperty            observaciones;

    public abonoCompraModelo(int idAbono, int idDeuda, LocalDate fechaAbono,
                             double montoAbono, String metodoPago,
                             String numeroReferencia, String observaciones) {
        this.idAbono          = new SimpleIntegerProperty(idAbono);
        this.idDeuda          = new SimpleIntegerProperty(idDeuda);
        this.fechaAbono       = new SimpleObjectProperty<>(fechaAbono);
        this.montoAbono       = new SimpleDoubleProperty(montoAbono);
        this.metodoPago       = new SimpleStringProperty(metodoPago);
        this.numeroReferencia = new SimpleStringProperty(numeroReferencia);
        this.observaciones    = new SimpleStringProperty(observaciones);
    }

    public int       getIdAbono()          { return idAbono.get(); }
    public int       getIdDeuda()          { return idDeuda.get(); }
    public LocalDate getFechaAbono()       { return fechaAbono.get(); }
    public double    getMontoAbono()       { return montoAbono.get(); }
    public String    getMetodoPago()       { return metodoPago.get(); }
    public String    getNumeroReferencia() { return numeroReferencia.get(); }
    public String    getObservaciones()    { return observaciones.get(); }

    public SimpleIntegerProperty           idAbonoProperty()          { return idAbono; }
    public SimpleIntegerProperty           idDeudaProperty()          { return idDeuda; }
    public SimpleObjectProperty<LocalDate> fechaAbonoProperty()       { return fechaAbono; }
    public SimpleDoubleProperty            montoAbonoProperty()       { return montoAbono; }
    public SimpleStringProperty            metodoPagoProperty()       { return metodoPago; }
    public SimpleStringProperty            numeroReferenciaProperty() { return numeroReferencia; }
    public SimpleStringProperty            observacionesProperty()    { return observaciones; }
}