package com.example.chocolateria.modelo;

import javafx.beans.property.*;
import java.time.LocalDate;

public class reclamoModelo {

    private final SimpleIntegerProperty            idReclamo;
    private final SimpleIntegerProperty            idCliente;
    private final SimpleStringProperty             cliente;
    private final SimpleIntegerProperty            idOrden;
    private final SimpleStringProperty             tipoReclamo;
    private final SimpleStringProperty             estado;
    private final SimpleStringProperty             prioridad;
    private final SimpleStringProperty             descripcion;
    private final SimpleObjectProperty<LocalDate>  fechaReclamo;

    public reclamoModelo(int idReclamo, int idCliente, String cliente,
                         int idOrden, String tipoReclamo, String estado,
                         String prioridad, String descripcion, LocalDate fechaReclamo) {
        this.idReclamo   = new SimpleIntegerProperty(idReclamo);
        this.idCliente   = new SimpleIntegerProperty(idCliente);
        this.cliente     = new SimpleStringProperty(cliente);
        this.idOrden     = new SimpleIntegerProperty(idOrden);
        this.tipoReclamo = new SimpleStringProperty(tipoReclamo);
        this.estado      = new SimpleStringProperty(estado);
        this.prioridad   = new SimpleStringProperty(prioridad);
        this.descripcion = new SimpleStringProperty(descripcion);
        this.fechaReclamo= new SimpleObjectProperty<>(fechaReclamo);
    }

    public int       getIdReclamo()   { return idReclamo.get(); }
    public int       getIdCliente()   { return idCliente.get(); }
    public String    getCliente()     { return cliente.get(); }
    public int       getIdOrden()     { return idOrden.get(); }
    public String    getTipoReclamo() { return tipoReclamo.get(); }
    public String    getEstado()      { return estado.get(); }
    public String    getPrioridad()   { return prioridad.get(); }
    public String    getDescripcion() { return descripcion.get(); }
    public LocalDate getFechaReclamo(){ return fechaReclamo.get(); }

    public void setCliente(String v)     { cliente.set(v); }
    public void setIdOrden(int v)        { idOrden.set(v); }
    public void setTipoReclamo(String v) { tipoReclamo.set(v); }
    public void setEstado(String v)      { estado.set(v); }
    public void setPrioridad(String v)   { prioridad.set(v); }
    public void setDescripcion(String v) { descripcion.set(v); }

    public SimpleIntegerProperty            idReclamoProperty()   { return idReclamo; }
    public SimpleIntegerProperty            idClienteProperty()   { return idCliente; }
    public SimpleStringProperty             clienteProperty()     { return cliente; }
    public SimpleIntegerProperty            idOrdenProperty()     { return idOrden; }
    public SimpleStringProperty             tipoReclamoProperty() { return tipoReclamo; }
    public SimpleStringProperty             estadoProperty()      { return estado; }
    public SimpleStringProperty             prioridadProperty()   { return prioridad; }
    public SimpleStringProperty             descripcionProperty() { return descripcion; }
    public SimpleObjectProperty<LocalDate>  fechaReclamoProperty(){ return fechaReclamo; }
}
