package com.example.chocolateria.modelo;

import javafx.beans.property.*;

public class ordenProveedorDetalleModelo {

    private final SimpleIntegerProperty idDetalle;
    private final SimpleIntegerProperty idOrden;
    private final SimpleStringProperty  codigoProd;
    private final SimpleStringProperty  producto;
    private final SimpleIntegerProperty cantidad;
    private final SimpleDoubleProperty  precio;
    private final SimpleDoubleProperty  subtotal;

    public ordenProveedorDetalleModelo(int idDetalle, int idOrden, String codigoProd,
                                       String producto, int cantidad, double precio) {
        this.idDetalle  = new SimpleIntegerProperty(idDetalle);
        this.idOrden    = new SimpleIntegerProperty(idOrden);
        this.codigoProd = new SimpleStringProperty(codigoProd);
        this.producto   = new SimpleStringProperty(producto);
        this.cantidad   = new SimpleIntegerProperty(cantidad);
        this.precio     = new SimpleDoubleProperty(precio);
        this.subtotal   = new SimpleDoubleProperty(cantidad * precio);
    }

    public int    getIdDetalle()  { return idDetalle.get(); }
    public int    getIdOrden()    { return idOrden.get(); }
    public String getCodigoProd() { return codigoProd.get(); }
    public String getProducto()   { return producto.get(); }
    public int    getCantidad()   { return cantidad.get(); }
    public double getPrecio()     { return precio.get(); }
    public double getSubtotal()   { return subtotal.get(); }

    public void setCantidad(int v)    { cantidad.set(v); subtotal.set(v * precio.get()); }
    public void setPrecio(double v)   { precio.set(v);   subtotal.set(cantidad.get() * v); }

    public SimpleIntegerProperty idDetalleProperty()  { return idDetalle; }
    public SimpleIntegerProperty idOrdenProperty()    { return idOrden; }
    public SimpleStringProperty  codigoProdProperty() { return codigoProd; }
    public SimpleStringProperty  productoProperty()   { return producto; }
    public SimpleIntegerProperty cantidadProperty()   { return cantidad; }
    public SimpleDoubleProperty  precioProperty()     { return precio; }
    public SimpleDoubleProperty  subtotalProperty()   { return subtotal; }
}
