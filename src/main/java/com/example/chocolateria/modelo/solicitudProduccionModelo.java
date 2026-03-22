package com.example.chocolateria.modelo;

import java.time.LocalDate;

public class solicitudProduccionModelo {

    private int id;
    private String codigoProducto; // NUEVO
    private String producto;
    private int cantidad;
    private LocalDate fechaSolicitud;
    private LocalDate fechaProduccion;
    private String prioridad;
    private String responsable;
    private String observaciones;

    // Constructor vacío
    public solicitudProduccionModelo() {}

    // Constructor con todos los campos (sin id)
    public solicitudProduccionModelo(String codigoProducto, String producto, int cantidad,
                                     LocalDate fechaSolicitud, LocalDate fechaProduccion,
                                     String prioridad, String responsable, String observaciones) {
        this.codigoProducto = codigoProducto;
        this.producto = producto;
        this.cantidad = cantidad;
        this.fechaSolicitud = fechaSolicitud;
        this.fechaProduccion = fechaProduccion;
        this.prioridad = prioridad;
        this.responsable = responsable;
        this.observaciones = observaciones;
    }

    // Constructor con id (opcional, si quieres usarlo al leer de la BD)
    public solicitudProduccionModelo(int id, String codigoProducto, String producto, int cantidad,
                                     LocalDate fechaSolicitud, LocalDate fechaProduccion,
                                     String prioridad, String responsable, String observaciones) {
        this.id = id;
        this.codigoProducto = codigoProducto;
        this.producto = producto;
        this.cantidad = cantidad;
        this.fechaSolicitud = fechaSolicitud;
        this.fechaProduccion = fechaProduccion;
        this.prioridad = prioridad;
        this.responsable = responsable;
        this.observaciones = observaciones;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getCodigoProducto() { return codigoProducto; }
    public void setCodigoProducto(String codigoProducto) { this.codigoProducto = codigoProducto; }

    public String getProducto() { return producto; }
    public void setProducto(String producto) { this.producto = producto; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public LocalDate getFechaSolicitud() { return fechaSolicitud; }
    public void setFechaSolicitud(LocalDate fechaSolicitud) { this.fechaSolicitud = fechaSolicitud; }

    public LocalDate getFechaProduccion() { return fechaProduccion; }
    public void setFechaProduccion(LocalDate fechaProduccion) { this.fechaProduccion = fechaProduccion; }

    public String getPrioridad() { return prioridad; }
    public void setPrioridad(String prioridad) { this.prioridad = prioridad; }

    public String getResponsable() { return responsable; }
    public void setResponsable(String responsable) { this.responsable = responsable; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
}