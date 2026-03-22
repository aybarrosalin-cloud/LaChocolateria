package com.example.chocolateria.modelo;

public class reclamoModelo {
    private String codigo, cliente, tipo, estado, orden, descripcion, prioridad;

    public reclamoModelo(String codigo, String cliente, String tipo,
                         String estado, String orden, String descripcion, String prioridad) {
        this.codigo = codigo;
        this.cliente = cliente;
        this.tipo = tipo;
        this.estado = estado;
        this.orden = orden;
        this.descripcion = descripcion;
        this.prioridad = prioridad;
    }
}