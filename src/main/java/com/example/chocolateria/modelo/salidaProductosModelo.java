package com.example.chocolateria.modelo;

import javafx.beans.property.*;

import java.time.LocalDate;

public class salidaProductosModelo {

    private final SimpleIntegerProperty            idSalida;
    private final SimpleIntegerProperty            idOrdenCliente;
    private final SimpleStringProperty             cliente;
    private final SimpleObjectProperty<LocalDate>  fechaSalida;
    private final SimpleStringProperty             responsable;
    private final SimpleStringProperty             observaciones;

    public salidaProductosModelo() {
        this(0, 0, "", null, "", "");
    }

    public salidaProductosModelo(int idSalida, int idOrdenCliente, String cliente,
                                 LocalDate fechaSalida, String responsable,
                                 String observaciones) {
        this.idSalida       = new SimpleIntegerProperty(idSalida);
        this.idOrdenCliente = new SimpleIntegerProperty(idOrdenCliente);
        this.cliente        = new SimpleStringProperty(cliente);
        this.fechaSalida    = new SimpleObjectProperty<>(fechaSalida);
        this.responsable    = new SimpleStringProperty(responsable);
        this.observaciones  = new SimpleStringProperty(observaciones);
    }

    public int       getIdSalida()       { return idSalida.get(); }
    public int       getIdOrdenCliente() { return idOrdenCliente.get(); }
    public String    getCliente()        { return cliente.get(); }
    public LocalDate getFechaSalida()    { return fechaSalida.get(); }
    public String    getResponsable()    { return responsable.get(); }
    public String    getObservaciones()  { return observaciones.get(); }

    public void setIdOrdenCliente(int v)       { idOrdenCliente.set(v); }
    public void setCliente(String v)            { cliente.set(v); }
    public void setFechaSalida(LocalDate v)     { fechaSalida.set(v); }
    public void setResponsable(String v)        { responsable.set(v); }
    public void setObservaciones(String v)      { observaciones.set(v); }

    public SimpleIntegerProperty           idSalidaProperty()       { return idSalida; }
    public SimpleIntegerProperty           idOrdenClienteProperty() { return idOrdenCliente; }
    public SimpleStringProperty            clienteProperty()        { return cliente; }
    public SimpleObjectProperty<LocalDate> fechaSalidaProperty()    { return fechaSalida; }
    public SimpleStringProperty            responsableProperty()    { return responsable; }
    public SimpleStringProperty            observacionesProperty()  { return observaciones; }
}
