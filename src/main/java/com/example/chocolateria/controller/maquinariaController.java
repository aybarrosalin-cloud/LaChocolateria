package com.example.chocolateria.controller;

import com.example.chocolateria.baseDeDatos.conexion;
import com.example.chocolateria.modelo.maquinariaModelo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
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
    @FXML private TextField        txtBuscarTabla;

    @FXML private TableView<maquinariaModelo>                    tablaMaquinarias;
    @FXML private TableColumn<maquinariaModelo, Number>          colId;
    @FXML private TableColumn<maquinariaModelo, String>          colNombre;
    @FXML private TableColumn<maquinariaModelo, String>          colTipo;
    @FXML private TableColumn<maquinariaModelo, String>          colMarca;
    @FXML private TableColumn<maquinariaModelo, String>          colSerie;
    @FXML private TableColumn<maquinariaModelo, LocalDate>       colFecha;
    @FXML private TableColumn<maquinariaModelo, String>          colEstado;
    @FXML private TableColumn<maquinariaModelo, String>          colResponsable;

    private final ObservableList<maquinariaModelo> lista = FXCollections.observableArrayList();
    private final conexion con = new conexion();
    private final Map<String, Integer> mapaResponsables = new HashMap<>();

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

        colId.setCellValueFactory(d          -> d.getValue().idMaquinariaProperty());
        colNombre.setCellValueFactory(d      -> d.getValue().nombreProperty());
        colTipo.setCellValueFactory(d        -> d.getValue().tipoProperty());
        colMarca.setCellValueFactory(d       -> d.getValue().marcaModeloProperty());
        colSerie.setCellValueFactory(d       -> d.getValue().numeroSerieProperty());
        colFecha.setCellValueFactory(d       -> d.getValue().fechaAdquisicionProperty());
        colEstado.setCellValueFactory(d      -> d.getValue().estadoProperty());
        colResponsable.setCellValueFactory(d -> d.getValue().responsableProperty());

        // Color por estado
        tablaMaquinarias.setRowFactory(tv -> new TableRow<>() {
            @Override
            protected void updateItem(maquinariaModelo m, boolean empty) {
                super.updateItem(m, empty);
                if (m == null || empty) { setStyle(""); return; }
                switch (m.getEstado()) {
                    case "Activo"             -> setStyle("-fx-background-color:#e8f5e9;");
                    case "En mantenimiento"   -> setStyle("-fx-background-color:#fff8e1;");
                    case "Fuera de servicio"  -> setStyle("-fx-background-color:#fde8e8;");
                    case "Retirado"           -> setStyle("-fx-background-color:#eeeeee;");
                    default                   -> setStyle("");
                }
            }
        });

        FilteredList<maquinariaModelo> listaFiltrada = new FilteredList<>(lista, p -> true);
        txtBuscarTabla.textProperty().addListener((obs, oldVal, newVal) ->
            listaFiltrada.setPredicate(m -> {
                if (newVal == null || newVal.isBlank()) return true;
                String f = newVal.toLowerCase();
                return m.getNombre().toLowerCase().contains(f)
                    || m.getTipo().toLowerCase().contains(f)
                    || m.getEstado().toLowerCase().contains(f)
                    || m.getResponsable().toLowerCase().contains(f);
            })
        );
        tablaMaquinarias.setItems(listaFiltrada);

        tablaMaquinarias.getSelectionModel().selectedItemProperty().addListener(
            (obs, old, sel) -> { if (sel != null) cargarEnFormulario(sel); }
        );

        cargarMaquinarias();
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

            ResultSet rs = ps.getGeneratedKeys();
            int nuevoId = rs.next() ? rs.getInt(1) : 0;

            lista.add(0, new maquinariaModelo(nuevoId,
                txtNombre.getText().trim(), cbTipo.getValue(),
                txtMarcaModelo.getText().trim(), txtNumeroSerie.getText().trim(),
                dpFechaAdquisicion.getValue(), cbEstado.getValue(),
                idResponsable, cbResponsable.getValue() != null ? cbResponsable.getValue() : ""));

            mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Maquinaria registrada correctamente.");
            limpiar();

        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error al guardar", e.getMessage());
        }
    }

    @FXML
    private void fnEditar() {
        maquinariaModelo sel = tablaMaquinarias.getSelectionModel().getSelectedItem();
        if (sel == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Atención", "Selecciona una maquinaria para editar.");
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
            ps.setInt(9, sel.getIdMaquinaria());
            ps.executeUpdate();

            sel.setNombre(txtNombre.getText().trim());
            sel.setTipo(cbTipo.getValue());
            sel.setMarcaModelo(txtMarcaModelo.getText().trim());
            sel.setNumeroSerie(txtNumeroSerie.getText().trim());
            sel.setFechaAdquisicion(dpFechaAdquisicion.getValue());
            sel.setEstado(cbEstado.getValue());
            sel.setIdResponsable(idResponsable);
            sel.setResponsable(cbResponsable.getValue() != null ? cbResponsable.getValue() : "");

            tablaMaquinarias.refresh();
            mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Maquinaria actualizada correctamente.");
            limpiar();

        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error al editar", e.getMessage());
        }
    }

    @FXML
    private void fnEliminar() {
        maquinariaModelo sel = tablaMaquinarias.getSelectionModel().getSelectedItem();
        if (sel == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Atención", "Selecciona una maquinaria para eliminar.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirmar eliminación");
        confirm.setHeaderText(null);
        confirm.setContentText("¿Eliminar la maquinaria " + sel.getNombre() + "?");

        confirm.showAndWait().ifPresent(resp -> {
            if (resp == ButtonType.OK) {
                try (Connection conn = con.establecerConexion();
                     PreparedStatement ps = conn.prepareStatement(
                         "DELETE FROM tbl_maquinaria WHERE id_maquinaria=?")) {
                    ps.setInt(1, sel.getIdMaquinaria());
                    ps.executeUpdate();
                    lista.remove(sel);
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

        for (maquinariaModelo m : lista) {
            if (m.getIdMaquinaria() == idBuscar) {
                tablaMaquinarias.getSelectionModel().select(m);
                tablaMaquinarias.scrollTo(m);
                cargarEnFormulario(m);
                return;
            }
        }
        mostrarAlerta(Alert.AlertType.WARNING, "No encontrado",
            "No existe una maquinaria con el ID " + idBuscar + ".");
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
        tablaMaquinarias.getSelectionModel().clearSelection();
    }

    private void cargarMaquinarias() {
        lista.clear();
        String sql = "SELECT id_maquinaria, nombre, tipo, marca_modelo, numero_serie, " +
                     "fecha_adquisicion, estado, id_responsable, responsable " +
                     "FROM tbl_maquinaria ORDER BY nombre";
        try (Connection conn = con.establecerConexion();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Date d = rs.getDate("fecha_adquisicion");
                lista.add(new maquinariaModelo(
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
            }
        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error al cargar maquinarias", e.getMessage());
        }
    }

    private void cargarEnFormulario(maquinariaModelo m) {
        txtId.setText(String.valueOf(m.getIdMaquinaria()));
        txtNombre.setText(m.getNombre());
        cbTipo.setValue(m.getTipo());
        txtMarcaModelo.setText(m.getMarcaModelo());
        txtNumeroSerie.setText(m.getNumeroSerie());
        dpFechaAdquisicion.setValue(m.getFechaAdquisicion());
        cbEstado.setValue(m.getEstado());
        cbResponsable.setValue(m.getResponsable());
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
    @FXML private void irAReportesVentas(javafx.event.ActionEvent e)      { Navegacion.irA("/vistasFinales/vistaConsultas.fxml", e); }
    @FXML private void irAReportesCompras(javafx.event.ActionEvent e)     { Navegacion.irA("/vistasFinales/vistaConsultas.fxml", e); }
    @FXML private void irAReportesInventario(javafx.event.ActionEvent e)  { Navegacion.irA("/vistasFinales/vistaConsultas.fxml", e); }
    @FXML private void irAReportesProduccion(javafx.event.ActionEvent e)  { Navegacion.irA("/vistasFinales/vistaConsultas.fxml", e); }
    @FXML private void irAMantenimientoMaquinaria(javafx.event.ActionEvent e) { Navegacion.irA("/vistasFinales/vistaMantenimientoMaquinaria.fxml", e); }
    @FXML private void irAConsultas(javafx.event.ActionEvent e)           { Navegacion.irA("/vistasFinales/vistaConsultas.fxml", e); }
    @FXML private void salir(javafx.event.ActionEvent e)                  { Navegacion.salir(e); }
}