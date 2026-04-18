package com.example.chocolateria.controller;

import com.example.chocolateria.baseDeDatos.conexion;
import com.example.chocolateria.modelo.clienteModelo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;

import java.sql.*;

public class registrarclienteController {

    @FXML private TextField txtIdCliente;
    @FXML private TextField txtNombre;
    @FXML private TextField txtApellido;
    @FXML private TextField txtCedula;
    @FXML private TextField txtEmail;
    @FXML private TextField txtTelefono;
    @FXML private TextField txtDireccion;
    @FXML private TextField txtBuscarTabla;
    @FXML private ImageView imgBanner;

    @FXML private TableView<clienteModelo>           tablaClientes;
    @FXML private TableColumn<clienteModelo, Number> colId;
    @FXML private TableColumn<clienteModelo, String> colNombre;
    @FXML private TableColumn<clienteModelo, String> colApellido;
    @FXML private TableColumn<clienteModelo, String> colCedula;
    @FXML private TableColumn<clienteModelo, String> colEmail;
    @FXML private TableColumn<clienteModelo, String> colTelefono;
    @FXML private TableColumn<clienteModelo, String> colDireccion;
    @FXML private TableColumn<clienteModelo, String> colEstado;

    private final ObservableList<clienteModelo> listaClientes = FXCollections.observableArrayList();
    private final conexion con = new conexion();

    @FXML private Label lblUsuario;
    @FXML private ImageView imgFotoPerfil;

