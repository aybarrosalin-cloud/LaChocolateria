package com.example.chocolateria.modelo;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class productoModelo {

    private StringProperty codigo;
    private StringProperty nombre;
    private StringProperty precioUnitario;
    private StringProperty precioMayor;
    private StringProperty descripcion;
    private StringProperty unidadMedida;
    private StringProperty categoria;
    private StringProperty tipo;

    public productoModelo(String codigo, String nombre, String precioUnitario, String precioMayor,
                          String descripcion, String unidadMedida, String categoria, String tipo) {
        this.codigo = new SimpleStringProperty(codigo);
        this.nombre = new SimpleStringProperty(nombre);
        this.precioUnitario = new SimpleStringProperty(precioUnitario);
        this.precioMayor = new SimpleStringProperty(precioMayor);
        this.descripcion = new SimpleStringProperty(descripcion);
        this.unidadMedida = new SimpleStringProperty(unidadMedida);
        this.categoria = new SimpleStringProperty(categoria);
        this.tipo = new SimpleStringProperty(tipo);
    }

    // Getters
    public String getCodigo() { return codigo.get(); }
    public String getNombre() { return nombre.get(); }
    public String getPrecioUnitario() { return precioUnitario.get(); }
    public String getPrecioMayor() { return precioMayor.get(); }
    public String getDescripcion() { return descripcion.get(); }
    public String getUnidadMedida() { return unidadMedida.get(); }
    public String getCategoria() { return categoria.get(); }
    public String getTipo() { return tipo.get(); }

    // Property methods (para la tabla si necesitas)
    public StringProperty codigoProperty() { return codigo; }
    public StringProperty nombreProperty() { return nombre; }
    public StringProperty precioUnitarioProperty() { return precioUnitario; }
    public StringProperty precioMayorProperty() { return precioMayor; }
    public StringProperty descripcionProperty() { return descripcion; }
    public StringProperty unidadMedidaProperty() { return unidadMedida; }
    public StringProperty categoriaProperty() { return categoria; }
    public StringProperty tipoProperty() { return tipo; }
}