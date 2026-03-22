package com.example.chocolateria.controller;

import com.example.chocolateria.baseDeDatos.conexion;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

public class solicitudProduccionController {

    @FXML private ChoiceBox<String> cbProducto;
    @FXML private TextField txtCantidad;
    @FXML private DatePicker dpFechaSolicitud;
    @FXML private DatePicker dpFechaProduccion;
    @FXML private ChoiceBox<String> cbPrioridad;
    @FXML private ChoiceBox<String> cbResponsable;
    @FXML private TextArea txtObservaciones;

    private Map<String, String> mapaProductos = new HashMap<>(); // Cambiado a String
    private Map<String, Integer> mapaResponsables = new HashMap<>();
    conexion con = new conexion();

    @FXML
    public void initialize() {
        cbPrioridad.getItems().addAll("Alta", "Media", "Baja");
        cargarResponsables();
        cargarProductos();
    }

    private void cargarResponsables() {
        String sql = "SELECT id_empleado, nombre, apellido FROM tbl_empleado";

        try (Connection cn = con.establecerConexion();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id_empleado");
                String nombreCompleto = rs.getString("nombre") + " " + rs.getString("apellido");
                cbResponsable.getItems().add(nombreCompleto);
                mapaResponsables.put(nombreCompleto, id);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void cargarProductos() {
        String sql = "SELECT codigo, nombre FROM tbl_producto";

        try (Connection cn = con.establecerConexion();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String codigo = rs.getString("codigo"); // ahora es String
                String nombre = rs.getString("nombre");
                cbProducto.getItems().add(nombre);
                mapaProductos.put(nombre, codigo);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML private TextField txtCodigoProducto; // NUEVO

    @FXML
    public void guardar() {
        String codigoProducto = txtCodigoProducto.getText(); // Tomamos el código manual
        String nombreProducto = cbProducto.getValue();
        String nombreResponsable = cbResponsable.getValue();

        if (codigoProducto == null || codigoProducto.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Ingrese el código del producto").show();
            return;
        }

        if (nombreProducto == null) {
            new Alert(Alert.AlertType.WARNING, "Seleccione un producto").show();
            return;
        }
        if (nombreResponsable == null) {
            new Alert(Alert.AlertType.WARNING, "Seleccione un responsable").show();
            return;
        }

        int idResponsable = mapaResponsables.get(nombreResponsable);

        try {
            Connection cn = con.establecerConexion();

            String sql = "INSERT INTO tbl_solicitud_produccion " +
                    "(id_producto, cantidad, fecha_solicitud, fecha_produccion, prioridad, responsable, observaciones) " +
                    "VALUES (?,?,?,?,?,?,?)";

            PreparedStatement ps = cn.prepareStatement(sql);
            ps.setString(1, codigoProducto); // usamos el código del TextField
            ps.setInt(2, Integer.parseInt(txtCantidad.getText()));
            ps.setDate(3, java.sql.Date.valueOf(dpFechaSolicitud.getValue()));
            ps.setDate(4, java.sql.Date.valueOf(dpFechaProduccion.getValue()));
            ps.setString(5, cbPrioridad.getValue());
            ps.setInt(6, idResponsable);
            ps.setString(7, txtObservaciones.getText());

            ps.executeUpdate();

            new Alert(Alert.AlertType.INFORMATION, "Guardado correctamente").show();

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error: " + e.getMessage()).show();
        }
    }

    @FXML
    public void limpiar() {
        txtCantidad.clear();
        dpFechaSolicitud.setValue(null);
        dpFechaProduccion.setValue(null);
        txtObservaciones.clear();
        cbProducto.setValue(null);
        cbPrioridad.setValue(null);
        cbResponsable.setValue(null);
    }
}