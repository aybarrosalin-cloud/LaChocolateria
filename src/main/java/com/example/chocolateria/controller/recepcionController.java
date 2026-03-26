package com.example.chocolateria.controller;

import com.example.chocolateria.baseDeDatos.conexion;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class recepcionController {

    @FXML private DatePicker dpFechaRecepcion;
    @FXML private TextField txtRncProveedor;
    @FXML private TextField txtProductos;
    @FXML private TextField txtMontoTotal;
    @FXML private TextField txtMontoPorProducto;
    @FXML private TextField txtObservaciones;
    @FXML private TextField txtNumeroOrden;
    @FXML private TextField txtPrecioUnitario;
    @FXML private TextField txtCantidadRecibida;

    @FXML
    private void guardarRecepcion() {
        if (dpFechaRecepcion.getValue() == null ||
                txtRncProveedor.getText().isBlank() ||
                txtProductos.getText().isBlank() ||
                txtMontoTotal.getText().isBlank() ||
                txtMontoPorProducto.getText().isBlank() ||
                txtNumeroOrden.getText().isBlank() ||
                txtPrecioUnitario.getText().isBlank() ||
                txtCantidadRecibida.getText().isBlank()) {

            new Alert(Alert.AlertType.WARNING, "Complete todos los campos obligatorios.").show();
            return;
        }

        try {
            double montoTotal       = Double.parseDouble(txtMontoTotal.getText());
            double montoPorProducto = Double.parseDouble(txtMontoPorProducto.getText());
            double precioUnitario   = Double.parseDouble(txtPrecioUnitario.getText());

            conexion con = new conexion();
            try (Connection conn = con.establecerConexion();
                 PreparedStatement ps = conn.prepareStatement(
                         "INSERT INTO tbl_recepcion(rnc_proveedor, productos, monto_total, fecha_recepcion, " +
                                 "monto_por_producto, observaciones, numero_orden, precio_unitario, cantidad_recibida) " +
                                 "VALUES(?,?,?,?,?,?,?,?,?)")) {

                ps.setString(1, txtRncProveedor.getText());
                ps.setString(2, txtProductos.getText());
                ps.setDouble(3, montoTotal);
                ps.setDate(4, java.sql.Date.valueOf(dpFechaRecepcion.getValue()));
                ps.setDouble(5, montoPorProducto);
                ps.setString(6, txtObservaciones.getText());
                ps.setString(7, txtNumeroOrden.getText());
                ps.setDouble(8, precioUnitario);
                ps.setString(9, txtCantidadRecibida.getText());

                ps.executeUpdate();
                new Alert(Alert.AlertType.INFORMATION, "Recepción guardada correctamente.").show();
                limpiarCampos();
            }

        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.WARNING, "Los campos numéricos deben contener números válidos.").show();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error al guardar: " + e.getMessage()).show();
        }
    }

    @FXML
    private void limpiarCampos() {
        dpFechaRecepcion.setValue(null);
        txtRncProveedor.clear();
        txtProductos.clear();
        txtMontoTotal.clear();
        txtMontoPorProducto.clear();
        txtObservaciones.clear();
        txtNumeroOrden.clear();
        txtPrecioUnitario.clear();
        txtCantidadRecibida.clear();
    }
}