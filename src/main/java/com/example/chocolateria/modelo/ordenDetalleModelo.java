package com.example.chocolateria.modelo;

import javafx.beans.property.*;

public class ordenDetalleModelo {

    private final SimpleIntegerProperty idDetalle;
    private final SimpleIntegerProperty idOrden;
    private final SimpleStringProperty  codigo;
    private final SimpleStringProperty  producto;
    private final SimpleStringProperty  categoria;
    private final SimpleIntegerProperty cantidad;
    private final SimpleDoubleProperty  precio;

    public ordenDetalleModelo(int idDetalle, int idOrden, String codigo,
                               String producto, String categoria,
                               int cantidad, double precio) {
        this.idDetalle = new SimpleIntegerProperty(idDetalle);
        this.idOrden   = new SimpleIntegerProperty(idOrden);
        this.codigo    = new SimpleStringProperty(codigo);
        this.producto  = new SimpleStringProperty(producto);
        this.categoria = new SimpleStringProperty(categoria);
        this.cantidad  = new SimpleIntegerProperty(cantidad);
        this.precio    = new SimpleDoubleProperty(precio);
    }

    public int    getIdDetalle() { return idDetalle.get(); }
    public int    getIdOrden()   { return idOrden.get(); }
    public String getCodigo()    { return codigo.get(); }
    public String getProducto()  { return producto.get(); }
    public String getCategoria() { return categoria.get(); }
    public int    getCantidad()  { return cantidad.get(); }
    public double getPrecio()    { return precio.get(); }

    public void setCodigo(String v)    { codigo.set(v); }
    public void setProducto(String v)  { producto.set(v); }
    public void setCategoria(String v) { categoria.set(v); }
    public void setCantidad(int v)     { cantidad.set(v); }
    public void setPrecio(double v)    { precio.set(v); }

    public SimpleIntegerProperty idDetalleProperty() { return idDetalle; }
    public SimpleIntegerProperty idOrdenProperty()   { return idOrden; }
    public SimpleStringProperty  codigoProperty()    { return codigo; }
    public SimpleStringProperty  productoProperty()  { return producto; }
    public SimpleStringProperty  categoriaProperty() { return categoria; }
    public SimpleIntegerProperty cantidadProperty()  { return cantidad; }
    public SimpleDoubleProperty  precioProperty()    { return precio; }
}
