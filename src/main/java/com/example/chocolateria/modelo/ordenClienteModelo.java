package com.example.chocolateria.modelo;

import javafx.beans.property.*;

public class ordenClienteModelo {

    private IntegerProperty idOrden;
    private StringProperty cliente;
    private StringProperty fecha;
    private StringProperty producto;
    private StringProperty categoria;
    private IntegerProperty cantidad;
    private StringProperty estado;
    private StringProperty metodoPago;

    public ordenClienteModelo(int idOrden, String cliente, String fecha, String producto,
                              String categoria, int cantidad, String estado, String metodoPago) {
        this.idOrden = new SimpleIntegerProperty(idOrden);
        this.cliente = new SimpleStringProperty(cliente);
        this.fecha = new SimpleStringProperty(fecha);
        this.producto = new SimpleStringProperty(producto);
        this.categoria = new SimpleStringProperty(categoria);
        this.cantidad = new SimpleIntegerProperty(cantidad);
        this.estado = new SimpleStringProperty(estado);
        this.metodoPago = new SimpleStringProperty(metodoPago);
    }

    public int getIdOrden() { return idOrden.get(); }
    public String getCliente() { return cliente.get(); }
    public String getFecha() { return fecha.get(); }
    public String getProducto() { return producto.get(); }
    public String getCategoria() { return categoria.get(); }
    public int getCantidad() { return cantidad.get(); }
    public String getEstado() { return estado.get(); }
    public String getMetodoPago() { return metodoPago.get(); }

    public IntegerProperty idOrdenProperty() { return idOrden; }
    public StringProperty clienteProperty() { return cliente; }
    public StringProperty fechaProperty() { return fecha; }
    public StringProperty productoProperty() { return producto; }
    public StringProperty categoriaProperty() { return categoria; }
    public IntegerProperty cantidadProperty() { return cantidad; }
    public StringProperty estadoProperty() { return estado; }
    public StringProperty metodoPagoProperty() { return metodoPago; }
}