package com.example.chocolateria.controller;

import com.example.chocolateria.baseDeDatos.conexion;
import com.example.chocolateria.modelo.empleadoModelo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;

import java.sql.*;

public class empleadoController {

    @FXML private TextField txtIdEmpleado;
    @FXML private TextField txtNombre;
    @FXML private TextField txtApellido;
    @FXML private TextField txtCedula;
    @FXML private TextField txtTelefono;
    @FXML private ComboBox<String> cbTipoEmpleado;
    @FXML private ComboBox<String> cbRol;
    @FXML private TextField txtBuscarTabla;

    @FXML private TableView<empleadoModelo>           tablaEmpleados;
    @FXML private TableColumn<empleadoModelo, Number> colId;
    @FXML private TableColumn<empleadoModelo, String> colNombre;
    @FXML private TableColumn<empleadoModelo, String> colApellido;
    @FXML private TableColumn<empleadoModelo, String> colCedula;
    @FXML private TableColumn<empleadoModelo, String> colTelefono;
    @FXML private TableColumn<empleadoModelo, String> colTipoEmpleado;
    @FXML private TableColumn<empleadoModelo, String> colRol;
    @FXML private TableColumn<empleadoModelo, String> colEstado;

    private final ObservableList<empleadoModelo> listaEmpleados = FXCollections.observableArrayList();
    private final conexion con = new conexion();

    @FXML private Label lblUsuario;
    @FXML private ImageView imgFotoPerfil;

    @FXML
    public void initialize() {
        CargarPerfil.aplicar(lblUsuario, imgFotoPerfil);

        cbTipoEmpleado.getItems().addAll("Supervisor", "Empleado", "Maestro Chocolatero");
        cbRol.getItems().addAll("Administrador", "Usuario", "Supervisor");

        colId.setCellValueFactory(d           -> d.getValue().idEmpleadoProperty());
        colNombre.setCellValueFactory(d       -> d.getValue().nombreProperty());
        colApellido.setCellValueFactory(d     -> d.getValue().apellidoProperty());
        colCedula.setCellValueFactory(d       -> d.getValue().cedulaProperty());
        colTelefono.setCellValueFactory(d     -> d.getValue().telefonoProperty());
        colTipoEmpleado.setCellValueFactory(d -> d.getValue().tipoEmpleadoProperty());
        colRol.setCellValueFactory(d          -> d.getValue().rolProperty());
        colEstado.setCellValueFactory(d       -> d.getValue().estadoProperty());

        tablaEmpleados.setRowFactory(tv -> new TableRow<>() {
            @Override
            protected void updateItem(empleadoModelo e, boolean empty) {
                super.updateItem(e, empty);
                if (e == null || empty) {
                    setStyle("");
                } else if ("Inactivo".equalsIgnoreCase(e.getEstado())) {
                    setStyle("-fx-background-color:#e0e0e0; -fx-text-fill:#888;");
                } else {
                    setStyle("");
                }
            }
        });

        FilteredList<empleadoModelo> listaFiltrada = new FilteredList<>(listaEmpleados, p -> true);
        if (txtBuscarTabla != null) {
            txtBuscarTabla.textProperty().addListener((obs, oldVal, newVal) ->
                    listaFiltrada.setPredicate(e -> {
                        if (newVal == null || newVal.isBlank()) return true;
                        String f = newVal.toLowerCase();
                        return e.getNombre().toLowerCase().contains(f)
                                || e.getApellido().toLowerCase().contains(f)
                                || e.getCedula().toLowerCase().contains(f);
                    })
            );
        }
        tablaEmpleados.setItems(listaFiltrada);

        tablaEmpleados.getSelectionModel().selectedItemProperty().addListener(
                (obs, old, sel) -> { if (sel != null) cargarEnFormulario(sel); }
        );

        cargarEmpleados();
        generarSiguienteId();
    }

