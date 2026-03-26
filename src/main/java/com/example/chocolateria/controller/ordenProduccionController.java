package com.example.chocolateria.controller;

import com.example.chocolateria.baseDeDatos.conexion;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ordenProduccionController {

    @FXML private DatePicker dpFechaInicio;
    @FXML private DatePicker dpFechaOrden;
    @FXML private DatePicker dpFechaEntrega;
    @FXML private ChoiceBox<String> cbResponsable;
    @FXML private ChoiceBox<String> cbEstado;
    @FXML private TextField txtPrioridad;
    @FXML private TextField txtMateriales;
    @FXML private TextField txtObservaciones;
    @FXML private CheckBox chkBombones;
    @FXML private CheckBox chkTabletas;
    @FXML private CheckBox chkTrufas;

    @FXML
    public void initialize() {
        cargarResponsables();
        cbEstado.setItems(FXCollections.observableArrayList(
                "Pendiente", "En proceso", "Completada", "Cancelada"
        ));
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

    private String obtenerCategorias() {
        StringBuilder sb = new StringBuilder();
        if (chkBombones.isSelected()) sb.append("Bombones, ");
        if (chkTabletas.isSelected()) sb.append("Tabletas, ");
        if (chkTrufas.isSelected())   sb.append("Trufas, ");
        if (sb.length() > 0) sb.setLength(sb.length() - 2);
        return sb.toString();
    }

    @FXML
    private void guardarOrden() {
        if (dpFechaInicio.getValue() == null || dpFechaOrden.getValue() == null ||
                dpFechaEntrega.getValue() == null || cbResponsable.getValue() == null ||
                cbEstado.getValue() == null || txtPrioridad.getText().isBlank()) {
            new Alert(Alert.AlertType.WARNING, "Complete todos los campos obligatorios.").show();
            return;
        }

        String categorias = obtenerCategorias();
        if (categorias.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Seleccione al menos una categoría.").show();
            return;
        }

        conexion con = new conexion();
        try (Connection conn = con.establecerConexion();
             PreparedStatement ps = conn.prepareStatement(
                     "INSERT INTO tbl_orden_produccion(fecha_inicio, fecha_orden, fecha_entrega, " +
                             "responsable, estado, prioridad, categoria, materiales, observaciones) " +
                             "VALUES(?,?,?,?,?,?,?,?,?)")) {

            ps.setDate(1, java.sql.Date.valueOf(dpFechaInicio.getValue()));
            ps.setDate(2, java.sql.Date.valueOf(dpFechaOrden.getValue()));
            ps.setDate(3, java.sql.Date.valueOf(dpFechaEntrega.getValue()));
            ps.setString(4, cbResponsable.getValue());
            ps.setString(5, cbEstado.getValue());
            ps.setString(6, txtPrioridad.getText());
            ps.setString(7, categorias);
            ps.setString(8, txtMateriales.getText());
            ps.setString(9, txtObservaciones.getText());

            ps.executeUpdate();
            new Alert(Alert.AlertType.INFORMATION, "Orden guardada correctamente.").show();
            limpiarCampos();

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error al guardar: " + e.getMessage()).show();
        }
    }

    @FXML
    private void limpiarCampos() {
        dpFechaInicio.setValue(null);
        dpFechaOrden.setValue(null);
        dpFechaEntrega.setValue(null);
        cbResponsable.setValue(null);
        cbEstado.setValue(null);
        txtPrioridad.clear();
        txtMateriales.clear();
        txtObservaciones.clear();
        chkBombones.setSelected(false);
        chkTabletas.setSelected(false);
        chkTrufas.setSelected(false);
    }
}