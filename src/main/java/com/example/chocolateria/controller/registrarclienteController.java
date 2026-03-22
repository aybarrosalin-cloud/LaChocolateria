package com.example.chocolateria.controller;

import com.example.chocolateria.baseDeDatos.conexion;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.*;

public class registrarclienteController {

    @FXML private TextField txtIdCliente;
    @FXML private TextField txtNombre;
    @FXML private TextField txtApellido;
    @FXML private TextField txtCedula;
    @FXML private TextField txtEmail;
    @FXML private TextField txtTelefono;
    @FXML private TextField txtDireccion;

    conexion con = new conexion();

    @FXML
    public void guardarCliente() {

        try {
            Connection cn = con.establecerConexion();

            String sql = "INSERT INTO tbl_cliente (nombre, apellido, email, telefono) VALUES (?,?,?,?)";

            PreparedStatement ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            ps.setString(1, txtNombre.getText());
            ps.setString(2, txtApellido.getText());
            ps.setString(3, txtEmail.getText());
            ps.setString(4, txtTelefono.getText());

            ps.executeUpdate();


            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                txtIdCliente.setText(String.valueOf(rs.getInt(1)));
            }

            new Alert(Alert.AlertType.INFORMATION, "Cliente registrado").show();

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error: " + e.getMessage()).show();
        }
    }
}