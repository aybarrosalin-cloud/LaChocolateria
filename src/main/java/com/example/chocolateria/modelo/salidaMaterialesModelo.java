package com.example.chocolateria.modelo;

import javafx.beans.property.*;

import java.time.LocalDate;

public class salidaMaterialesModelo {

    private final SimpleIntegerProperty            idSalida;
    private final SimpleIntegerProperty            idSolicitud;
    private final SimpleObjectProperty<LocalDate>  fechaSalida;
    private final SimpleStringProperty             responsable;
    private final SimpleStringProperty             observaciones;

    public salidaMaterialesModelo() {
        this(0, 0, null, "", "");
    }

    public salidaMaterialesModelo(int idSalida, int idSolicitud,
                                  LocalDate fechaSalida, String responsable,
                                  String observaciones) {
        this.idSalida      = new SimpleIntegerProperty(idSalida);
        this.idSolicitud   = new SimpleIntegerProperty(idSolicitud);
        this.fechaSalida   = new SimpleObjectProperty<>(fechaSalida);
        this.responsable   = new SimpleStringProperty(responsable);
        this.observaciones = new SimpleStringProperty(observaciones);
    }

    public int       getIdSalida()      { return idSalida.get(); }
    public int       getIdSolicitud()   { return idSolicitud.get(); }
    public LocalDate getFechaSalida()   { return fechaSalida.get(); }
    public String    getResponsable()   { return responsable.get(); }
    public String    getObservaciones() { return observaciones.get(); }

    public void setIdSolicitud(int v)            { idSolicitud.set(v); }
    public void setFechaSalida(LocalDate v)      { fechaSalida.set(v); }
    public void setResponsable(String v)         { responsable.set(v); }
    public void setObservaciones(String v)       { observaciones.set(v); }

    public SimpleIntegerProperty           idSalidaProperty()      { return idSalida; }
    public SimpleIntegerProperty           idSolicitudProperty()   { return idSolicitud; }
    public SimpleObjectProperty<LocalDate> fechaSalidaProperty()   { return fechaSalida; }
    public SimpleStringProperty            responsableProperty()   { return responsable; }
    public SimpleStringProperty            observacionesProperty() { return observaciones; }
}
