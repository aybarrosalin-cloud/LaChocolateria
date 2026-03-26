package com.example.chocolateria.controller;

import com.example.chocolateria.baseDeDatos.conexion;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ventaController {

    @FXML private TextField txtNumeroFactura;
    @FXML private DatePicker dpFechaFactura;
    @FXML private TextField txtNombreCliente;
    @FXML private TextField txtProducto;
    @FXML private TextField txtCantidad;
    @FXML private TextField txtPrecioUnitario;
    @FXML private ChoiceBox<String> cbResponsable;
    @FXML private TextField txtSubtotal;
    @FXML private TextField txtTotal;
    @FXML private TextField txtAbono;
    @FXML private TextField txtSaldo;
    @FXML private TextField txtEstadoPago;

    @FXML
    public void initialize() {
        cargarResponsables();
    }

    private void cargarResponsables() {
        ObservableList<String> lista = FXCollections.observableArrayList();
        conexion con = new conexion();
        try (Connection conn = con.establecerConexion();
             PreparedStatement ps = conn.prepareStatement("SELECT nombre FROM tbl_responsable")) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(rs.getString("nombre"));
            cbResponsable.setItems(lista);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void calcular() {
        try {
            int cantidad          = Integer.parseInt(txtCantidad.getText());
            double precioUnitario = Double.parseDouble(txtPrecioUnitario.getText());

            double subtotal = cantidad * precioUnitario;
            double itbis    = subtotal * 0.18;
            double total    = subtotal + itbis;

            txtSubtotal.setText(String.format("%.2f", subtotal));
            txtTotal.setText(String.format("%.2f", total));

            // Si ya hay abono recalcular saldo
            if (!txtAbono.getText().isBlank()) {
                calcularSaldo(total);
            }

        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.WARNING, "Cantidad y precio deben ser números válidos.").show();
        }
    }

    private void calcularSaldo(double total) {
        try {
            double abono = Double.parseDouble(txtAbono.getText());

            if (abono > total) {
                new Alert(Alert.AlertType.WARNING, "El abono no puede ser mayor al total.").show();
                txtAbono.clear();
                return;
            }

            double saldo = total - abono;
            txtSaldo.setText(String.format("%.2f", saldo));

            if (saldo == 0) {
                txtEstadoPago.setText("Pagado");
            } else if (abono == 0) {
                txtEstadoPago.setText("Pendiente");
            } else {
                txtEstadoPago.setText("Abonado");
            }

        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.WARNING, "El abono debe ser un número válido.").show();
        }
    }

    @FXML
    private void guardarVenta() {
        if (dpFechaFactura.getValue() == null ||
                txtNumeroFactura.getText().isBlank() ||
                txtNombreCliente.getText().isBlank() ||
                txtProducto.getText().isBlank() ||
                txtCantidad.getText().isBlank() ||
                txtPrecioUnitario.getText().isBlank() ||
                txtTotal.getText().isBlank() ||
                txtAbono.getText().isBlank() ||
                cbResponsable.getValue() == null) {

            new Alert(Alert.AlertType.WARNING, "Complete todos los campos y calcule primero.").show();
            return;
        }

        try {
            double total = Double.parseDouble(txtTotal.getText());
            calcularSaldo(total);

            conexion con = new conexion();
            try (Connection conn = con.establecerConexion();
                 PreparedStatement ps = conn.prepareStatement(
                         "INSERT INTO tbl_venta(numero_factura, fecha_venta, nombre_cliente, " +
                                 "producto, cantidad, precio_unitario, subtotal, total, abono, saldo, " +
                                 "estado_pago, responsable) VALUES(?,?,?,?,?,?,?,?,?,?,?,?)")) {

                ps.setString(1, txtNumeroFactura.getText());
                ps.setDate(2, java.sql.Date.valueOf(dpFechaFactura.getValue()));
                ps.setString(3, txtNombreCliente.getText());
                ps.setString(4, txtProducto.getText());
                ps.setInt(5, Integer.parseInt(txtCantidad.getText()));
                ps.setDouble(6, Double.parseDouble(txtPrecioUnitario.getText()));
                ps.setDouble(7, Double.parseDouble(txtSubtotal.getText()));
                ps.setDouble(8, total);
                ps.setDouble(9, Double.parseDouble(txtAbono.getText()));
                ps.setDouble(10, Double.parseDouble(txtSaldo.getText()));
                ps.setString(11, txtEstadoPago.getText());
                ps.setString(12, cbResponsable.getValue());

                ps.executeUpdate();
                new Alert(Alert.AlertType.INFORMATION, "Venta registrada correctamente.").show();
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
        txtNombreCliente.clear();
        txtProducto.clear();
        txtCantidad.clear();
        txtPrecioUnitario.clear();
        cbResponsable.setValue(null);
        txtSubtotal.clear();
        txtTotal.clear();
        txtAbono.clear();
        txtSaldo.clear();
        txtEstadoPago.clear();
    }
}