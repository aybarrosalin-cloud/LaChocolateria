package com.example.chocolateria.modelo;

import javafx.beans.property.*;
import java.time.LocalDate;

public class ventaModelo {

    private final SimpleIntegerProperty            idVenta;
    private final SimpleIntegerProperty            idOrden;
    private final SimpleObjectProperty<LocalDate>  fechaVenta;
    private final SimpleStringProperty             cliente;
    private final SimpleDoubleProperty             subtotal;
    private final SimpleDoubleProperty             descuento;
    private final SimpleDoubleProperty             itbis;
    private final SimpleDoubleProperty             montoTotal;
    private final SimpleDoubleProperty             montoPagado;
    private final SimpleDoubleProperty             balancePendiente;
    private final SimpleStringProperty             tipoPago;
    private final SimpleStringProperty             estadoPago;
    private final SimpleStringProperty             metodoPago;
    private final SimpleStringProperty             ncf;
    private final SimpleIntegerProperty            idEmpleado;
    private final SimpleStringProperty             empleado;

    public ventaModelo(int idVenta, int idOrden, LocalDate fechaVenta, String cliente,
                       double subtotal, double descuento, double itbis, double montoTotal,
                       double montoPagado, double balancePendiente, String tipoPago,
                       String estadoPago, String metodoPago, String ncf,
                       int idEmpleado, String empleado) {
        this.idVenta          = new SimpleIntegerProperty(idVenta);
        this.idOrden          = new SimpleIntegerProperty(idOrden);
        this.fechaVenta       = new SimpleObjectProperty<>(fechaVenta);
        this.cliente          = new SimpleStringProperty(cliente);
        this.subtotal         = new SimpleDoubleProperty(subtotal);
        this.descuento        = new SimpleDoubleProperty(descuento);
        this.itbis            = new SimpleDoubleProperty(itbis);
        this.montoTotal       = new SimpleDoubleProperty(montoTotal);
        this.montoPagado      = new SimpleDoubleProperty(montoPagado);
        this.balancePendiente = new SimpleDoubleProperty(balancePendiente);
        this.tipoPago         = new SimpleStringProperty(tipoPago);
        this.estadoPago       = new SimpleStringProperty(estadoPago);
        this.metodoPago       = new SimpleStringProperty(metodoPago);
        this.ncf              = new SimpleStringProperty(ncf);
        this.idEmpleado       = new SimpleIntegerProperty(idEmpleado);
        this.empleado         = new SimpleStringProperty(empleado);
    }

    public int       getIdVenta()          { return idVenta.get(); }
    public int       getIdOrden()          { return idOrden.get(); }
    public LocalDate getFechaVenta()       { return fechaVenta.get(); }
    public String    getCliente()          { return cliente.get(); }
    public double    getSubtotal()         { return subtotal.get(); }
    public double    getDescuento()        { return descuento.get(); }
    public double    getItbis()            { return itbis.get(); }
    public double    getMontoTotal()       { return montoTotal.get(); }
    public double    getMontoPagado()      { return montoPagado.get(); }
    public double    getBalancePendiente() { return balancePendiente.get(); }
    public String    getTipoPago()         { return tipoPago.get(); }
    public String    getEstadoPago()       { return estadoPago.get(); }
    public String    getMetodoPago()       { return metodoPago.get(); }
    public String    getNcf()              { return ncf.get(); }
    public int       getIdEmpleado()       { return idEmpleado.get(); }
    public String    getEmpleado()         { return empleado.get(); }

    public void setMontoPagado(double v)      { montoPagado.set(v); }
    public void setBalancePendiente(double v)  { balancePendiente.set(v); }
    public void setEstadoPago(String v)        { estadoPago.set(v); }

    public SimpleIntegerProperty           idVentaProperty()          { return idVenta; }
    public SimpleIntegerProperty           idOrdenProperty()          { return idOrden; }
    public SimpleObjectProperty<LocalDate> fechaVentaProperty()       { return fechaVenta; }
    public SimpleStringProperty            clienteProperty()          { return cliente; }
    public SimpleDoubleProperty            subtotalProperty()         { return subtotal; }
    public SimpleDoubleProperty            descuentoProperty()        { return descuento; }
    public SimpleDoubleProperty            itbisProperty()            { return itbis; }
    public SimpleDoubleProperty            montoTotalProperty()       { return montoTotal; }
    public SimpleDoubleProperty            montoPagadoProperty()      { return montoPagado; }
    public SimpleDoubleProperty            balancePendienteProperty() { return balancePendiente; }
    public SimpleStringProperty            tipoPagoProperty()         { return tipoPago; }
    public SimpleStringProperty            estadoPagoProperty()       { return estadoPago; }
    public SimpleStringProperty            metodoPagoProperty()       { return metodoPago; }
    public SimpleStringProperty            ncfProperty()              { return ncf; }
    public SimpleIntegerProperty           idEmpleadoProperty()       { return idEmpleado; }
    public SimpleStringProperty            empleadoProperty()         { return empleado; }
}