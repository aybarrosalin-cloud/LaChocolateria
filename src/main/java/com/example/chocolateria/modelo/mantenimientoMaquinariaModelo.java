package com.example.chocolateria.modelo;

import javafx.beans.property.*;

import java.time.LocalDate;

public class mantenimientoMaquinariaModelo {

    private final SimpleIntegerProperty           id;
    private final SimpleObjectProperty<LocalDate> fechaMantenimiento;
    private final SimpleObjectProperty<LocalDate> fechaProximoMantenimiento;
    private final SimpleStringProperty            maquina;
    private final SimpleStringProperty            tecnico;
    private final SimpleDoubleProperty            costo;
    private final SimpleStringProperty            estadoMaquina;
    private final SimpleStringProperty            tipoMantenimiento;
    private final SimpleStringProperty            observaciones;

    public mantenimientoMaquinariaModelo() {
        this(0, null, null, "", "", 0.0, "", "", "");
    }

    public mantenimientoMaquinariaModelo(int id, LocalDate fechaMantenimiento, LocalDate fechaProximoMantenimiento,
                                         String maquina, String tecnico, double costo,
                                         String estadoMaquina, String tipoMantenimiento, String observaciones) {
        this.id                        = new SimpleIntegerProperty(id);
        this.fechaMantenimiento        = new SimpleObjectProperty<>(fechaMantenimiento);
        this.fechaProximoMantenimiento = new SimpleObjectProperty<>(fechaProximoMantenimiento);
        this.maquina                   = new SimpleStringProperty(maquina);
        this.tecnico                   = new SimpleStringProperty(tecnico);
        this.costo                     = new SimpleDoubleProperty(costo);
        this.estadoMaquina             = new SimpleStringProperty(estadoMaquina);
        this.tipoMantenimiento         = new SimpleStringProperty(tipoMantenimiento);
        this.observaciones             = new SimpleStringProperty(observaciones);
    }

    public int       getId()                        { return id.get(); }
    public LocalDate getFechaMantenimiento()        { return fechaMantenimiento.get(); }
    public LocalDate getFechaProximoMantenimiento() { return fechaProximoMantenimiento.get(); }
    public String    getMaquina()                   { return maquina.get(); }
    public String    getTecnico()                   { return tecnico.get(); }
    public double    getCosto()                     { return costo.get(); }
    public String    getEstadoMaquina()             { return estadoMaquina.get(); }
    public String    getTipoMantenimiento()         { return tipoMantenimiento.get(); }
    public String    getObservaciones()             { return observaciones.get(); }

    public void setId(int v)                              { id.set(v); }
    public void setFechaMantenimiento(LocalDate v)        { fechaMantenimiento.set(v); }
    public void setFechaProximoMantenimiento(LocalDate v) { fechaProximoMantenimiento.set(v); }
    public void setMaquina(String v)                      { maquina.set(v); }
    public void setTecnico(String v)                      { tecnico.set(v); }
    public void setCosto(double v)                        { costo.set(v); }
    public void setEstadoMaquina(String v)                { estadoMaquina.set(v); }
    public void setTipoMantenimiento(String v)            { tipoMantenimiento.set(v); }
    public void setObservaciones(String v)                { observaciones.set(v); }

    public SimpleIntegerProperty            idProperty()                        { return id; }
    public SimpleObjectProperty<LocalDate>  fechaMantenimientoProperty()        { return fechaMantenimiento; }
    public SimpleObjectProperty<LocalDate>  fechaProximoMantenimientoProperty() { return fechaProximoMantenimiento; }
    public SimpleStringProperty             maquinaProperty()                   { return maquina; }
    public SimpleStringProperty             tecnicoProperty()                   { return tecnico; }
    public SimpleDoubleProperty             costoProperty()                     { return costo; }
    public SimpleStringProperty             estadoMaquinaProperty()             { return estadoMaquina; }
    public SimpleStringProperty             tipoMantenimientoProperty()         { return tipoMantenimiento; }
    public SimpleStringProperty             observacionesProperty()             { return observaciones; }
}