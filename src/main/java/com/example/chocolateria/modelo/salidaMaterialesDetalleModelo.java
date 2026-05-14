package com.example.chocolateria.modelo;

import javafx.beans.property.*;

public class salidaMaterialesDetalleModelo {

    private final SimpleIntegerProperty idDetalle;
    private final SimpleIntegerProperty idSalida;
    private final SimpleStringProperty  codigoProducto;
    private final SimpleStringProperty  producto;
    private final SimpleIntegerProperty cantidad;
    private final SimpleStringProperty  unidadMedida;

    public salidaMaterialesDetalleModelo(int idDetalle, int idSalida, String codigoProducto,
                                         String producto, int cantidad, String unidadMedida) {
        this.idDetalle      = new SimpleIntegerProperty(idDetalle);
        this.idSalida       = new SimpleIntegerProperty(idSalida);
        this.codigoProducto = new SimpleStringProperty(codigoProducto);
        this.producto       = new SimpleStringProperty(producto);
        this.cantidad       = new SimpleIntegerProperty(cantidad);
        this.unidadMedida   = new SimpleStringProperty(unidadMedida);
    }

    public int    getIdDetalle()      { return idDetalle.get(); }
    public int    getIdSalida()       { return idSalida.get(); }
    public String getCodigoProducto() { return codigoProducto.get(); }
    public String getProducto()       { return producto.get(); }
    public int    getCantidad()       { return cantidad.get(); }
    public String getUnidadMedida()   { return unidadMedida.get(); }

    public void setCodigoProducto(String v) { codigoProducto.set(v); }
    public void setProducto(String v)       { producto.set(v); }
    public void setCantidad(int v)           { cantidad.set(v); }
    public void setUnidadMedida(String v)    { unidadMedida.set(v); }

    public SimpleIntegerProperty idDetalleProperty()      { return idDetalle; }
    public SimpleIntegerProperty idSalidaProperty()       { return idSalida; }
    public SimpleStringProperty  codigoProductoProperty() { return codigoProducto; }
    public SimpleStringProperty  productoProperty()       { return producto; }
    public SimpleIntegerProperty cantidadProperty()       { return cantidad; }
    public SimpleStringProperty  unidadMedidaProperty()   { return unidadMedida; }
}