    @FXML
    public void initialize() {
        CargarPerfil.aplicar(lblUsuario, imgFotoPerfil);

        colId.setCellValueFactory(d        -> d.getValue().idClienteProperty());
        colNombre.setCellValueFactory(d    -> d.getValue().nombreProperty());
        colApellido.setCellValueFactory(d  -> d.getValue().apellidoProperty());
        colCedula.setCellValueFactory(d    -> d.getValue().cedulaProperty());
        colEmail.setCellValueFactory(d     -> d.getValue().emailProperty());
        colTelefono.setCellValueFactory(d  -> d.getValue().telefonoProperty());
        colDireccion.setCellValueFactory(d -> d.getValue().direccionProperty());
        colEstado.setCellValueFactory(d    -> d.getValue().estadoProperty());

        tablaClientes.setRowFactory(tv -> new TableRow<>() {
            @Override
            protected void updateItem(clienteModelo c, boolean empty) {
                super.updateItem(c, empty);
                if (c == null || empty) {
                    setStyle("");
                } else if ("Inactivo".equalsIgnoreCase(c.getEstado())) {
                    setStyle("-fx-background-color:#e0e0e0; -fx-text-fill:#888;");
                } else {
                    setStyle("");
                }
            }
        });

        FilteredList<clienteModelo> listaFiltrada = new FilteredList<>(listaClientes, p -> true);
        if (txtBuscarTabla != null) {
            txtBuscarTabla.textProperty().addListener((obs, oldVal, newVal) ->
                    listaFiltrada.setPredicate(c -> {
                        if (newVal == null || newVal.isBlank()) return true;
                        String f = newVal.toLowerCase();
                        return c.getNombre().toLowerCase().contains(f)
                                || c.getApellido().toLowerCase().contains(f)
                                || c.getCedula().toLowerCase().contains(f)
                                || c.getEmail().toLowerCase().contains(f);
                    })
            );
        }
        tablaClientes.setItems(listaFiltrada);

        tablaClientes.getSelectionModel().selectedItemProperty().addListener(
                (obs, old, sel) -> { if (sel != null) cargarEnFormulario(sel); }
        );

        imgBanner.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                imgBanner.fitWidthProperty().bind(newScene.widthProperty().subtract(190));
            }
        });

        cargarClientes();
        generarSiguienteId();
    }

    @FXML
    private void guardarCliente() {
        if (!validarCampos()) return;

        String nombre    = txtNombre.getText().trim();
        String apellido  = txtApellido.getText().trim();
        String cedula    = txtCedula.getText().trim();
        String email     = txtEmail.getText().trim();
        String telefono  = txtTelefono.getText().trim();
        String direccion = txtDireccion.getText().trim();

        String sql = "INSERT INTO tbl_clientes (nombre, apellido, cedula, email, telefono, direccion, estado) " +
                "VALUES (?, ?, ?, ?, ?, ?, 'Activo')";

        try (Connection c = con.establecerConexion();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, nombre);
            ps.setString(2, apellido);
            ps.setString(3, cedula);
            ps.setString(4, email);
            ps.setString(5, telefono);
            ps.setString(6, direccion);
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            int nuevoId = rs.next() ? rs.getInt(1) : 0;

            listaClientes.add(new clienteModelo(nuevoId, nombre, apellido, cedula, email, telefono, direccion, "Activo"));
            mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Cliente registrado correctamente.");
            limpiarCampos();

        } catch (SQLException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error al guardar", e.getMessage());
        }
    }

    @FXML
    private void fnEditar() {
        clienteModelo sel = tablaClientes.getSelectionModel().getSelectedItem();
        if (sel == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Atención", "Selecciona un cliente de la tabla para editar.");
            return;
        }
        if (!validarCampos()) return;

        sel.setNombre(txtNombre.getText().trim());
        sel.setApellido(txtApellido.getText().trim());
        sel.setCedula(txtCedula.getText().trim());
        sel.setEmail(txtEmail.getText().trim());
        sel.setTelefono(txtTelefono.getText().trim());
        sel.setDireccion(txtDireccion.getText().trim());

        String sql = "UPDATE tbl_clientes SET nombre=?, apellido=?, cedula=?, email=?, telefono=?, direccion=? WHERE id_cliente=?";

        try (Connection c = con.establecerConexion();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, sel.getNombre());
            ps.setString(2, sel.getApellido());
            ps.setString(3, sel.getCedula());
            ps.setString(4, sel.getEmail());
            ps.setString(5, sel.getTelefono());
            ps.setString(6, sel.getDireccion());
            ps.setInt(7, sel.getIdCliente());
            ps.executeUpdate();

            tablaClientes.refresh();
            mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Cliente actualizado correctamente.");
            limpiarCampos();

        } catch (SQLException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error al editar", e.getMessage());
        }
    }

    @FXML
    private void fnEliminar() {
        clienteModelo sel = tablaClientes.getSelectionModel().getSelectedItem();
        if (sel == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Atención", "Selecciona un cliente de la tabla para eliminar.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirmar eliminación");
        confirm.setHeaderText(null);
        confirm.setContentText("¿Eliminar a " + sel.getNombre() + " " + sel.getApellido() + "?");

        confirm.showAndWait().ifPresent(resp -> {
            if (resp == ButtonType.OK) {
                try (Connection c = con.establecerConexion();
                     PreparedStatement ps = c.prepareStatement("DELETE FROM tbl_clientes WHERE id_cliente=?")) {

                    ps.setInt(1, sel.getIdCliente());
                    ps.executeUpdate();
                    listaClientes.remove(sel);
                    mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Cliente eliminado correctamente.");
                    limpiarCampos();

                } catch (SQLException e) {
                    mostrarAlerta(Alert.AlertType.ERROR, "Error al eliminar", e.getMessage());
                }
            }
        });
    }

    @FXML
    private void fnDeshabilitar() {
        clienteModelo sel = tablaClientes.getSelectionModel().getSelectedItem();
        if (sel == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Atención", "Selecciona un cliente de la tabla.");
            return;
        }

        String nuevoEstado = "Activo".equalsIgnoreCase(sel.getEstado()) ? "Inactivo" : "Activo";

        try (Connection c = con.establecerConexion();
             PreparedStatement ps = c.prepareStatement("UPDATE tbl_clientes SET estado=? WHERE id_cliente=?")) {

            ps.setString(1, nuevoEstado);
            ps.setInt(2, sel.getIdCliente());
            ps.executeUpdate();

            sel.setEstado(nuevoEstado);
            tablaClientes.refresh();
            mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Cliente marcado como " + nuevoEstado + ".");

        } catch (SQLException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", e.getMessage());
        }
    }

    @FXML
    private void fnBuscar() {
        String idTexto = txtIdCliente.getText().trim();
        if (idTexto.isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Atención", "Escribe un ID en el campo 'ID Cliente' y presiona Buscar.");
            return;
        }

        int idBuscar;
        try {
            idBuscar = Integer.parseInt(idTexto);
        } catch (NumberFormatException ex) {
            mostrarAlerta(Alert.AlertType.WARNING, "ID inválido", "El ID debe ser un número entero.");
            return;
        }

        for (clienteModelo c : listaClientes) {
            if (c.getIdCliente() == idBuscar) {
                tablaClientes.getSelectionModel().select(c);
                tablaClientes.scrollTo(c);
                cargarEnFormulario(c);
                return;
            }
        }

        String sql = "SELECT id_cliente, nombre, apellido, cedula, email, telefono, direccion, estado FROM tbl_clientes WHERE id_cliente = ?";
        try (Connection c = con.establecerConexion();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, idBuscar);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                cargarEnFormulario(new clienteModelo(
                        rs.getInt("id_cliente"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("cedula"),
                        rs.getString("email"),
                        rs.getString("telefono"),
                        rs.getString("direccion"),
                        rs.getString("estado")
                ));
                mostrarAlerta(Alert.AlertType.INFORMATION, "Encontrado", "Cliente encontrado y cargado en el formulario.");
            } else {
                mostrarAlerta(Alert.AlertType.WARNING, "No encontrado", "No existe ningún cliente con el ID " + idBuscar + ".");
            }

        } catch (SQLException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error de búsqueda", e.getMessage());
        }
    }

    @FXML
    private void limpiarCampos() {
        txtNombre.clear();
        txtApellido.clear();
        txtCedula.clear();
        txtEmail.clear();
        txtTelefono.clear();
        txtDireccion.clear();
        tablaClientes.getSelectionModel().clearSelection();
        generarSiguienteId();
    }

    private void cargarClientes() {
        listaClientes.clear();
        String sql = "SELECT id_cliente, nombre, apellido, cedula, email, telefono, direccion, estado FROM tbl_clientes";
        try (Connection c = con.establecerConexion();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                listaClientes.add(new clienteModelo(
                        rs.getInt("id_cliente"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("cedula"),
                        rs.getString("email"),
                        rs.getString("telefono"),
                        rs.getString("direccion"),
                        rs.getString("estado")
                ));
            }
        } catch (SQLException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error al cargar clientes", e.getMessage());
        }
    }

    private void cargarEnFormulario(clienteModelo c) {
        txtIdCliente.setText(String.valueOf(c.getIdCliente()));
        txtNombre.setText(c.getNombre());
        txtApellido.setText(c.getApellido());
        txtCedula.setText(c.getCedula());
        txtEmail.setText(c.getEmail());
        txtTelefono.setText(c.getTelefono());
        txtDireccion.setText(c.getDireccion());
    }

    private void generarSiguienteId() {
        String sql = "SELECT ISNULL(MAX(id_cliente), 0) + 1 AS siguiente FROM tbl_clientes";
        try (Connection c = con.establecerConexion();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            if (rs.next()) txtIdCliente.setText(String.valueOf(rs.getInt("siguiente")));

        } catch (SQLException e) {
            txtIdCliente.setText("1");
        }
    }

    private boolean validarCampos() {
        if (txtNombre.getText().trim().isEmpty()   ||
                txtApellido.getText().trim().isEmpty()  ||
                txtCedula.getText().trim().isEmpty()    ||
                txtEmail.getText().trim().isEmpty()     ||
                txtTelefono.getText().trim().isEmpty()  ||
                txtDireccion.getText().trim().isEmpty()) {

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
    @FXML private void salir(javafx.event.ActionEvent e)                  { Navegacion.salir(e); }
}