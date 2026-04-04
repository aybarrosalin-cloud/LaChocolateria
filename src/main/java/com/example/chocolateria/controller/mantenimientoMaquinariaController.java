package com.example.chocolateria.controller;

import com.example.chocolateria.baseDeDatos.conexion;
import com.example.chocolateria.modelo.mantenimientoMaquinariaModelo;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class mantenimientoMaquinariaController {

    @FXML private TextField        txtId;
    @FXML private DatePicker       dpFechaMantenimiento;
    @FXML private DatePicker       dpProximoMantenimiento;
    @FXML private ComboBox<String> cbMaquina;
    @FXML private ComboBox<String> cbTecnico;
    @FXML private TextField        txtCosto;
    @FXML private ComboBox<String> cbEstadoMaquina;
    @FXML private ComboBox<String> cbTipoMantenimiento;
    @FXML private TextArea         taObservaciones;
    @FXML private TextField        txtBuscarTabla;
    @FXML private Label            lblAlerta;

    @FXML private TableView<mantenimientoMaquinariaModelo>              tablaMantenimientos;
    @FXML private TableColumn<mantenimientoMaquinariaModelo, Number>    colId;
    @FXML private TableColumn<mantenimientoMaquinariaModelo, LocalDate> colFecha;
    @FXML private TableColumn<mantenimientoMaquinariaModelo, LocalDate> colProximo;
    @FXML private TableColumn<mantenimientoMaquinariaModelo, String>    colMaquina;
    @FXML private TableColumn<mantenimientoMaquinariaModelo, String>    colTecnico;
    @FXML private TableColumn<mantenimientoMaquinariaModelo, Number>    colCosto;
    @FXML private TableColumn<mantenimientoMaquinariaModelo, String>    colEstadoMaquina;
    @FXML private TableColumn<mantenimientoMaquinariaModelo, String>    colTipoMantenimiento;
    @FXML private TableColumn<mantenimientoMaquinariaModelo, String>    colObservaciones;

    @FXML private TableView<ObservableList<String>>           tablaResumen;
    @FXML private TableColumn<ObservableList<String>, String> colResumenMaquina;
    @FXML private TableColumn<ObservableList<String>, String> colResumenTotal;
    @FXML private TableColumn<ObservableList<String>, String> colResumenUltimo;
    @FXML private TableColumn<ObservableList<String>, String> colResumenProximo;

    private final ObservableList<mantenimientoMaquinariaModelo> lista = FXCollections.observableArrayList();
    private final conexion con = new conexion();

    @FXML
    public void initialize() {

        cbEstadoMaquina.setItems(FXCollections.observableArrayList("Operativa", "En reparación", "Fuera de servicio"));
        cbTipoMantenimiento.setItems(FXCollections.observableArrayList("Preventivo", "Correctivo"));

        cargarMaquinas();
        cargarTecnicos();

        cbMaquina.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == null) {
                cbTecnico.setValue(null);
                return;
            }
            try (Connection conn = con.establecerConexion();
                 PreparedStatement ps = conn.prepareStatement(
                         "SELECT tecnico FROM tbl_asignacion_tecnico WHERE maquina = ?")) {

                ps.setString(1, newVal);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    cbTecnico.setValue(rs.getString("tecnico"));
                } else {
                    cbTecnico.setValue(null);
                }

            } catch (Exception e) {
                mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo cargar el técnico asignado.");
            }
        });

        colId.setCellValueFactory(d                -> d.getValue().idProperty());
        colFecha.setCellValueFactory(d             -> d.getValue().fechaMantenimientoProperty());
        colProximo.setCellValueFactory(d           -> d.getValue().fechaProximoMantenimientoProperty());
        colMaquina.setCellValueFactory(d           -> d.getValue().maquinaProperty());
        colTecnico.setCellValueFactory(d           -> d.getValue().tecnicoProperty());
        colCosto.setCellValueFactory(d             -> d.getValue().costoProperty());
        colEstadoMaquina.setCellValueFactory(d     -> d.getValue().estadoMaquinaProperty());
        colTipoMantenimiento.setCellValueFactory(d -> d.getValue().tipoMantenimientoProperty());
        colObservaciones.setCellValueFactory(d     -> d.getValue().observacionesProperty());

        tablaMantenimientos.setRowFactory(tv -> new TableRow<>() {
            @Override
            protected void updateItem(mantenimientoMaquinariaModelo m, boolean empty) {
                super.updateItem(m, empty);
                if (m == null || empty) {
                    setStyle("");
                    return;
                }
                LocalDate fecha = m.getFechaMantenimiento();
                if (fecha != null && ChronoUnit.DAYS.between(fecha, LocalDate.now()) > 30) {
                    setStyle("-fx-background-color:#fde8e8;");
                } else {
                    setStyle("");
                }
            }
        });

        FilteredList<mantenimientoMaquinariaModelo> listaFiltrada = new FilteredList<>(lista, p -> true);
        if (txtBuscarTabla != null) {
            txtBuscarTabla.textProperty().addListener((obs, oldVal, newVal) ->
                    listaFiltrada.setPredicate(m -> {
                        if (newVal == null || newVal.isBlank()) return true;
                        String f = newVal.toLowerCase();
                        return m.getMaquina().toLowerCase().contains(f)
                                || m.getTecnico().toLowerCase().contains(f)
                                || m.getTipoMantenimiento().toLowerCase().contains(f);
                    })
            );
        }
        tablaMantenimientos.setItems(listaFiltrada);

        tablaMantenimientos.getSelectionModel().selectedItemProperty().addListener(
                (obs, old, sel) -> { if (sel != null) cargarEnFormulario(sel); }
        );

        colResumenMaquina.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().get(0)));
        colResumenTotal.setCellValueFactory(d   -> new SimpleStringProperty(d.getValue().get(1)));
        colResumenUltimo.setCellValueFactory(d  -> new SimpleStringProperty(d.getValue().get(2)));
        colResumenProximo.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().get(3)));

        cargarMantenimientos();
        cargarResumen();
        verificarAlertas();
    }

    private void cargarMaquinas() {
        try (Connection conn = con.establecerConexion();
             PreparedStatement ps = conn.prepareStatement("SELECT nombre FROM tbl_maquinaria")) {
            ResultSet rs = ps.executeQuery();
            ObservableList<String> items = FXCollections.observableArrayList();
            while (rs.next()) items.add(rs.getString("nombre"));
            cbMaquina.setItems(items);
        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudieron cargar las máquinas.");
        }
    }

    private void cargarTecnicos() {
        try (Connection conn = con.establecerConexion();
             PreparedStatement ps = conn.prepareStatement("SELECT nombre FROM tbl_tecnico")) {
            ResultSet rs = ps.executeQuery();
            ObservableList<String> items = FXCollections.observableArrayList();
            while (rs.next()) items.add(rs.getString("nombre"));
            cbTecnico.setItems(items);
        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudieron cargar los técnicos.");
        }
    }

    private void cargarMantenimientos() {
        lista.clear();
        String sql = "SELECT id, fecha_mantenimiento, fecha_proximo_mantenimiento, maquina, tecnico, costo, " +
                "estado_maquina, tipo_mantenimiento, observaciones " +
                "FROM tbl_mantenimiento_maquinaria ORDER BY fecha_mantenimiento DESC";
        try (Connection conn = con.establecerConexion();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Date dProximo = rs.getDate("fecha_proximo_mantenimiento");
                lista.add(new mantenimientoMaquinariaModelo(
                        rs.getInt("id"),
                        rs.getDate("fecha_mantenimiento").toLocalDate(),
                        dProximo != null ? dProximo.toLocalDate() : null,
                        rs.getString("maquina"),
                        rs.getString("tecnico"),
                        rs.getDouble("costo"),
                        rs.getString("estado_maquina"),
                        rs.getString("tipo_mantenimiento"),
                        rs.getString("observaciones")
                ));
            }
        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error al cargar", e.getMessage());
        }
    }

    private void cargarResumen() {
        ObservableList<ObservableList<String>> resumen = FXCollections.observableArrayList();
        String sql = "SELECT maquina, SUM(costo) AS total, MAX(fecha_mantenimiento) AS ultimo, " +
                "MAX(fecha_proximo_mantenimiento) AS proximo " +
                "FROM tbl_mantenimiento_maquinaria GROUP BY maquina ORDER BY maquina";
        try (Connection conn = con.establecerConexion();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                ObservableList<String> fila = FXCollections.observableArrayList();
                fila.add(rs.getString("maquina"));
                fila.add(String.format("RD$ %,.2f", rs.getDouble("total")));
                fila.add(rs.getDate("ultimo")  != null ? rs.getDate("ultimo").toLocalDate().toString()  : "—");
                fila.add(rs.getDate("proximo") != null ? rs.getDate("proximo").toLocalDate().toString() : "—");
                resumen.add(fila);
            }
        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error al cargar resumen", e.getMessage());
        }
        tablaResumen.setItems(resumen);
    }

    private void verificarAlertas() {
        long maquinasSinMant = lista.stream()
                .filter(m -> m.getFechaMantenimiento() != null
                        && ChronoUnit.DAYS.between(m.getFechaMantenimiento(), LocalDate.now()) > 30)
                .map(mantenimientoMaquinariaModelo::getMaquina)
                .distinct()
                .count();

        lblAlerta.setText(maquinasSinMant > 0
                ? " " + maquinasSinMant + " máquina(s) sin mantenimiento en más de 30 días"
                : "");
    }

    @FXML
    private void guardarMantenimiento() {
        if (!validarCampos()) return;

        String sql = "INSERT INTO tbl_mantenimiento_maquinaria " +
                "(fecha_mantenimiento, fecha_proximo_mantenimiento, maquina, tecnico, costo, estado_maquina, tipo_mantenimiento, observaciones) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = con.establecerConexion();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            double    costo   = Double.parseDouble(txtCosto.getText().trim());
            LocalDate proximo = dpProximoMantenimiento.getValue();

            ps.setDate(1, Date.valueOf(dpFechaMantenimiento.getValue()));
            ps.setDate(2, proximo != null ? Date.valueOf(proximo) : null);
            ps.setString(3, cbMaquina.getValue());
            ps.setString(4, cbTecnico.getValue());
            ps.setDouble(5, costo);
            ps.setString(6, cbEstadoMaquina.getValue());
            ps.setString(7, cbTipoMantenimiento.getValue());
            ps.setString(8, taObservaciones.getText().trim());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            int nuevoId = rs.next() ? rs.getInt(1) : 0;

            lista.add(0, new mantenimientoMaquinariaModelo(nuevoId,
                    dpFechaMantenimiento.getValue(), proximo,
                    cbMaquina.getValue(), cbTecnico.getValue(), costo,
                    cbEstadoMaquina.getValue(), cbTipoMantenimiento.getValue(),
                    taObservaciones.getText().trim()));

            cargarResumen();
            verificarAlertas();
            mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Mantenimiento guardado correctamente.");
            limpiarCampos();

        } catch (NumberFormatException e) {
            mostrarAlerta(Alert.AlertType.WARNING, "Costo inválido", "El costo debe ser un número válido.");
        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error al guardar", e.getMessage());
        }
    }

    @FXML
    private void fnEditar() {
        mantenimientoMaquinariaModelo sel = tablaMantenimientos.getSelectionModel().getSelectedItem();
        if (sel == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Atención", "Selecciona un registro de la tabla para editar.");
            return;
        }
        if (!validarCampos()) return;

        String sql = "UPDATE tbl_mantenimiento_maquinaria " +
                "SET fecha_mantenimiento=?, fecha_proximo_mantenimiento=?, maquina=?, tecnico=?, " +
                "costo=?, estado_maquina=?, tipo_mantenimiento=?, observaciones=? WHERE id=?";
        try (Connection conn = con.establecerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            double    costo   = Double.parseDouble(txtCosto.getText().trim());
            LocalDate proximo = dpProximoMantenimiento.getValue();

            ps.setDate(1, Date.valueOf(dpFechaMantenimiento.getValue()));
            ps.setDate(2, proximo != null ? Date.valueOf(proximo) : null);
            ps.setString(3, cbMaquina.getValue());
            ps.setString(4, cbTecnico.getValue());
            ps.setDouble(5, costo);
            ps.setString(6, cbEstadoMaquina.getValue());
            ps.setString(7, cbTipoMantenimiento.getValue());
            ps.setString(8, taObservaciones.getText().trim());
            ps.setInt(9, sel.getId());
            ps.executeUpdate();

            sel.setFechaMantenimiento(dpFechaMantenimiento.getValue());
            sel.setFechaProximoMantenimiento(proximo);
            sel.setMaquina(cbMaquina.getValue());
            sel.setTecnico(cbTecnico.getValue());
            sel.setCosto(costo);
            sel.setEstadoMaquina(cbEstadoMaquina.getValue());
            sel.setTipoMantenimiento(cbTipoMantenimiento.getValue());
            sel.setObservaciones(taObservaciones.getText().trim());

            tablaMantenimientos.refresh();
            cargarResumen();
            verificarAlertas();
            mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Mantenimiento actualizado correctamente.");
            limpiarCampos();

        } catch (NumberFormatException e) {
            mostrarAlerta(Alert.AlertType.WARNING, "Costo inválido", "El costo debe ser un número válido.");
        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error al editar", e.getMessage());
        }
    }

    @FXML
    private void fnEliminar() {
        mantenimientoMaquinariaModelo sel = tablaMantenimientos.getSelectionModel().getSelectedItem();
        if (sel == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Atención", "Selecciona un registro de la tabla para eliminar.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirmar eliminación");
        confirm.setHeaderText(null);
        confirm.setContentText("¿Eliminar el mantenimiento de " + sel.getMaquina() + " del " + sel.getFechaMantenimiento() + "?");

        confirm.showAndWait().ifPresent(resp -> {
            if (resp == ButtonType.OK) {
                try (Connection conn = con.establecerConexion();
                     PreparedStatement ps = conn.prepareStatement(
                             "DELETE FROM tbl_mantenimiento_maquinaria WHERE id=?")) {

                    ps.setInt(1, sel.getId());
                    ps.executeUpdate();
                    lista.remove(sel);
                    cargarResumen();
                    verificarAlertas();
                    mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Registro eliminado correctamente.");
                    limpiarCampos();

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
            mostrarAlerta(Alert.AlertType.WARNING, "Atención", "Escribe un ID en el campo para buscar.");
            return;
        }

        int idBuscar;
        try {
            idBuscar = Integer.parseInt(idTexto);
        } catch (NumberFormatException ex) {
            mostrarAlerta(Alert.AlertType.WARNING, "ID inválido", "El ID debe ser un número entero.");
            return;
        }

        for (mantenimientoMaquinariaModelo m : lista) {
            if (m.getId() == idBuscar) {
                tablaMantenimientos.getSelectionModel().select(m);
                tablaMantenimientos.scrollTo(m);
                cargarEnFormulario(m);
                return;
            }
        }

        String sql = "SELECT id, fecha_mantenimiento, fecha_proximo_mantenimiento, maquina, tecnico, costo, " +
                "estado_maquina, tipo_mantenimiento, observaciones " +
                "FROM tbl_mantenimiento_maquinaria WHERE id=?";
        try (Connection conn = con.establecerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idBuscar);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Date dProximo = rs.getDate("fecha_proximo_mantenimiento");
                cargarEnFormulario(new mantenimientoMaquinariaModelo(
                        rs.getInt("id"),
                        rs.getDate("fecha_mantenimiento").toLocalDate(),
                        dProximo != null ? dProximo.toLocalDate() : null,
                        rs.getString("maquina"),
                        rs.getString("tecnico"),
                        rs.getDouble("costo"),
                        rs.getString("estado_maquina"),
                        rs.getString("tipo_mantenimiento"),
                        rs.getString("observaciones")
                ));
                mostrarAlerta(Alert.AlertType.INFORMATION, "Encontrado", "Registro cargado en el formulario.");
            } else {
                mostrarAlerta(Alert.AlertType.WARNING, "No encontrado", "No existe un registro con el ID " + idBuscar + ".");
            }

        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error de búsqueda", e.getMessage());
        }
    }

    @FXML
    private void limpiarCampos() {
        txtId.clear();
        dpFechaMantenimiento.setValue(null);
        dpProximoMantenimiento.setValue(null);
        cbMaquina.setValue(null);
        cbTecnico.setValue(null);
        txtCosto.clear();
        cbEstadoMaquina.setValue(null);
        cbTipoMantenimiento.setValue(null);
        taObservaciones.clear();
        tablaMantenimientos.getSelectionModel().clearSelection();
    }

    private void cargarEnFormulario(mantenimientoMaquinariaModelo m) {
        txtId.setText(String.valueOf(m.getId()));
        dpFechaMantenimiento.setValue(m.getFechaMantenimiento());
        dpProximoMantenimiento.setValue(m.getFechaProximoMantenimiento());
        cbMaquina.setValue(m.getMaquina());
        cbTecnico.setValue(m.getTecnico());
        txtCosto.setText(String.valueOf(m.getCosto()));
        cbEstadoMaquina.setValue(m.getEstadoMaquina());
        cbTipoMantenimiento.setValue(m.getTipoMantenimiento());
        taObservaciones.setText(m.getObservaciones());
    }

    private boolean validarCampos() {
        if (dpFechaMantenimiento.getValue() == null ||
                cbMaquina.getValue() == null            ||
                cbTecnico.getValue() == null            ||
                txtCosto.getText().trim().isEmpty()      ||
                cbEstadoMaquina.getValue() == null       ||
                cbTipoMantenimiento.getValue() == null) {

            mostrarAlerta(Alert.AlertType.WARNING, "Campos vacíos", "Por favor completa todos los campos obligatorios.");
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
    @FXML private void irAReportesVentas(javafx.event.ActionEvent e)      { Navegacion.irA("/vistasFinales/vistaConsultas.fxml", e); }
    @FXML private void irAReportesCompras(javafx.event.ActionEvent e)     { Navegacion.irA("/vistasFinales/vistaConsultas.fxml", e); }
    @FXML private void irAReportesInventario(javafx.event.ActionEvent e)  { Navegacion.irA("/vistasFinales/vistaConsultas.fxml", e); }
    @FXML private void irAReportesProduccion(javafx.event.ActionEvent e)  { Navegacion.irA("/vistasFinales/vistaConsultas.fxml", e); }
    @FXML private void irAMantenimientoMaquinaria(javafx.event.ActionEvent e) { Navegacion.irA("/vistasFinales/vistaMantenimientoMaquinaria.fxml", e); }
    @FXML private void irAConsultas(javafx.event.ActionEvent e)           { Navegacion.irA("/vistasFinales/vistaConsultas.fxml", e); }
    @FXML private void salir(javafx.event.ActionEvent e)                  { Navegacion.irA("/vistasFinales/vistaPrincipal.fxml", e, 949, 533); }
}