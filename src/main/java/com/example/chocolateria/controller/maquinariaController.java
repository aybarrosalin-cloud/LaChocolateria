package com.example.chocolateria.controller;

import com.example.chocolateria.baseDeDatos.conexion;
import com.example.chocolateria.modelo.maquinariaModelo;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;

import java.sql.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class maquinariaController {

    @FXML private TextField        txtId;
    @FXML private TextField        txtNombre;
    @FXML private ComboBox<String> cbTipo;
    @FXML private TextField        txtMarcaModelo;
    @FXML private TextField        txtNumeroSerie;
    @FXML private DatePicker       dpFechaAdquisicion;
    @FXML private ComboBox<String> cbEstado;
    @FXML private ComboBox<String> cbResponsable;

    private final conexion con = new conexion();
    private final Map<String, Integer> mapaResponsables = new HashMap<>();
    private maquinariaModelo maquinariaCargada = null;

    @FXML private Label lblUsuario;
    @FXML private ImageView imgFotoPerfil;

    @FXML
    public void initialize() {
        CargarPerfil.aplicar(lblUsuario, imgFotoPerfil);

        cbTipo.setItems(FXCollections.observableArrayList(
            "Temperadora de chocolate",
            "Moldeadora",
            "Envolvedora",
            "Refinadora / Molino",
            "Conchadora",
            "Mezcladora",
            "Túnel de enfriamiento",
            "Derretidora",
            "Dosificadora",
            "Selladora",
            "Compresor",
            "Banda transportadora",
            "Caldera / Horno",
            "Empacadora",
            "Otro"
        ));

        cbEstado.setItems(FXCollections.observableArrayList(
            "Activo", "En mantenimiento", "Fuera de servicio", "Retirado"
        ));

        cargarResponsables();
        generarSiguienteId();
    }

    private void cargarResponsables() {
        String sql = "SELECT id_empleado, nombre + ' ' + apellido AS nombre_completo FROM tbl_empleado ORDER BY nombre";
        try (Connection conn = con.establecerConexion();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                String nombre = rs.getString("nombre_completo");
                int id = rs.getInt("id_empleado");
                cbResponsable.getItems().add(nombre);
                mapaResponsables.put(nombre, id);
            }
        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", e.getMessage());
        }
    }

    @FXML
    private void guardar() {
        if (!validarCampos()) return;

        int idResponsable = mapaResponsables.getOrDefault(cbResponsable.getValue(), 0);

        String sql = "INSERT INTO tbl_maquinaria (nombre, tipo, marca_modelo, numero_serie, " +
                     "fecha_adquisicion, estado, id_responsable, responsable) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = con.establecerConexion();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, txtNombre.getText().trim());
            ps.setString(2, cbTipo.getValue());
            ps.setString(3, txtMarcaModelo.getText().trim());
            ps.setString(4, txtNumeroSerie.getText().trim());
            ps.setObject(5, dpFechaAdquisicion.getValue() != null
                ? Date.valueOf(dpFechaAdquisicion.getValue()) : null);
            ps.setString(6, cbEstado.getValue());
            ps.setObject(7, idResponsable > 0 ? idResponsable : null);
            ps.setString(8, cbResponsable.getValue() != null ? cbResponsable.getValue() : "");
            ps.executeUpdate();

            mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Maquinaria registrada correctamente.");
            limpiar();

        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error al guardar", e.getMessage());
        }
    }

    @FXML
    private void fnEditar() {
        if (maquinariaCargada == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Atención", "Busca un registro por ID primero.");
            return;
        }
        if (!validarCampos()) return;

        int idResponsable = mapaResponsables.getOrDefault(cbResponsable.getValue(), 0);

        String sql = "UPDATE tbl_maquinaria SET nombre=?, tipo=?, marca_modelo=?, numero_serie=?, " +
                     "fecha_adquisicion=?, estado=?, id_responsable=?, responsable=? " +
                     "WHERE id_maquinaria=?";
        try (Connection conn = con.establecerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, txtNombre.getText().trim());
            ps.setString(2, cbTipo.getValue());
            ps.setString(3, txtMarcaModelo.getText().trim());
            ps.setString(4, txtNumeroSerie.getText().trim());
            ps.setObject(5, dpFechaAdquisicion.getValue() != null
                ? Date.valueOf(dpFechaAdquisicion.getValue()) : null);
            ps.setString(6, cbEstado.getValue());
            ps.setObject(7, idResponsable > 0 ? idResponsable : null);
            ps.setString(8, cbResponsable.getValue() != null ? cbResponsable.getValue() : "");
            ps.setInt(9, maquinariaCargada.getIdMaquinaria());
            ps.executeUpdate();

            mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Maquinaria actualizada correctamente.");
            limpiar();

        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error al editar", e.getMessage());
        }
    }

    @FXML
    private void fnEliminar() {
        if (maquinariaCargada == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Atención", "Busca un registro por ID primero.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirmar eliminación");
        confirm.setHeaderText(null);
        confirm.setContentText("¿Eliminar la maquinaria " + maquinariaCargada.getNombre() + "?");

        confirm.showAndWait().ifPresent(resp -> {
            if (resp == ButtonType.OK) {
                try (Connection conn = con.establecerConexion();
                     PreparedStatement ps = conn.prepareStatement(
                         "DELETE FROM tbl_maquinaria WHERE id_maquinaria=?")) {
                    ps.setInt(1, maquinariaCargada.getIdMaquinaria());
                    ps.executeUpdate();
                    mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Maquinaria eliminada correctamente.");
                    limpiar();
                } catch (Exception e) {
                    mostrarAlerta(Alert.AlertType.ERROR, "Error al eliminar", e.getMessage());
                }
            }
        });
    }

    @FXML
    private void fnBuscar() {
        String idTexto = txtId.getText().trim();
        if (idTexto.isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Atención", "Escribe un ID para buscar.");
            return;
        }
        int idBuscar;
        try {
            idBuscar = Integer.parseInt(idTexto);
        } catch (NumberFormatException ex) {
            mostrarAlerta(Alert.AlertType.WARNING, "ID inválido", "El ID debe ser un número entero.");
            return;
        }

        String sql = "SELECT id_maquinaria, nombre, tipo, marca_modelo, numero_serie, " +
                     "fecha_adquisicion, estado, id_responsable, responsable " +
                     "FROM tbl_maquinaria WHERE id_maquinaria = ?";
        try (Connection conn = con.establecerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idBuscar);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Date d = rs.getDate("fecha_adquisicion");
                cargarEnFormulario(new maquinariaModelo(
                    rs.getInt("id_maquinaria"),
                    rs.getString("nombre"),
                    rs.getString("tipo"),
                    rs.getString("marca_modelo")  != null ? rs.getString("marca_modelo")  : "",
                    rs.getString("numero_serie")  != null ? rs.getString("numero_serie")  : "",
                    d != null ? d.toLocalDate() : null,
                    rs.getString("estado"),
                    rs.getInt("id_responsable"),
                    rs.getString("responsable")   != null ? rs.getString("responsable")   : ""
                ));
                mostrarAlerta(Alert.AlertType.INFORMATION, "Encontrado", "Maquinaria encontrada y cargada en el formulario.");
            } else {
                mostrarAlerta(Alert.AlertType.WARNING, "No encontrado",
                    "No existe una maquinaria con el ID " + idBuscar + ".");
            }
        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error de búsqueda", e.getMessage());
        }
    }

    @FXML
    private void limpiar() {
        txtId.clear();
        txtNombre.clear();
        cbTipo.setValue(null);
        txtMarcaModelo.clear();
        txtNumeroSerie.clear();
        dpFechaAdquisicion.setValue(null);
        cbEstado.setValue(null);
        cbResponsable.setValue(null);
        maquinariaCargada = null;
        generarSiguienteId();
    }

    private void cargarEnFormulario(maquinariaModelo m) {
        this.maquinariaCargada = m;
        txtId.setText(String.valueOf(m.getIdMaquinaria()));
        txtNombre.setText(m.getNombre());
        cbTipo.setValue(m.getTipo());
        txtMarcaModelo.setText(m.getMarcaModelo());
        txtNumeroSerie.setText(m.getNumeroSerie());
        dpFechaAdquisicion.setValue(m.getFechaAdquisicion());
        cbEstado.setValue(m.getEstado());
        cbResponsable.setValue(m.getResponsable());
    }

    private void generarSiguienteId() {
        String sql = "SELECT ISNULL(MAX(id_maquinaria), 0) + 1 AS siguiente FROM tbl_maquinaria";
        try (Connection c = con.establecerConexion();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            if (rs.next()) txtId.setText(String.valueOf(rs.getInt("siguiente")));

        } catch (SQLException e) {
            txtId.setText("1");
        }
    }

    private boolean validarCampos() {
        if (txtNombre.getText().trim().isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Campo requerido", "El nombre es obligatorio.");
            return false;
        }
        if (cbTipo.getValue() == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Campo requerido", "Selecciona el tipo de maquinaria.");
            return false;
        }
        if (cbEstado.getValue() == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Campo requerido", "Selecciona el estado.");
            return false;
        }
        return true;
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    // -- Navegacion --
    @FXML private void irAInicio(javafx.event.ActionEvent e)              { Navegacion.irA("/vistasFinales/vistaInicio.fxml", e); }
    @FXML private void irAOrdenCliente(javafx.event.ActionEvent e)        { Navegacion.irA("/vistasFinales/vistaOrdenCliente.fxml", e); }
    @FXML private void irAPagoVenta(javafx.event.ActionEvent e)           { Navegacion.irA("/vistasFinales/vistaPagoVenta.fxml", e); }
    @FXML private void irAGestionEnvios(javafx.event.ActionEvent e)       { Navegacion.irA("/vistasFinales/vistaGestionEnvios.fxml", e); }
    @FXML private void irAGestionReclamos(javafx.event.ActionEvent e)     { Navegacion.irA("/vistasFinales/vistaGestionReclamos.fxml", e); }
    @FXML private void irASolicitudProduccion(javafx.event.ActionEvent e) { Navegacion.irA("/vistasFinales/vistaSolicitudDeProduccion.fxml", e); }
    @FXML private void irAOrdenProduccion(javafx.event.ActionEvent e)     { Navegacion.irA("/vistasFinales/vistaOrdenProduccion.fxml", e); }
    @FXML private void irASalidaMateriales(javafx.event.ActionEvent e)    { Navegacion.irA("/vistasFinales/vistaRecepcion.fxml", e); }
    @FXML private void irASalidaProductos(javafx.event.ActionEvent e)     { Navegacion.irA("/vistasFinales/vistaRecepcion.fxml", e); }
    @FXML private void irAOrdenProveedor(javafx.event.ActionEvent e)      { Navegacion.irA("/vistasFinales/vistaOrdenProveedor.fxml", e); }
    @FXML private void irAPagoCompra(javafx.event.ActionEvent e)          { Navegacion.irA("/vistasFinales/vistaPagoCompra.fxml", e); }
    @FXML private void irARegistroProducto(javafx.event.ActionEvent e)    { Navegacion.irA("/vistasFinales/vistaRegistroProducto.fxml", e); }
    @FXML private void irARegistroEmpleado(javafx.event.ActionEvent e)    { Navegacion.irA("/vistasFinales/vistaRegistroDeEmpleado.fxml", e); }
    @FXML private void irARegistroCliente(javafx.event.ActionEvent e)     { Navegacion.irA("/vistasFinales/vistaRegistroDeCliente.fxml", e); }
    @FXML private void irARegistroSuplidor(javafx.event.ActionEvent e)    { Navegacion.irA("/vistasFinales/vistaRegistroSuplidor.fxml", e); }
    @FXML private void irARegistroMaquinaria(javafx.event.ActionEvent e)  { Navegacion.irA("/vistasFinales/vistaRegistroMaquinaria.fxml", e); }
    @FXML private void irAReportesVentas(javafx.event.ActionEvent e)      { Navegacion.irA("/vistasFinales/vistaConsultasGenerales.fxml", e); }
    @FXML private void irAReportesCompras(javafx.event.ActionEvent e)     { Navegacion.irA("/vistasFinales/vistaConsultasGenerales.fxml", e); }
    @FXML private void irAReportesInventario(javafx.event.ActionEvent e)  { Navegacion.irA("/vistasFinales/vistaConsultasGenerales.fxml", e); }
    @FXML private void irAReportesProduccion(javafx.event.ActionEvent e)  { Navegacion.irA("/vistasFinales/vistaConsultasGenerales.fxml", e); }
    @FXML private void irAMantenimientoMaquinaria(javafx.event.ActionEvent e) { Navegacion.irA("/vistasFinales/vistaMantenimientoMaquinaria.fxml", e); }
    @FXML private void irAConsultas(javafx.event.ActionEvent e)           { Navegacion.irA("/vistasFinales/vistaConsultasGenerales.fxml", e); }
    @FXML private void irAConsultaMaquinarias(javafx.event.ActionEvent e) { Navegacion.irA("/vistasFinales/vistaConsultaMaquinarias.fxml", e); }
    @FXML private void salir(javafx.event.ActionEvent e)                  { Navegacion.salir(e); }
}
