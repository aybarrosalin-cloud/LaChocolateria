package com.example.chocolateria.modelo;

import java.time.LocalDate;

public class ventaModelo {

    private int idVenta;
    private String numeroFactura;
    private LocalDate fechaVenta;
    private String nombreCliente;
    private String producto;
    private int cantidad;
    private double precioUnitario;
    private double subtotal;
    private double total;
    private double abono;
    private double saldo;
    private String estadoPago;
    private String responsable;

    public ventaModelo(int idVenta, String numeroFactura, LocalDate fechaVenta,
                       String nombreCliente, String producto, int cantidad,
                       double precioUnitario, double subtotal, double total,
                       double abono, double saldo, String estadoPago, String responsable) {
        this.idVenta = idVenta;
        this.numeroFactura = numeroFactura;
        this.fechaVenta = fechaVenta;
        this.nombreCliente = nombreCliente;
        this.producto = producto;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.subtotal = subtotal;
        this.total = total;
        this.abono = abono;
        this.saldo = saldo;
        this.estadoPago = estadoPago;
        this.responsable = responsable;
    }

    public int getIdVenta() { return idVenta; }
    public String getNumeroFactura() { return numeroFactura; }
    public LocalDate getFechaVenta() { return fechaVenta; }
    public String getNombreCliente() { return nombreCliente; }
    public String getProducto() { return producto; }
    public int getCantidad() { return cantidad; }
    public double getPrecioUnitario() { return precioUnitario; }
    public double getSubtotal() { return subtotal; }
    public double getTotal() { return total; }
    public double getAbono() { return abono; }
    public double getSaldo() { return saldo; }
    public String getEstadoPago() { return estadoPago; }
    public String getResponsable() { return responsable; }
}