package com.example.chocolateria.controller;

import com.example.chocolateria.baseDeDatos.conexion;
import com.example.chocolateria.modelo.productoModelo;
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

public class productoController implements Initializable {
    private Connection con;

    // FORMULARIO
    @FXML private TextField txtCodigo;
    @FXML private TextField txtNombre;
    @FXML private TextField txtPrecioUnitario;
    @FXML private TextField txtPrecioMayor;
    @FXML private TextArea txtDescripcion;
    @FXML private TextField txtUnidadMedida;

    // CHECKBOX CATEGORIA
    @FXML private CheckBox chkBombones;
    @FXML private CheckBox chkTabletas;
    @FXML private CheckBox chkTrufas;
    @FXML private CheckBox chkCacao;
    @FXML private CheckBox chkManteca;

    // CHECKBOX TIPO
    @FXML private CheckBox chkLeche;
    @FXML private CheckBox chkNegro;
    @FXML private CheckBox chkBlanco;
    @FXML private CheckBox chkOtro;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        conexion conexion = new conexion();
        con = conexion.establecerConexion();
        actualizarDatos();
    }

    public void actualizarDatos() {
//        // tener las columnas con el modelo
//        colNombre.setCellValueFactory(data -> data.getValue().nombreProperty());
//        colRnc.setCellValueFactory(data -> data.getValue().rncProperty());
//        colTelefono.setCellValueFactory(data -> data.getValue().telefonoProperty());
//        colCorreo.setCellValueFactory(data -> data.getValue().correoProperty());
//        colCiudad.setCellValueFactory(data -> data.getValue().ciudadProperty());
//
//        // conectar tabla con la lista
//        tablaSuplidores.setItems(listaObservable);
    }

    @FXML
    private void guardarProducto(ActionEvent event) {
        try {
            String codigo = txtCodigo.getText();
            String nombre = txtNombre.getText();
            String precioUnitario = txtPrecioUnitario.getText();
            String precioMayor = txtPrecioMayor.getText();
            String descripcion = txtDescripcion.getText();
            String unidadMedida = txtUnidadMedida.getText();

            // CATEGORIA
            String categoria = "";
            if (chkBombones.isSelected()) categoria += "Bombones,";
            if (chkTabletas.isSelected()) categoria += "Tabletas,";
            if (chkTrufas.isSelected()) categoria += "Trufas,";
            if (chkCacao.isSelected()) categoria += "Cacao,";
            if (chkManteca.isSelected()) categoria += "Manteca,";
            if(!categoria.isEmpty()) categoria = categoria.substring(0, categoria.length()-1); // quitar coma final

            // TIPO
            String tipo = "";
            if (chkLeche.isSelected()) tipo += "Leche,";
            if (chkNegro.isSelected()) tipo += "Negro,";
            if (chkBlanco.isSelected()) tipo += "Blanco,";
            if (chkOtro.isSelected()) tipo += "Otro,";
            if(!tipo.isEmpty()) tipo = tipo.substring(0, tipo.length()-1);

            String sql = "INSERT INTO tbl_producto (codigo, nombre, precio_unitario, precio_mayor, descripcion, unidad_medida, categoria, tipo) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, codigo);
            ps.setString(2, nombre);
            ps.setString(3, precioUnitario);
            ps.setDouble(4, Double.parseDouble(precioMayor));
            ps.setString(5, descripcion);
            ps.setString(6, unidadMedida);
            ps.setString(7, categoria);
            ps.setString(8, tipo);
            ps.executeUpdate();

            JOptionPane.showMessageDialog(null, "Producto guardado correctamente");
            limpiarCampos();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void fnbuscar(ActionEvent event) {
        String codigo = txtCodigo.getText().trim();
        String sql = "SELECT * FROM tbl_producto WHERE codigo='" + codigo + "'";
        buscarDatos(sql);
    }

    private void buscarDatos(String sql) {
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                txtCodigo.setText(rs.getString("codigo"));
                txtNombre.setText(rs.getString("nombre"));
                txtPrecioUnitario.setText(rs.getString("precio_unitario"));
                txtPrecioMayor.setText(String.valueOf(rs.getDouble("precio_mayor")));
                txtDescripcion.setText(rs.getString("descripcion"));
                txtUnidadMedida.setText(rs.getString("unidad_medida"));

                String categoria = rs.getString("categoria");
                chkBombones.setSelected(categoria.contains("Bombones"));
                chkTabletas.setSelected(categoria.contains("Tabletas"));
                chkTrufas.setSelected(categoria.contains("Trufas"));
                chkCacao.setSelected(categoria.contains("Cacao"));
                chkManteca.setSelected(categoria.contains("Manteca"));

                String tipo = rs.getString("tipo");
                chkLeche.setSelected(tipo.contains("Leche"));
                chkNegro.setSelected(tipo.contains("Negro"));
                chkBlanco.setSelected(tipo.contains("Blanco"));
                chkOtro.setSelected(tipo.contains("Otro"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void fnEditar(ActionEvent event){
        try {
            String codigo = txtCodigo.getText();
            String nombre = txtNombre.getText();
            String precioUnitario = txtPrecioUnitario.getText();
            String precioMayor = txtPrecioMayor.getText();
            String descripcion = txtDescripcion.getText();
            String unidadMedida = txtUnidadMedida.getText();

            // CATEGORIA
            String categoria = "";
            if (chkBombones.isSelected()) categoria += "Bombones,";
            if (chkTabletas.isSelected()) categoria += "Tabletas,";
            if (chkTrufas.isSelected()) categoria += "Trufas,";
            if (chkCacao.isSelected()) categoria += "Cacao,";
            if (chkManteca.isSelected()) categoria += "Manteca,";
            if(!categoria.isEmpty()) categoria = categoria.substring(0, categoria.length()-1);

            // TIPO
            String tipo = "";
            if (chkLeche.isSelected()) tipo += "Leche,";
            if (chkNegro.isSelected()) tipo += "Negro,";
            if (chkBlanco.isSelected()) tipo += "Blanco,";
            if (chkOtro.isSelected()) tipo += "Otro,";
            if(!tipo.isEmpty()) tipo = tipo.substring(0, tipo.length()-1);

            String sql = "UPDATE tbl_producto SET nombre=?, precio_unitario=?, precio_mayor=?, descripcion=?, unidad_medida=?, categoria=?, tipo=? WHERE codigo=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, nombre);
            ps.setString(2, precioUnitario);
            ps.setDouble(3, Double.parseDouble(precioMayor));
            ps.setString(4, descripcion);
            ps.setString(5, unidadMedida);
            ps.setString(6, categoria);
            ps.setString(7, tipo);
            ps.setString(8, codigo);
            ps.executeUpdate();

            JOptionPane.showMessageDialog(null, "Producto editado correctamente");

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    private void fnlimpiar(ActionEvent actionEvent){
        limpiarCampos();
    }

    @FXML
    private void limpiarCampos() {
        txtCodigo.clear();
        txtNombre.clear();
        txtPrecioUnitario.clear();
        txtPrecioMayor.clear();
        txtDescripcion.clear();
        txtUnidadMedida.clear();

        chkBombones.setSelected(false);
        chkTabletas.setSelected(false);
        chkTrufas.setSelected(false);
        chkCacao.setSelected(false);
        chkManteca.setSelected(false);

        chkLeche.setSelected(false);
        chkNegro.setSelected(false);
        chkBlanco.setSelected(false);
        chkOtro.setSelected(false);
    }
}