package com.example.chocolateria.modelo;

import javafx.beans.property.*;

import java.time.LocalDate;

public class ordenProduccionModelo {

    private final SimpleIntegerProperty            idOrden;
    private final SimpleStringProperty             tipoOrden;
    private final SimpleObjectProperty<LocalDate>  fechaInicio;
    private final SimpleObjectProperty<LocalDate>  fechaOrden;
    private final SimpleObjectProperty<LocalDate>  fechaEntrega;
    private final SimpleIntegerProperty            idResponsable;
    private final SimpleStringProperty             responsable;
    private final SimpleStringProperty             estado;
    private final SimpleStringProperty             prioridad;
    private final SimpleStringProperty             categoria;
    private final SimpleStringProperty             materiales;
    private final SimpleStringProperty             observaciones;
    private final SimpleStringProperty             cliente;

    public ordenProduccionModelo(int idOrden, String tipoOrden, LocalDate fechaInicio,
                                 LocalDate fechaOrden, LocalDate fechaEntrega,
                                 int idResponsable, String responsable, String estado,
                                 String prioridad, String categoria, String materiales,
                                 String observaciones, String cliente) {
        this.idOrden       = new SimpleIntegerProperty(idOrden);
        this.tipoOrden     = new SimpleStringProperty(tipoOrden);
        this.fechaInicio   = new SimpleObjectProperty<>(fechaInicio);
        this.fechaOrden    = new SimpleObjectProperty<>(fechaOrden);
        this.fechaEntrega  = new SimpleObjectProperty<>(fechaEntrega);
        this.idResponsable = new SimpleIntegerProperty(idResponsable);
        this.responsable   = new SimpleStringProperty(responsable);
        this.estado        = new SimpleStringProperty(estado);
        this.prioridad     = new SimpleStringProperty(prioridad);
        this.categoria     = new SimpleStringProperty(categoria);
        this.materiales    = new SimpleStringProperty(materiales);
        this.observaciones = new SimpleStringProperty(observaciones);
        this.cliente       = new SimpleStringProperty(cliente);
    }

    public int       getIdOrden()       { return idOrden.get(); }
    public String    getTipoOrden()     { return tipoOrden.get(); }
    public LocalDate getFechaInicio()   { return fechaInicio.get(); }
    public LocalDate getFechaOrden()    { return fechaOrden.get(); }
    public LocalDate getFechaEntrega()  { return fechaEntrega.get(); }
    public int       getIdResponsable() { return idResponsable.get(); }
    public String    getResponsable()   { return responsable.get(); }
    public String    getEstado()        { return estado.get(); }
    public String    getPrioridad()     { return prioridad.get(); }
    public String    getCategoria()     { return categoria.get(); }
    public String    getMateriales()    { return materiales.get(); }
    public String    getObservaciones() { return observaciones.get(); }
    public String    getCliente()       { return cliente.get(); }

    public void setTipoOrden(String v)           { tipoOrden.set(v); }
    public void setFechaInicio(LocalDate v)       { fechaInicio.set(v); }
    public void setFechaOrden(LocalDate v)        { fechaOrden.set(v); }
    public void setFechaEntrega(LocalDate v)      { fechaEntrega.set(v); }
    public void setIdResponsable(int v)           { idResponsable.set(v); }
    public void setResponsable(String v)          { responsable.set(v); }
    public void setEstado(String v)               { estado.set(v); }
    public void setPrioridad(String v)            { prioridad.set(v); }
    public void setCategoria(String v)            { categoria.set(v); }
    public void setMateriales(String v)           { materiales.set(v); }
    public void setObservaciones(String v)        { observaciones.set(v); }
    public void setCliente(String v)              { cliente.set(v); }

    public SimpleIntegerProperty           idOrdenProperty()       { return idOrden; }
    public SimpleStringProperty            tipoOrdenProperty()     { return tipoOrden; }
    public SimpleObjectProperty<LocalDate> fechaInicioProperty()   { return fechaInicio; }
    public SimpleObjectProperty<LocalDate> fechaOrdenProperty()    { return fechaOrden; }
    public SimpleObjectProperty<LocalDate> fechaEntregaProperty()  { return fechaEntrega; }
    public SimpleIntegerProperty           idResponsableProperty() { return idResponsable; }
    public SimpleStringProperty            responsableProperty()   { return responsable; }
    public SimpleStringProperty            estadoProperty()        { return estado; }
    public SimpleStringProperty            prioridadProperty()     { return prioridad; }
    public SimpleStringProperty            categoriaProperty()     { return categoria; }
    public SimpleStringProperty            materialesProperty()    { return materiales; }
    public SimpleStringProperty            observacionesProperty() { return observaciones; }
    public SimpleStringProperty            clienteProperty()       { return cliente; }
}