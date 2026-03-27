package com.example.chocolateria.modelo;

import javafx.beans.property.*;

public class recepcionDetalleModelo {

    private final SimpleIntegerProperty idDetalle;
    private final SimpleIntegerProperty idRecepcion;
    private final SimpleStringProperty  codigoProducto;
    private final SimpleStringProperty  producto;
    private final SimpleIntegerProperty cantidadRecibida;
    private final SimpleDoubleProperty  precioUnitario;
    private final SimpleDoubleProperty  montoProducto;

    public recepcionDetalleModelo(int idDetalle, int idRecepcion, String codigoProducto,
                                  String producto, int cantidadRecibida,
                                  double precioUnitario, double montoProducto) {
        this.idDetalle        = new SimpleIntegerProperty(idDetalle);
        this.idRecepcion      = new SimpleIntegerProperty(idRecepcion);
        this.codigoProducto   = new SimpleStringProperty(codigoProducto);
        this.producto         = new SimpleStringProperty(producto);
        this.cantidadRecibida = new SimpleIntegerProperty(cantidadRecibida);
        this.precioUnitario   = new SimpleDoubleProperty(precioUnitario);
        this.montoProducto    = new SimpleDoubleProperty(montoProducto);
    }

    public int    getIdDetalle()        { return idDetalle.get(); }
    public int    getIdRecepcion()      { return idRecepcion.get(); }
    public String getCodigoProducto()   { return codigoProducto.get(); }
    public String getProducto()         { return producto.get(); }
    public int    getCantidadRecibida() { return cantidadRecibida.get(); }
    public double getPrecioUnitario()   { return precioUnitario.get(); }
    public double getMontoProducto()    { return montoProducto.get(); }

    public void setCodigoProducto(String v)   { codigoProducto.set(v); }
    public void setProducto(String v)          { producto.set(v); }
    public void setCantidadRecibida(int v)     { cantidadRecibida.set(v); }
    public void setPrecioUnitario(double v)    { precioUnitario.set(v); }
    public void setMontoProducto(double v)     { montoProducto.set(v); }

    public SimpleIntegerProperty idDetalleProperty()        { return idDetalle; }
    public SimpleIntegerProperty idRecepcionProperty()      { return idRecepcion; }
    public SimpleStringProperty  codigoProductoProperty()   { return codigoProducto; }
    public SimpleStringProperty  productoProperty()         { return producto; }
    public SimpleIntegerProperty cantidadRecibidaProperty() { return cantidadRecibida; }
    public SimpleDoubleProperty  precioUnitarioProperty()   { return precioUnitario; }
    public SimpleDoubleProperty  montoProductoProperty()    { return montoProducto; }
}