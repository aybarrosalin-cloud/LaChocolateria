package com.example.chocolateria.modelo;

import javafx.beans.property.*;

import java.time.LocalDate;

public class solicitudProduccionModelo {

    private final SimpleIntegerProperty            id;
    private final SimpleObjectProperty<LocalDate>  fechaSolicitud;
    private final SimpleObjectProperty<LocalDate>  fechaProduccion;
    private final SimpleStringProperty             prioridad;
    private final SimpleStringProperty             estado;
    private final SimpleIntegerProperty            idResponsable;
    private final SimpleStringProperty             responsable;
    private final SimpleStringProperty             observaciones;

    public solicitudProduccionModelo() {
        this(0, null, null, "", "", 0, "", "");
    }

    public solicitudProduccionModelo(int id, LocalDate fechaSolicitud, LocalDate fechaProduccion,
                                     String prioridad, String estado,
                                     int idResponsable, String responsable, String observaciones) {
        this.id              = new SimpleIntegerProperty(id);
        this.fechaSolicitud  = new SimpleObjectProperty<>(fechaSolicitud);
        this.fechaProduccion = new SimpleObjectProperty<>(fechaProduccion);
        this.prioridad       = new SimpleStringProperty(prioridad);
        this.estado          = new SimpleStringProperty(estado);
        this.idResponsable   = new SimpleIntegerProperty(idResponsable);
        this.responsable     = new SimpleStringProperty(responsable);
        this.observaciones   = new SimpleStringProperty(observaciones);
    }

    public int       getId()              { return id.get(); }
    public LocalDate getFechaSolicitud()  { return fechaSolicitud.get(); }
    public LocalDate getFechaProduccion() { return fechaProduccion.get(); }
    public String    getPrioridad()       { return prioridad.get(); }
    public String    getEstado()          { return estado.get(); }
    public int       getIdResponsable()   { return idResponsable.get(); }
    public String    getResponsable()     { return responsable.get(); }
    public String    getObservaciones()   { return observaciones.get(); }

    public void setFechaSolicitud(LocalDate v)  { fechaSolicitud.set(v); }
    public void setFechaProduccion(LocalDate v) { fechaProduccion.set(v); }
    public void setPrioridad(String v)          { prioridad.set(v); }
    public void setEstado(String v)             { estado.set(v); }
    public void setIdResponsable(int v)         { idResponsable.set(v); }
    public void setResponsable(String v)        { responsable.set(v); }
    public void setObservaciones(String v)      { observaciones.set(v); }

    public SimpleIntegerProperty           idProperty()              { return id; }
    public SimpleObjectProperty<LocalDate> fechaSolicitudProperty()  { return fechaSolicitud; }
    public SimpleObjectProperty<LocalDate> fechaProduccionProperty() { return fechaProduccion; }
    public SimpleStringProperty            prioridadProperty()       { return prioridad; }
    public SimpleStringProperty            estadoProperty()          { return estado; }
    public SimpleIntegerProperty           idResponsableProperty()   { return idResponsable; }
    public SimpleStringProperty            responsableProperty()     { return responsable; }
    public SimpleStringProperty            observacionesProperty()   { return observaciones; }
}