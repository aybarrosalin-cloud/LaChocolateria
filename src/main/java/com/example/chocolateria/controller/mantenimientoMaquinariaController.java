
package com.example.chocolateria.controller;

import com.example.chocolateria.baseDeDatos.conexion;
import com.example.chocolateria.modelo.mantenimientoMaquinariaModelo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
        import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;

public class mantenimientoMaquinariaController {

    @FXML
    private DatePicker dpFechaMantenimiento;

    @FXML
    private ChoiceBox<String> cbMaquina;

    @FXML
    private ChoiceBox<String> cbTecnico;

    @FXML
    private TextField txtCosto;

    @FXML
    private ChoiceBox<String> cbEstadoMaquina;

    @FXML
    private ChoiceBox<String> cbTipoMantenimiento;

    @FXML
    private TextArea taObservaciones;

    private ObservableList<String> listaMaquinas = FXCollections.observableArrayList();
    private ObservableList<String> listaTecnicos = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        cargarMaquinas();
        cargarTecnicos();

        cbEstadoMaquina.setItems(FXCollections.observableArrayList("Operativa", "En reparación", "Fuera de servicio"));
        cbTipoMantenimiento.setItems(FXCollections.observableArrayList("Preventivo", "Correctivo"));
    }

    private void cargarMaquinas() {
        conexion con = new conexion();
        try (Connection conn = con.establecerConexion();
             PreparedStatement ps = conn.prepareStatement("SELECT nombre FROM tbl_maquinaria")) {

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                listaMaquinas.add(rs.getString("nombre"));
            }
            cbMaquina.setItems(listaMaquinas);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void cargarTecnicos() {
        conexion con = new conexion();
        try (Connection conn = con.establecerConexion();
             PreparedStatement ps = conn.prepareStatement("SELECT nombre FROM tbl_tecnico")) {

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                listaTecnicos.add(rs.getString("nombre"));
            }
            cbTecnico.setItems(listaTecnicos);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void guardarMantenimiento() {
        conexion con = new conexion();
        try (Connection conn = con.establecerConexion()) {

            LocalDate fecha = dpFechaMantenimiento.getValue();
            String maquina = cbMaquina.getValue();
            String tecnico = cbTecnico.getValue();
            double costo = Double.parseDouble(txtCosto.getText());
            String estado = cbEstadoMaquina.getValue();
            String tipo = cbTipoMantenimiento.getValue();
            String observaciones = taObservaciones.getText();

            mantenimientoMaquinariaModelo mantenimiento = new mantenimientoMaquinariaModelo(
                    0, fecha, maquina, tecnico, costo, estado, tipo, observaciones
            );

            try (PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO tbl_mantenimiento_maquinaria(fecha_mantenimiento, maquina, tecnico, costo, estado_maquina, tipo_mantenimiento, observaciones) VALUES(?,?,?,?,?,?,?)")) {

                ps.setDate(1, java.sql.Date.valueOf(mantenimiento.getFechaMantenimiento()));
                ps.setString(2, mantenimiento.getMaquina());
                ps.setString(3, mantenimiento.getTecnico());
                ps.setDouble(4, mantenimiento.getCosto());
                ps.setString(5, mantenimiento.getEstadoMaquina());
                ps.setString(6, mantenimiento.getTipoMantenimiento());
                ps.setString(7, mantenimiento.getObservaciones());

                ps.executeUpdate();
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Mantenimiento guardado correctamente");
                alert.show();
            }

        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error al guardar el mantenimiento");
            alert.show();
        }
    }

        public void LimpiarCampos(ActionEvent actionEvent) {
            dpFechaMantenimiento.setValue(null);
            cbMaquina.setValue(null);
            cbTecnico.setValue(null);
            txtCosto.clear();
            cbEstadoMaquina.setValue(null);
            cbTipoMantenimiento.setValue(null);
            taObservaciones.clear();
        }
    }