    @FXML
    public void guardarEmpleado() {
        if (!validarCampos()) return;

        String sql = "INSERT INTO tbl_empleado (nombre, apellido, cedula, telefono, tipo_empleado, rol, estado) " +
                "VALUES (?, ?, ?, ?, ?, ?, 'Activo')";

        try (Connection c = con.establecerConexion();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, txtNombre.getText().trim());
            ps.setString(2, txtApellido.getText().trim());
            ps.setString(3, txtCedula.getText().trim());
            ps.setString(4, txtTelefono.getText().trim());
            ps.setString(5, cbTipoEmpleado.getValue());
            ps.setString(6, cbRol.getValue());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            int nuevoId = rs.next() ? rs.getInt(1) : 0;

            listaEmpleados.add(new empleadoModelo(nuevoId,
                    txtNombre.getText().trim(), txtApellido.getText().trim(),
                    txtCedula.getText().trim(), txtTelefono.getText().trim(),
                    cbTipoEmpleado.getValue(), cbRol.getValue(), "Activo"));

            mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Empleado registrado correctamente.");
            limpiar();

        } catch (SQLException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error al guardar", e.getMessage());
        }
    }

    @FXML
    private void fnEditar() {
        empleadoModelo sel = tablaEmpleados.getSelectionModel().getSelectedItem();
        if (sel == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Atención", "Selecciona un empleado de la tabla para editar.");
            return;
        }
        if (!validarCampos()) return;

        sel.setNombre(txtNombre.getText().trim());
        sel.setApellido(txtApellido.getText().trim());
        sel.setCedula(txtCedula.getText().trim());
        sel.setTelefono(txtTelefono.getText().trim());
        sel.setTipoEmpleado(cbTipoEmpleado.getValue());
        sel.setRol(cbRol.getValue());

        String sql = "UPDATE tbl_empleado SET nombre=?, apellido=?, cedula=?, telefono=?, tipo_empleado=?, rol=? WHERE id_empleado=?";

        try (Connection c = con.establecerConexion();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, sel.getNombre());
            ps.setString(2, sel.getApellido());
            ps.setString(3, sel.getCedula());
            ps.setString(4, sel.getTelefono());
            ps.setString(5, sel.getTipoEmpleado());
            ps.setString(6, sel.getRol());
            ps.setInt(7, sel.getIdEmpleado());
            ps.executeUpdate();

            tablaEmpleados.refresh();
            mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Empleado actualizado correctamente.");
            limpiar();

        } catch (SQLException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error al editar", e.getMessage());
        }
    }

    @FXML
    private void fnEliminar() {
        empleadoModelo sel = tablaEmpleados.getSelectionModel().getSelectedItem();
        if (sel == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Atención", "Selecciona un empleado de la tabla para eliminar.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirmar eliminación");
        confirm.setHeaderText(null);
        confirm.setContentText("¿Eliminar a " + sel.getNombre() + " " + sel.getApellido() + "?");

        confirm.showAndWait().ifPresent(resp -> {
            if (resp == ButtonType.OK) {
                try (Connection c = con.establecerConexion();
                     PreparedStatement ps = c.prepareStatement("DELETE FROM tbl_empleado WHERE id_empleado=?")) {

                    ps.setInt(1, sel.getIdEmpleado());
                    ps.executeUpdate();
                    listaEmpleados.remove(sel);
                    mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Empleado eliminado correctamente.");
                    limpiar();

                } catch (SQLException e) {
                    mostrarAlerta(Alert.AlertType.ERROR, "Error al eliminar", e.getMessage());
                }
            }
        });
    }

    @FXML
    private void fnDeshabilitar() {
        empleadoModelo sel = tablaEmpleados.getSelectionModel().getSelectedItem();
        if (sel == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Atención", "Selecciona un empleado de la tabla.");
            return;
        }

        String nuevoEstado = "Activo".equalsIgnoreCase(sel.getEstado()) ? "Inactivo" : "Activo";

        try (Connection c = con.establecerConexion();
             PreparedStatement ps = c.prepareStatement("UPDATE tbl_empleado SET estado=? WHERE id_empleado=?")) {

            ps.setString(1, nuevoEstado);
            ps.setInt(2, sel.getIdEmpleado());
            ps.executeUpdate();

            sel.setEstado(nuevoEstado);
            tablaEmpleados.refresh();
            mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Empleado marcado como " + nuevoEstado + ".");

        } catch (SQLException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", e.getMessage());
        }
    }

    @FXML
    private void fnBuscar() {
        String idTexto = txtIdEmpleado.getText().trim();
        if (idTexto.isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Atención", "Escribe un ID en el campo 'ID Empleado' y presiona Buscar.");
            return;
        }

        int idBuscar;
        try {
            idBuscar = Integer.parseInt(idTexto);
        } catch (NumberFormatException ex) {
            mostrarAlerta(Alert.AlertType.WARNING, "ID inválido", "El ID debe ser un número entero.");
            return;
        }

        for (empleadoModelo e : listaEmpleados) {
            if (e.getIdEmpleado() == idBuscar) {
                tablaEmpleados.getSelectionModel().select(e);
                tablaEmpleados.scrollTo(e);
                cargarEnFormulario(e);
                return;
            }
        }

        String sql = "SELECT id_empleado, nombre, apellido, cedula, telefono, tipo_empleado, rol, estado FROM tbl_empleado WHERE id_empleado = ?";
        try (Connection c = con.establecerConexion();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, idBuscar);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                cargarEnFormulario(new empleadoModelo(
                        rs.getInt("id_empleado"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("cedula"),
                        rs.getString("telefono"),
                        rs.getString("tipo_empleado"),
                        rs.getString("rol"),
                        rs.getString("estado")
                ));
                mostrarAlerta(Alert.AlertType.INFORMATION, "Encontrado", "Empleado encontrado y cargado en el formulario.");
            } else {
                mostrarAlerta(Alert.AlertType.WARNING, "No encontrado", "No existe ningún empleado con el ID " + idBuscar + ".");
            }

        } catch (SQLException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error de búsqueda", e.getMessage());
        }
    }

    @FXML
    public void limpiar() {
        txtNombre.clear();
        txtApellido.clear();
        txtCedula.clear();
        txtTelefono.clear();
        cbTipoEmpleado.setValue(null);
        cbRol.setValue(null);
        tablaEmpleados.getSelectionModel().clearSelection();
        generarSiguienteId();
    }

    private void cargarEmpleados() {
        listaEmpleados.clear();
        String sql = "SELECT id_empleado, nombre, apellido, cedula, telefono, tipo_empleado, rol, estado FROM tbl_empleado";
        try (Connection c = con.establecerConexion();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                listaEmpleados.add(new empleadoModelo(
                        rs.getInt("id_empleado"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("cedula"),
                        rs.getString("telefono"),
                        rs.getString("tipo_empleado"),
                        rs.getString("rol"),
                        rs.getString("estado")
                ));
            }
        } catch (SQLException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error al cargar empleados", e.getMessage());
        }
    }

    private void cargarEnFormulario(empleadoModelo e) {
        txtIdEmpleado.setText(String.valueOf(e.getIdEmpleado()));
        txtNombre.setText(e.getNombre());
        txtApellido.setText(e.getApellido());
        txtCedula.setText(e.getCedula());
        txtTelefono.setText(e.getTelefono());
        cbTipoEmpleado.setValue(e.getTipoEmpleado());
        cbRol.setValue(e.getRol());
    }

    private void generarSiguienteId() {
        String sql = "SELECT ISNULL(MAX(id_empleado), 0) + 1 AS siguiente FROM tbl_empleado";
        try (Connection c = con.establecerConexion();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            if (rs.next()) txtIdEmpleado.setText(String.valueOf(rs.getInt("siguiente")));

        } catch (SQLException e) {
            txtIdEmpleado.setText("1");
        }
    }

    private boolean validarCampos() {
        if (txtNombre.getText().trim().isEmpty()   ||
                txtApellido.getText().trim().isEmpty()  ||
                txtCedula.getText().trim().isEmpty()    ||
                txtTelefono.getText().trim().isEmpty()  ||
                cbTipoEmpleado.getValue() == null       ||
                cbRol.getValue() == null) {

            mostrarAlerta(Alert.AlertType.WARNING, "Campos vacíos", "Por favor completa todos los campos antes de continuar.");
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