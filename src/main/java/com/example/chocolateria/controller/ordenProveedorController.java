package com.example.chocolateria.controller;

import com.example.chocolateria.baseDeDatos.conexion;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javax.swing.*;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class ordenProveedorController implements Initializable {

    private Connection con;

    // FORMULARIO
    @FXML private TextField txtCodigo;
    @FXML private TextField txtRNC;
    @FXML private DatePicker dpFecha;
    @FXML private ComboBox<String> cmbPrioridad;
    @FXML private ChoiceBox<String> chCantidad;
    @FXML private ComboBox<String> cmbEstadoPago;
    @FXML private TextField txtMonto;
    @FXML private TextArea txtDescripcion;
    @FXML private ComboBox<String> cmbProducto;

    // CHECKBOX
    @FXML private CheckBox chkProgramada;
    @FXML private CheckBox chkUrgente;
    @FXML private CheckBox chkEmergencia;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        conexion conexion = new conexion();
        con = conexion.establecerConexion();

        cmbPrioridad.setItems(FXCollections.observableArrayList("Alta", "Media", "Baja"));
        cmbEstadoPago.setItems(FXCollections.observableArrayList("Pagado", "Pendiente"));
        chCantidad.setItems(FXCollections.observableArrayList("1-10", "11-50", "50+"));

        cmbProducto.setItems(llenarProductos());

        actualizarDatos();
    }

    public ObservableList<String> llenarProductos() {
        ObservableList<String> lista = FXCollections.observableArrayList();
        String sql = "SELECT nombre FROM tbl_producto";

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                lista.add(rs.getString("nombre"));
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
            String rnc = txtRNC.getText();
            LocalDate fecha = dpFecha.getValue();
            String prioridad = cmbPrioridad.getValue();
            String cantidad = chCantidad.getValue();
            String estadoPago = cmbEstadoPago.getValue();
            String monto = txtMonto.getText();
            String descripcion = txtDescripcion.getText();
            String producto = cmbProducto.getValue();

            // CATEGORIA (CHECKBOX)
            String categoria = "";
            if (chkProgramada.isSelected()) categoria += "Programada ";
            if (chkUrgente.isSelected()) categoria += "Urgente ";
            if (chkEmergencia.isSelected()) categoria += "Emergencia ";

            String sql = "INSERT INTO tbl_orden_proveedor VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, codigo);
            ps.setString(2, rnc);
            ps.setDate(3, Date.valueOf(fecha));
            ps.setString(4, prioridad);
            ps.setString(5, cantidad);
            ps.setString(6, estadoPago);
            ps.setDouble(7, Double.parseDouble(monto));
            ps.setString(8, categoria);
            ps.setString(9, descripcion);
            ps.setString(10, producto);

            ps.executeUpdate();

            JOptionPane.showMessageDialog(null, "Orden guardada");
            limpiarCampos();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void fnbuscar(ActionEvent event) {
        String codigo = txtCodigo.getText().trim();
        String sql = "SELECT * FROM tbl_orden_proveedor WHERE codigo='" + codigo + "'";
        buscarDatos(sql);
    }

    private void buscarDatos(String sql) {

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                txtCodigo.setText(rs.getString("codigo"));
                txtRNC.setText(rs.getString("rnc_proveedor"));
                dpFecha.setValue(rs.getDate("fecha_requerida").toLocalDate());
                cmbPrioridad.setValue(rs.getString("prioridad"));
                chCantidad.setValue(rs.getString("cantidad_pedido"));
                cmbEstadoPago.setValue(rs.getString("estado_pago"));
                txtMonto.setText(rs.getString("monto_total"));
                txtDescripcion.setText(rs.getString("descripcion"));
                cmbProducto.setValue(rs.getString("producto"));

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void fnEditar(ActionEvent event) {

        String codigo = txtCodigo.getText().trim();

        String sql = "UPDATE tbl_orden_proveedor SET " +
                "rnc_proveedor='" + txtRNC.getText() + "', " +
                "fecha_requerida='" + dpFecha.getValue() + "', " +
                "prioridad='" + cmbPrioridad.getValue() + "', " +
                "cantidad_pedido='" + chCantidad.getValue() + "', " +
                "estado_pago='" + cmbEstadoPago.getValue() + "', " +
                "monto_total='" + txtMonto.getText() + "', " +
                "descripcion='" + txtDescripcion.getText() + "', " +
                "producto='" + cmbProducto.getValue() + "' " +
                "WHERE codigo='" + codigo + "'";

        EjecutarSQL(sql);
        actualizarDatos();
    }

    public void EjecutarSQL(String sql){
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            int result = ps.executeUpdate();
            if (result == 1) {
                JOptionPane.showMessageDialog(null, "Accion realizada correctamente");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "error " + e.toString());
        }
    }

    public void actualizarDatos() {
        // opcional
    }

    @FXML
    private void limpiarCampos() {
        txtCodigo.clear();
        txtRNC.clear();
        txtMonto.clear();
        txtDescripcion.clear();
        dpFecha.setValue(null);
        cmbPrioridad.setValue(null);
        cmbEstadoPago.setValue(null);
        chCantidad.setValue(null);
        cmbProducto.setValue(null);

        chkProgramada.setSelected(false);
        chkUrgente.setSelected(false);
        chkEmergencia.setSelected(false);
    }
}