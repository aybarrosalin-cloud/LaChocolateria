package com.example.chocolateria.controller;

import com.example.chocolateria.baseDeDatos.conexion;
import com.example.chocolateria.modelo.ordenClienteModelo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javax.swing.*;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class ordenClienteController implements Initializable {
    private Connection con;

    @FXML private TextField txtCodigo;
    @FXML private ComboBox<String> cmbCliente;
    @FXML private DatePicker dpFecha;
    @FXML private ComboBox<String> cmbProducto;
    @FXML private ComboBox<String> cmbCategoria;
    @FXML private Spinner<Integer> spCantidad;
    @FXML private ChoiceBox<String> cbEstado;
    @FXML private ChoiceBox<String> cbMetodoPago;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        conexion conexion = new conexion();
        con = conexion.establecerConexion();

        cbEstado.getItems().addAll("Pendiente", "Confirmada", "Completada", "Cancelada");
        cbMetodoPago.getItems().addAll("Efectivo", "Tarjeta", "Transferencia");
        cmbCategoria.getItems().addAll("Leche", "Negro", "Blanco", "Otro");

        spCantidad.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 1000, 1));

        cmbCliente.setItems(llenarCombo("SELECT nombre_apellido FROM tbl_cliente", "nombre_apellido"));
        cmbProducto.setItems(llenarCombo("SELECT nombre FROM tbl_producto", "nombre"));
    }

    public ObservableList<String> llenarCombo(String sql, String columna) {
        ObservableList<String> lista = FXCollections.observableArrayList();
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(rs.getString(columna));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    @FXML
    private void guardar(ActionEvent event) {
        try {
            String codigo = txtCodigo.getText();
            String cliente = cmbCliente.getValue();
            String producto = cmbProducto.getValue();
            String categoria = cmbCategoria.getValue();
            String estado = cbEstado.getValue();
            String metodoPago = cbMetodoPago.getValue();
            int cantidad = spCantidad.getValue();

            conexion conexion = new conexion();
            Connection con = conexion.establecerConexion();

            String sql = "INSERT INTO tbl_orden_cliente (id_orden, cliente, fecha, producto, categoria, cantidad, estado, metodo_pago) VALUES (?,?,?,?,?,?,?,?)";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, Integer.parseInt(codigo));
            ps.setString(2, cliente);
            ps.setDate(3, java.sql.Date.valueOf(dpFecha.getValue()));
            ps.setString(4, producto);
            ps.setString(5, categoria);
            ps.setInt(6, cantidad);
            ps.setString(7, estado);
            ps.setString(8, metodoPago);
            ps.executeUpdate();

            JOptionPane.showMessageDialog(null, "Orden guardada correctamente");
            limpiarCampos();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }

    public void fnbuscar(ActionEvent event) {
        String codigo = txtCodigo.getText().trim();
        String sql = "SELECT * FROM tbl_orden_cliente WHERE id_orden='" + codigo + "'";
        buscarDatos(sql);
    }

    public void fnEditar(ActionEvent event) {
        String codigo = txtCodigo.getText().trim();
        String cliente = cmbCliente.getValue();
        String producto = cmbProducto.getValue();
        String categoria = cmbCategoria.getValue();
        String estado = cbEstado.getValue();
        String metodoPago = cbMetodoPago.getValue();
        int cantidad = spCantidad.getValue();

        String sql = "UPDATE tbl_orden_cliente SET cliente='" + cliente + "', producto='" + producto +
                "', categoria='" + categoria + "', cantidad=" + cantidad + ", estado='" + estado +
                "', metodo_pago='" + metodoPago + "' WHERE id_orden='" + codigo + "'";
        EjecutarSQL(sql);
    }

    private void buscarDatos(String sql) {
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                txtCodigo.setText(String.valueOf(rs.getInt("id_orden")));
                cmbCliente.getSelectionModel().select(rs.getString("cliente"));
                dpFecha.setValue(rs.getDate("fecha").toLocalDate());
                cmbProducto.getSelectionModel().select(rs.getString("producto"));
                cmbCategoria.getSelectionModel().select(rs.getString("categoria"));
                spCantidad.getValueFactory().setValue(rs.getInt("cantidad"));
                cbEstado.getSelectionModel().select(rs.getString("estado"));
                cbMetodoPago.getSelectionModel().select(rs.getString("metodo_pago"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void EjecutarSQL(String sql) {
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            int result = ps.executeUpdate();
            if (result == 1) {
                JOptionPane.showMessageDialog(null, "Acción realizada correctamente");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.toString());
        }
    }

    @FXML
    private void limpiarCampos() {
        txtCodigo.clear();
        cmbCliente.getSelectionModel().clearSelection();
        dpFecha.setValue(null);
        cmbProducto.getSelectionModel().clearSelection();
        cmbCategoria.getSelectionModel().clearSelection();
        spCantidad.getValueFactory().setValue(1);
        cbEstado.getSelectionModel().clearSelection();
        cbMetodoPago.getSelectionModel().clearSelection();
    }
}