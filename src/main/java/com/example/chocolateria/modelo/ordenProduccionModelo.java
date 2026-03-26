package com.example.chocolateria.modelo;

import java.time.LocalDate;

public class ordenProduccionModelo {

    private int idOrden;
    private LocalDate fechaInicio;
    private LocalDate fechaOrden;
    private LocalDate fechaEntrega;
    private String responsable;
    private String estado;
    private String prioridad;
    private String categoria;
    private String materiales;
    private String observaciones;

    public ordenProduccionModelo(int idOrden, LocalDate fechaInicio, LocalDate fechaOrden,
                                 LocalDate fechaEntrega, String responsable, String estado,
                                 String prioridad, String categoria, String materiales,
                                 String observaciones) {
        this.idOrden = idOrden;
        this.fechaInicio = fechaInicio;
        this.fechaOrden = fechaOrden;
        this.fechaEntrega = fechaEntrega;
        this.responsable = responsable;
        this.estado = estado;
        this.prioridad = prioridad;
        this.categoria = categoria;
        this.materiales = materiales;
        this.observaciones = observaciones;
    }

    public int getIdOrden() { return idOrden; }
    public LocalDate getFechaInicio() { return fechaInicio; }
    public LocalDate getFechaOrden() { return fechaOrden; }
    public LocalDate getFechaEntrega() { return fechaEntrega; }
    public String getResponsable() { return responsable; }
    public String getEstado() { return estado; }
    public String getPrioridad() { return prioridad; }
    public String getCategoria() { return categoria; }
    public String getMateriales() { return materiales; }
    public String getObservaciones() { return observaciones; }
}