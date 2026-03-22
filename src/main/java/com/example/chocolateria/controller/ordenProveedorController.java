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
import java.util.ArrayList;
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

    // Lista interna de productos seleccionados
    private ArrayList<String> productosSeleccionados = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        conexion conexion = new conexion();
        con = conexion.establecerConexion();

        cmbPrioridad.setItems(FXCollections.observableArrayList("Alta", "Media", "Baja"));
        cmbEstadoPago.setItems(FXCollections.observableArrayList("Pagado", "Pendiente"));
        chCantidad.setItems(FXCollections.observableArrayList("1-10", "11-50", "50+"));

        cmbProducto.setItems(llenarProductos());
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

    // BOTON AGREGAR: añade producto a la lista interna
    @FXML
    private void agregarProducto(ActionEvent event){
        String producto = cmbProducto.getValue();
        if(producto != null && !productosSeleccionados.contains(producto)){
            productosSeleccionados.add(producto);
            JOptionPane.showMessageDialog(null, "Producto agregado: " + producto);
        }
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

            // CATEGORIA (CHECKBOX)
            String categoria = "";
            if (chkProgramada.isSelected()) categoria += "Programada ";
            if (chkUrgente.isSelected()) categoria += "Urgente ";
            if (chkEmergencia.isSelected()) categoria += "Emergencia ";

            // Concatenar productos en un solo String
            String productosConcatenados = String.join(", ", productosSeleccionados);

            // Insertar orden principal
            String sqlOrden = "INSERT INTO tbl_orden_proveedor(codigo, rnc_proveedor, fecha_requerida, prioridad, cantidad_pedido, estado_pago, monto_total, categoria, descripcion, producto) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sqlOrden);
            ps.setString(1, codigo);
            ps.setString(2, rnc);
            ps.setDate(3, Date.valueOf(fecha));
            ps.setString(4, prioridad);
            ps.setString(5, cantidad);
            ps.setString(6, estadoPago);
            ps.setDouble(7, Double.parseDouble(monto));
            ps.setString(8, categoria);
            ps.setString(9, descripcion);
            ps.setString(10, productosConcatenados); // <- todos los productos juntos
            ps.executeUpdate();

            JOptionPane.showMessageDialog(null, "Orden guardada correctamente");
            limpiarCampos();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
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
                // Campos principales
                txtCodigo.setText(rs.getString("codigo"));
                txtRNC.setText(rs.getString("rnc_proveedor"));
                dpFecha.setValue(rs.getDate("fecha_requerida").toLocalDate());
                cmbPrioridad.setValue(rs.getString("prioridad"));
                chCantidad.setValue(rs.getString("cantidad_pedido"));
                cmbEstadoPago.setValue(rs.getString("estado_pago"));
                txtMonto.setText(String.valueOf(rs.getDouble("monto_total")));
                txtDescripcion.setText(rs.getString("descripcion"));

                // Categoría (CHECKBOX)
                String categoria = rs.getString("categoria");
                chkProgramada.setSelected(categoria.contains("Programada"));
                chkUrgente.setSelected(categoria.contains("Urgente"));
                chkEmergencia.setSelected(categoria.contains("Emergencia"));

                // Productos (COMBOBOX)
                String productos = rs.getString("producto"); // ahora todos los productos están en una sola columna
                productosSeleccionados.clear();
                if(productos != null && !productos.isEmpty()) {
                    String[] listaProd = productos.split(",\\s*");
                    for(String prod : listaProd) {
                        productosSeleccionados.add(prod);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void fnEditar(ActionEvent event) {
        try {
            String codigo = txtCodigo.getText().trim();

            // Actualizar orden
            String sql = "UPDATE tbl_orden_proveedor SET rnc_proveedor=?, fecha_requerida=?, prioridad=?, cantidad_pedido=?, estado_pago=?, monto_total=?, categoria=?, descripcion=?, producto=? WHERE codigo=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, txtRNC.getText());
            ps.setDate(2, Date.valueOf(dpFecha.getValue()));
            ps.setString(3, cmbPrioridad.getValue());
            ps.setString(4, chCantidad.getValue());
            ps.setString(5, cmbEstadoPago.getValue());
            ps.setDouble(6, Double.parseDouble(txtMonto.getText()));

            // CATEGORIA
            String categoria = "";
            if (chkProgramada.isSelected()) categoria += "Programada ";
            if (chkUrgente.isSelected()) categoria += "Urgente ";
            if (chkEmergencia.isSelected()) categoria += "Emergencia ";
            ps.setString(7, categoria);

            ps.setString(8, txtDescripcion.getText());

            // Concatenar productos para actualizar
            String productosConcatenados = String.join(", ", productosSeleccionados);
            ps.setString(9, productosConcatenados);

            ps.setString(10, codigo);
            ps.executeUpdate();

            JOptionPane.showMessageDialog(null, "Orden editada correctamente");

        } catch (Exception e){
            e.printStackTrace();
        }
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

        productosSeleccionados.clear();
    }
}