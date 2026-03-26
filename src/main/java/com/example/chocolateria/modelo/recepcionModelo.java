package com.example.chocolateria.modelo;

import java.time.LocalDate;

public class recepcionModelo {

    private int idRecepcion;
    private String rncProveedor;
    private String productos;
    private double montoTotal;
    private LocalDate fechaRecepcion;
    private double montoPorProducto;
    private String observaciones;
    private String numeroOrden;
    private double precioUnitario;
    private String cantidadRecibida;

    public recepcionModelo(int idRecepcion, String rncProveedor, String productos,
                           double montoTotal, LocalDate fechaRecepcion, double montoPorProducto,
                           String observaciones, String numeroOrden, double precioUnitario,
                           String cantidadRecibida) {
        this.idRecepcion = idRecepcion;
        this.rncProveedor = rncProveedor;
        this.productos = productos;
        this.montoTotal = montoTotal;
        this.fechaRecepcion = fechaRecepcion;
        this.montoPorProducto = montoPorProducto;
        this.observaciones = observaciones;
        this.numeroOrden = numeroOrden;
        this.precioUnitario = precioUnitario;
        this.cantidadRecibida = cantidadRecibida;
    }

    public int getIdRecepcion() { return idRecepcion; }
    public String getRncProveedor() { return rncProveedor; }
    public String getProductos() { return productos; }
    public double getMontoTotal() { return montoTotal; }
    public LocalDate getFechaRecepcion() { return fechaRecepcion; }
    public double getMontoPorProducto() { return montoPorProducto; }
    public String getObservaciones() { return observaciones; }
    public String getNumeroOrden() { return numeroOrden; }
    public double getPrecioUnitario() { return precioUnitario; }
    public String getCantidadRecibida() { return cantidadRecibida; }
}