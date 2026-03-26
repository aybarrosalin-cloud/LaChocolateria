package com.example.chocolateria.modelo;

import java.time.LocalDate;

public class mantenimientoMaquinariaModelo {

    private int id;
    private LocalDate fechaMantenimiento;
    private String maquina;
    private String tecnico;
    private double costo;
    private String estadoMaquina;
    private String tipoMantenimiento;
    private String observaciones;

    public mantenimientoMaquinariaModelo() {}

    public mantenimientoMaquinariaModelo(int id, LocalDate fechaMantenimiento, String maquina, String tecnico,
                                         double costo, String estadoMaquina, String tipoMantenimiento, String observaciones) {
        this.id = id;
        this.fechaMantenimiento = fechaMantenimiento;
        this.maquina = maquina;
        this.tecnico = tecnico;
        this.costo = costo;
        this.estadoMaquina = estadoMaquina;
        this.tipoMantenimiento = tipoMantenimiento;
        this.observaciones = observaciones;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public LocalDate getFechaMantenimiento() { return fechaMantenimiento; }
    public void setFechaMantenimiento(LocalDate fechaMantenimiento) { this.fechaMantenimiento = fechaMantenimiento; }

    public String getMaquina() { return maquina; }
    public void setMaquina(String maquina) { this.maquina = maquina; }

    public String getTecnico() { return tecnico; }
    public void setTecnico(String tecnico) { this.tecnico = tecnico; }

    public double getCosto() { return costo; }
    public void setCosto(double costo) { this.costo = costo; }

    public String getEstadoMaquina() { return estadoMaquina; }
    public void setEstadoMaquina(String estadoMaquina) { this.estadoMaquina = estadoMaquina; }

    public String getTipoMantenimiento() { return tipoMantenimiento; }
    public void setTipoMantenimiento(String tipoMantenimiento) { this.tipoMantenimiento = tipoMantenimiento; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
}