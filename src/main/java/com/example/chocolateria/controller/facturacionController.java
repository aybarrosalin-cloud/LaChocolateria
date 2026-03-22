package com.example.chocolateria.controller;

import com.example.chocolateria.baseDeDatos.conexion;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class facturacionController {

    @FXML private TextField txtNumeroFactura;
    @FXML private DatePicker dpFechaFactura;
    @FXML private TextField txtRncCliente;
    @FXML private TextField txtProducto;
    @FXML private TextField txtCantidad;
    @FXML private TextField txtPrecioUnitario;
    @FXML private TextField txtSubtotal;
    @FXML private TextField txtItbis;
    @FXML private TextField txtTotal;

    @FXML
    private void calcular() {
        try {
            int cantidad          = Integer.parseInt(txtCantidad.getText());
            double precioUnitario = Double.parseDouble(txtPrecioUnitario.getText());

            double subtotal = cantidad * precioUnitario;
            double itbis    = subtotal * 0.18;
            double total    = subtotal + itbis;

            txtSubtotal.setText(String.format("%.2f", subtotal));
            txtItbis.setText(String.format("%.2f", itbis));
            txtTotal.setText(String.format("%.2f", total));

        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.WARNING, "Cantidad y precio deben ser números válidos.").show();
        }
    }

    @FXML
    private void guardarFactura() {
        if (dpFechaFactura.getValue() == null ||
                txtNumeroFactura.getText().isBlank() ||
                txtRncCliente.getText().isBlank() ||
                txtProducto.getText().isBlank() ||
                txtCantidad.getText().isBlank() ||
                txtPrecioUnitario.getText().isBlank() ||
                txtTotal.getText().isBlank()) {

            new Alert(Alert.AlertType.WARNING, "Complete todos los campos y calcule el total primero.").show();
            return;
        }

        try {
            conexion con = new conexion();
            try (Connection conn = con.establecerConexion();
                 PreparedStatement ps = conn.prepareStatement(
                         "INSERT INTO tbl_facturacion(numero_factura, fecha_factura, rnc_cliente, " +
                                 "producto, cantidad, precio_unitario, subtotal, itbis, total) " +
                                 "VALUES(?,?,?,?,?,?,?,?,?)")) {

                ps.setString(1, txtNumeroFactura.getText());
                ps.setDate(2, java.sql.Date.valueOf(dpFechaFactura.getValue()));
                ps.setString(3, txtRncCliente.getText());
                ps.setString(4, txtProducto.getText());
                ps.setInt(5, Integer.parseInt(txtCantidad.getText()));
                ps.setDouble(6, Double.parseDouble(txtPrecioUnitario.getText()));
                ps.setDouble(7, Double.parseDouble(txtSubtotal.getText()));
                ps.setDouble(8, Double.parseDouble(txtItbis.getText()));
                ps.setDouble(9, Double.parseDouble(txtTotal.getText()));

                ps.executeUpdate();
                new Alert(Alert.AlertType.INFORMATION, "Factura guardada correctamente.").show();
                limpiarCampos();
            }

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error al guardar: " + e.getMessage()).show();
        }
    }

    @FXML
    private void limpiarCampos() {
        txtNumeroFactura.clear();
        dpFechaFactura.setValue(null);
        txtRncCliente.clear();
        txtProducto.clear();
        txtCantidad.clear();
        txtPrecioUnitario.clear();
        txtSubtotal.clear();
        txtItbis.clear();
        txtTotal.clear();
    }
}