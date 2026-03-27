package com.example.chocolateria.modelo;

import javafx.beans.property.*;

public class solicitudDetalleModelo {

    private final SimpleIntegerProperty idDetalle;
    private final SimpleIntegerProperty idSolicitud;
    private final SimpleStringProperty  codigoProducto;
    private final SimpleStringProperty  producto;
    private final SimpleIntegerProperty cantidad;

    public solicitudDetalleModelo(int idDetalle, int idSolicitud,
                                  String codigoProducto, String producto, int cantidad) {
        this.idDetalle      = new SimpleIntegerProperty(idDetalle);
        this.idSolicitud    = new SimpleIntegerProperty(idSolicitud);
        this.codigoProducto = new SimpleStringProperty(codigoProducto);
        this.producto       = new SimpleStringProperty(producto);
        this.cantidad       = new SimpleIntegerProperty(cantidad);
    }

    public int    getIdDetalle()      { return idDetalle.get(); }
    public int    getIdSolicitud()    { return idSolicitud.get(); }
    public String getCodigoProducto() { return codigoProducto.get(); }
    public String getProducto()       { return producto.get(); }
    public int    getCantidad()       { return cantidad.get(); }

    public void setCantidad(int v)       { cantidad.set(v); }
    public void setProducto(String v)    { producto.set(v); }

    public SimpleIntegerProperty idDetalleProperty()      { return idDetalle; }
    public SimpleIntegerProperty idSolicitudProperty()    { return idSolicitud; }
    public SimpleStringProperty  codigoProductoProperty() { return codigoProducto; }
    public SimpleStringProperty  productoProperty()       { return producto; }
    public SimpleIntegerProperty cantidadProperty()       { return cantidad; }
}