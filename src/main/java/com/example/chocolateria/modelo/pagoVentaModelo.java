package com.example.chocolateria.modelo;

import javafx.beans.property.*;
import java.time.LocalDate;

public class pagoVentaModelo {

    private final SimpleIntegerProperty           idPago;
    private final SimpleIntegerProperty           idVenta;
    private final SimpleObjectProperty<LocalDate> fechaPago;
    private final SimpleDoubleProperty            montoPagado;
    private final SimpleStringProperty            metodoPago;
    private final SimpleStringProperty            numeroReferencia;
    private final SimpleStringProperty            observaciones;

    public pagoVentaModelo(int idPago, int idVenta, LocalDate fechaPago,
                           double montoPagado, String metodoPago,
                           String numeroReferencia, String observaciones) {
        this.idPago          = new SimpleIntegerProperty(idPago);
        this.idVenta         = new SimpleIntegerProperty(idVenta);
        this.fechaPago       = new SimpleObjectProperty<>(fechaPago);
        this.montoPagado     = new SimpleDoubleProperty(montoPagado);
        this.metodoPago      = new SimpleStringProperty(metodoPago);
        this.numeroReferencia= new SimpleStringProperty(numeroReferencia);
        this.observaciones   = new SimpleStringProperty(observaciones);
    }

    public int       getIdPago()          { return idPago.get(); }
    public int       getIdVenta()         { return idVenta.get(); }
    public LocalDate getFechaPago()       { return fechaPago.get(); }
    public double    getMontoPagado()     { return montoPagado.get(); }
    public String    getMetodoPago()      { return metodoPago.get(); }
    public String    getNumeroReferencia(){ return numeroReferencia.get(); }
    public String    getObservaciones()   { return observaciones.get(); }

    public SimpleIntegerProperty           idPagoProperty()           { return idPago; }
    public SimpleIntegerProperty           idVentaProperty()          { return idVenta; }
    public SimpleObjectProperty<LocalDate> fechaPagoProperty()        { return fechaPago; }
    public SimpleDoubleProperty            montoPagadoProperty()      { return montoPagado; }
    public SimpleStringProperty            metodoPagoProperty()       { return metodoPago; }
    public SimpleStringProperty            numeroReferenciaProperty() { return numeroReferencia; }
    public SimpleStringProperty            observacionesProperty()    { return observaciones; }
}