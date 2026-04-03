package com.example.chocolateria.modelo;

import javafx.beans.property.*;
import java.time.LocalDate;

public class ordenProveedorModelo {

    private final SimpleIntegerProperty            codigo;
    private final SimpleStringProperty             rncProveedor;
    private final SimpleStringProperty             proveedor;
    private final SimpleObjectProperty<LocalDate>  fechaRequerida;
    private final SimpleStringProperty             prioridad;
    private final SimpleStringProperty             categoria;
    private final SimpleStringProperty             estadoPago;
    private final SimpleDoubleProperty             montoTotal;
    private final SimpleStringProperty             descripcion;

    public ordenProveedorModelo(int codigo, String rncProveedor, String proveedor,
                                LocalDate fechaRequerida, String prioridad,
                                String categoria, String estadoPago,
                                double montoTotal, String descripcion) {
        this.codigo         = new SimpleIntegerProperty(codigo);
        this.rncProveedor   = new SimpleStringProperty(rncProveedor);
        this.proveedor      = new SimpleStringProperty(proveedor);
        this.fechaRequerida = new SimpleObjectProperty<>(fechaRequerida);
        this.prioridad      = new SimpleStringProperty(prioridad);
        this.categoria      = new SimpleStringProperty(categoria);
        this.estadoPago     = new SimpleStringProperty(estadoPago);
        this.montoTotal     = new SimpleDoubleProperty(montoTotal);
        this.descripcion    = new SimpleStringProperty(descripcion);
    }

    public int       getCodigo()        { return codigo.get(); }
    public String    getRncProveedor()  { return rncProveedor.get(); }
    public String    getProveedor()     { return proveedor.get(); }
    public LocalDate getFechaRequerida(){ return fechaRequerida.get(); }
    public String    getPrioridad()     { return prioridad.get(); }
    public String    getCategoria()     { return categoria.get(); }
    public String    getEstadoPago()    { return estadoPago.get(); }
    public double    getMontoTotal()    { return montoTotal.get(); }
    public String    getDescripcion()   { return descripcion.get(); }

    public void setRncProveedor(String v)   { rncProveedor.set(v); }
    public void setProveedor(String v)      { proveedor.set(v); }
    public void setFechaRequerida(LocalDate v){ fechaRequerida.set(v); }
    public void setPrioridad(String v)      { prioridad.set(v); }
    public void setCategoria(String v)      { categoria.set(v); }
    public void setEstadoPago(String v)     { estadoPago.set(v); }
    public void setMontoTotal(double v)     { montoTotal.set(v); }
    public void setDescripcion(String v)    { descripcion.set(v); }

    public SimpleIntegerProperty            codigoProperty()        { return codigo; }
    public SimpleStringProperty             rncProveedorProperty()  { return rncProveedor; }
    public SimpleStringProperty             proveedorProperty()     { return proveedor; }
    public SimpleObjectProperty<LocalDate>  fechaRequeridaProperty(){ return fechaRequerida; }
    public SimpleStringProperty             prioridadProperty()     { return prioridad; }
    public SimpleStringProperty             categoriaProperty()     { return categoria; }
    public SimpleStringProperty             estadoPagoProperty()    { return estadoPago; }
    public SimpleDoubleProperty             montoTotalProperty()    { return montoTotal; }
    public SimpleStringProperty             descripcionProperty()   { return descripcion; }
}
