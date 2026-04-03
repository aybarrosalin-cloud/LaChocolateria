package com.example.chocolateria.modelo;

import javafx.beans.property.*;

public class productoModelo {

    private final SimpleStringProperty  codigo;
    private final SimpleStringProperty  nombre;
    private final SimpleDoubleProperty  precioUnitario;
    private final SimpleDoubleProperty  precioMayor;
    private final SimpleStringProperty  descripcion;
    private final SimpleStringProperty  unidadMedida;
    private final SimpleStringProperty  categoria;
    private final SimpleStringProperty  tipo;
    private final SimpleIntegerProperty stock;

    public productoModelo(String codigo, String nombre, double precioUnitario,
                          double precioMayor, String descripcion, String unidadMedida,
                          String categoria, String tipo, int stock) {
        this.codigo         = new SimpleStringProperty(codigo);
        this.nombre         = new SimpleStringProperty(nombre);
        this.precioUnitario = new SimpleDoubleProperty(precioUnitario);
        this.precioMayor    = new SimpleDoubleProperty(precioMayor);
        this.descripcion    = new SimpleStringProperty(descripcion);
        this.unidadMedida   = new SimpleStringProperty(unidadMedida);
        this.categoria      = new SimpleStringProperty(categoria);
        this.tipo           = new SimpleStringProperty(tipo);
        this.stock          = new SimpleIntegerProperty(stock);
    }

    public String  getCodigo()         { return codigo.get(); }
    public String  getNombre()         { return nombre.get(); }
    public double  getPrecioUnitario() { return precioUnitario.get(); }
    public double  getPrecioMayor()    { return precioMayor.get(); }
    public String  getDescripcion()    { return descripcion.get(); }
    public String  getUnidadMedida()   { return unidadMedida.get(); }
    public String  getCategoria()      { return categoria.get(); }
    public String  getTipo()           { return tipo.get(); }
    public int     getStock()          { return stock.get(); }

    public void setNombre(String v)         { nombre.set(v); }
    public void setPrecioUnitario(double v) { precioUnitario.set(v); }
    public void setPrecioMayor(double v)    { precioMayor.set(v); }
    public void setDescripcion(String v)    { descripcion.set(v); }
    public void setUnidadMedida(String v)   { unidadMedida.set(v); }
    public void setCategoria(String v)      { categoria.set(v); }
    public void setTipo(String v)           { tipo.set(v); }
    public void setStock(int v)             { stock.set(v); }

    public SimpleStringProperty  codigoProperty()         { return codigo; }
    public SimpleStringProperty  nombreProperty()         { return nombre; }
    public SimpleDoubleProperty  precioUnitarioProperty() { return precioUnitario; }
    public SimpleDoubleProperty  precioMayorProperty()    { return precioMayor; }
    public SimpleStringProperty  descripcionProperty()    { return descripcion; }
    public SimpleStringProperty  unidadMedidaProperty()   { return unidadMedida; }
    public SimpleStringProperty  categoriaProperty()      { return categoria; }
    public SimpleStringProperty  tipoProperty()           { return tipo; }
    public SimpleIntegerProperty stockProperty()          { return stock; }
}
