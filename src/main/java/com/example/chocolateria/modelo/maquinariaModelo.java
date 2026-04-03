package com.example.chocolateria.modelo;

import javafx.beans.property.*;
import java.time.LocalDate;

public class maquinariaModelo {

    private final SimpleIntegerProperty            idMaquinaria;
    private final SimpleStringProperty             nombre;
    private final SimpleStringProperty             tipo;
    private final SimpleStringProperty             marcaModelo;
    private final SimpleStringProperty             numeroSerie;
    private final SimpleObjectProperty<LocalDate>  fechaAdquisicion;
    private final SimpleStringProperty             estado;
    private final SimpleIntegerProperty            idResponsable;
    private final SimpleStringProperty             responsable;

    public maquinariaModelo(int idMaquinaria, String nombre, String tipo,
                            String marcaModelo, String numeroSerie,
                            LocalDate fechaAdquisicion, String estado,
                            int idResponsable, String responsable) {
        this.idMaquinaria    = new SimpleIntegerProperty(idMaquinaria);
        this.nombre          = new SimpleStringProperty(nombre);
        this.tipo            = new SimpleStringProperty(tipo);
        this.marcaModelo     = new SimpleStringProperty(marcaModelo);
        this.numeroSerie     = new SimpleStringProperty(numeroSerie);
        this.fechaAdquisicion= new SimpleObjectProperty<>(fechaAdquisicion);
        this.estado          = new SimpleStringProperty(estado);
        this.idResponsable   = new SimpleIntegerProperty(idResponsable);
        this.responsable     = new SimpleStringProperty(responsable);
    }

    public int       getIdMaquinaria()    { return idMaquinaria.get(); }
    public String    getNombre()          { return nombre.get(); }
    public String    getTipo()            { return tipo.get(); }
    public String    getMarcaModelo()     { return marcaModelo.get(); }
    public String    getNumeroSerie()     { return numeroSerie.get(); }
    public LocalDate getFechaAdquisicion(){ return fechaAdquisicion.get(); }
    public String    getEstado()          { return estado.get(); }
    public int       getIdResponsable()   { return idResponsable.get(); }
    public String    getResponsable()     { return responsable.get(); }

    public void setNombre(String v)           { nombre.set(v); }
    public void setTipo(String v)             { tipo.set(v); }
    public void setMarcaModelo(String v)      { marcaModelo.set(v); }
    public void setNumeroSerie(String v)      { numeroSerie.set(v); }
    public void setFechaAdquisicion(LocalDate v){ fechaAdquisicion.set(v); }
    public void setEstado(String v)           { estado.set(v); }
    public void setIdResponsable(int v)       { idResponsable.set(v); }
    public void setResponsable(String v)      { responsable.set(v); }

    public SimpleIntegerProperty            idMaquinariaProperty()    { return idMaquinaria; }
    public SimpleStringProperty             nombreProperty()          { return nombre; }
    public SimpleStringProperty             tipoProperty()            { return tipo; }
    public SimpleStringProperty             marcaModeloProperty()     { return marcaModelo; }
    public SimpleStringProperty             numeroSerieProperty()     { return numeroSerie; }
    public SimpleObjectProperty<LocalDate>  fechaAdquisicionProperty(){ return fechaAdquisicion; }
    public SimpleStringProperty             estadoProperty()          { return estado; }
    public SimpleIntegerProperty            idResponsableProperty()   { return idResponsable; }
    public SimpleStringProperty             responsableProperty()     { return responsable; }
}
