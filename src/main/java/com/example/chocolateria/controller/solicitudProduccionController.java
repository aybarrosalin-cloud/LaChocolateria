package com.example.chocolateria.controller;
import com.example.chocolateria.baseDeDatos.conexion;
import javafx.fxml.FXML;
import javafx.scene.control.*;

        import java.sql.Connection;
import java.sql.PreparedStatement;

public class solicitudProduccionController {

    @FXML private ChoiceBox<String> cbProducto;
    @FXML private TextField txtCantidad;
    @FXML private DatePicker dpFechaSolicitud;
    @FXML private DatePicker dpFechaProduccion;
    @FXML private ChoiceBox<String> cbPrioridad;
    @FXML private ChoiceBox<String> cbResponsable;
    @FXML private TextArea txtObservaciones;

    conexion con = new conexion();

    @FXML
    public void initialize() {
        cbProducto.getItems().addAll("Chocolate Negro", "Chocolate Blanco", "Chocolate con Leche");
        cbPrioridad.getItems().addAll("Alta", "Media", "Baja");
        cbResponsable.getItems().addAll("Empleado 1", "Empleado 2", "Empleado 3");
    }

    @FXML
    public void guardar() {
        try {
            Connection cn = con.establecerConexion();

            String sql = "INSERT INTO tbl_solicitud_produccion (producto, cantidad, fecha_solicitud, fecha_produccion, prioridad, responsable, observaciones) VALUES (?,?,?,?,?,?,?)";

            PreparedStatement ps = cn.prepareStatement(sql);

            ps.setString(1, cbProducto.getValue());
            ps.setInt(2, Integer.parseInt(txtCantidad.getText()));
            ps.setDate(3, java.sql.Date.valueOf(dpFechaSolicitud.getValue()));
            ps.setDate(4, java.sql.Date.valueOf(dpFechaProduccion.getValue()));
            ps.setString(5, cbPrioridad.getValue());
            ps.setString(6, cbResponsable.getValue());
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