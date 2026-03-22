package com.example.chocolateria.controller;

import com.example.chocolateria.baseDeDatos.conexion;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class empleadoController {

    @FXML private TextField txtNombre;
    @FXML private TextField txtApellido;
    @FXML private TextField txtCedula;
    @FXML private TextField txtTelefono;
    @FXML private ComboBox<String> cbTipoEmpleado;
    @FXML private ComboBox<String> cbRol;
    conexion con = new conexion();

    @FXML
    public void initialize() {
        cbTipoEmpleado.getItems().addAll("Supervisor", "Empleado", "Maestro Chocolatero");
        cbRol.getItems().addAll("Administrador", "Usuario", "Supervisor");
    }

    @FXML
    public void guardarEmpleado() {

        try {
            Connection cn = con.establecerConexion();

            String sql = "INSERT INTO tbl_empleado (nombre, apellido, tipo_empleado, cedula, telefono, rol) VALUES (?,?,?,?,?,?)";

            PreparedStatement ps = cn.prepareStatement(sql);

            ps.setString(1, txtNombre.getText());
            ps.setString(2, txtApellido.getText());
            ps.setString(3, cbTipoEmpleado.getValue());
            ps.setString(4, txtCedula.getText());
            ps.setString(5, txtTelefono.getText());
            ps.setString(6, cbRol.getValue());

            ps.executeUpdate();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Empleado registrado correctamente");
            alert.show();

            limpiar();

        }catch (Exception e) {
            e.printStackTrace();

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Error: " + e.getMessage());
            alert.show();
        }
    }

    public void limpiar() {
        txtNombre.clear();
        txtApellido.clear();
        txtCedula.clear();
        txtTelefono.clear();
        cbTipoEmpleado.setValue(null);
    }
}