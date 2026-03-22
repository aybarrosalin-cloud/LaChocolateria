package com.example.chocolateria.controller;

import com.example.chocolateria.baseDeDatos.conexion;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javax.swing.*;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class reclamoController implements Initializable {
    private Connection con;

    //formulario
    @FXML private TextField txtCodigo;
    @FXML private ComboBox<String> cmbCliente;
    @FXML private ChoiceBox<String> chTipoReclamo;
    @FXML private ChoiceBox<String> chEstado;
    @FXML private ComboBox<String> cmbOrden;
    @FXML private TextArea txtDescripcion;

    // RADIO BUTTONS
    @FXML private RadioButton rbAlta;
    @FXML private RadioButton rbMedia;
    @FXML private RadioButton rbBaja;
    private ToggleGroup grupoPrioridad;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        conexion conexion = new conexion();
        con = conexion.establecerConexion();

        cmbCliente.setItems(llenaClientes());
        cmbOrden.setItems(llenaOrdenes());

        chTipoReclamo.setItems(FXCollections.observableArrayList("Producto", "Servicio", "Entrega"));
        chEstado.setItems(FXCollections.observableArrayList("Pendiente", "En proceso", "Resuelto"));

        // AGRUPAR RADIOBUTTONS
        grupoPrioridad = new ToggleGroup();
        rbAlta.setToggleGroup(grupoPrioridad);
        rbMedia.setToggleGroup(grupoPrioridad);
        rbBaja.setToggleGroup(grupoPrioridad);

        actualizarDatos();
    }

    public ObservableList<String> llenaClientes() {
        ObservableList<String> lista = FXCollections.observableArrayList();
        String sql = "SELECT nombre FROM tbl_cliente";

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

    public ObservableList<String> llenaOrdenes() {
        ObservableList<String> lista = FXCollections.observableArrayList();
        String sql = "SELECT id_pedido FROM tbl_pedido";

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                lista.add(rs.getString("id_pedido"));
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
            String tipo = chTipoReclamo.getValue();
            String estado = chEstado.getValue();
            String orden = cmbOrden.getValue();
            String descripcion = txtDescripcion.getText();

            String prioridad = "";
            if (rbAlta.isSelected()) prioridad = "Alta";
            if (rbMedia.isSelected()) prioridad = "Media";
            if (rbBaja.isSelected()) prioridad = "Baja";

            String sql = "INSERT INTO tbl_reclamos VALUES (?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, codigo);
            ps.setString(2, cliente);
            ps.setString(3, tipo);
            ps.setString(4, estado);
            ps.setString(5, orden);
            ps.setString(6, descripcion);
            ps.setString(7, prioridad);

            ps.executeUpdate();

            JOptionPane.showMessageDialog(null, "Reclamo guardado");
            limpiarCampos();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void fnbuscar(ActionEvent event) {
        String codigo = txtCodigo.getText().trim();
        String sql = "SELECT * FROM tbl_reclamos WHERE codigo='" + codigo + "'";
        buscarDatos(sql);
    }

    private void buscarDatos(String sql) {
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                txtCodigo.setText(rs.getString("codigo"));
                cmbCliente.setValue(rs.getString("cliente"));
                chTipoReclamo.setValue(rs.getString("tipo_reclamo"));
                chEstado.setValue(rs.getString("estado"));
                cmbOrden.setValue(rs.getString("orden_relacionada"));
                txtDescripcion.setText(rs.getString("descripcion"));

                String prioridad = rs.getString("prioridad");

                if (prioridad.equals("Alta")) rbAlta.setSelected(true);
                if (prioridad.equals("Media")) rbMedia.setSelected(true);
                if (prioridad.equals("Baja")) rbBaja.setSelected(true);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void fnEditar(ActionEvent event) {
        String codigo = txtCodigo.getText().trim();
        String cliente = cmbCliente.getValue();
        String tipo = chTipoReclamo.getValue();
        String estado = chEstado.getValue();
        String orden = cmbOrden.getValue();
        String descripcion = txtDescripcion.getText();

        String prioridad = "";
        if (rbAlta.isSelected()) prioridad = "Alta";
        if (rbMedia.isSelected()) prioridad = "Media";
        if (rbBaja.isSelected()) prioridad = "Baja";

        String sql = "UPDATE tbl_reclamos SET " +
                "cliente='" + cliente + "', " +
                "tipo_reclamo='" + tipo + "', " +
                "estado='" + estado + "', " +
                "orden_relacionada='" + orden + "', " +
                "descripcion='" + descripcion + "', " +
                "prioridad='" + prioridad + "' " +
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
        txtDescripcion.clear();
        cmbCliente.setValue(null);
        cmbOrden.setValue(null);
        chTipoReclamo.setValue(null);
        chEstado.setValue(null);
        grupoPrioridad.selectToggle(null);
    }
}