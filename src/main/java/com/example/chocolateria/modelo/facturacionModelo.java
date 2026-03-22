package com.example.chocolateria.modelo;

import java.time.LocalDate;

public class facturacionModelo {

    private int idFactura;
    private String numeroFactura;
    private LocalDate fechaFactura;
    private String rncCliente;
    private String producto;
    private int cantidad;
    private double precioUnitario;
    private double subtotal;
    private double itbis;
    private double total;

    public facturacionModelo(int idFactura, String numeroFactura, LocalDate fechaFactura,
                             String rncCliente, String producto, int cantidad,
                             double precioUnitario, double subtotal, double itbis, double total) {
        this.idFactura = idFactura;
        this.numeroFactura = numeroFactura;
        this.fechaFactura = fechaFactura;
        this.rncCliente = rncCliente;
        this.producto = producto;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.subtotal = subtotal;
        this.itbis = itbis;
        this.total = total;
    }

    public int getIdFactura() { return idFactura; }
    public String getNumeroFactura() { return numeroFactura; }
    public LocalDate getFechaFactura() { return fechaFactura; }
    public String getRncCliente() { return rncCliente; }
    public String getProducto() { return producto; }
    public int getCantidad() { return cantidad; }
    public double getPrecioUnitario() { return precioUnitario; }
    public double getSubtotal() { return subtotal; }
    public double getItbis() { return itbis; }
    public double getTotal() { return total; }
}