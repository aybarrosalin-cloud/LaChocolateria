package com.example.chocolateria.controller;

import com.example.chocolateria.baseDeDatos.conexion;
import com.example.chocolateria.modelo.usuarioModelo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;

import java.sql.*;

public class crearUsuarioController {

    @FXML private TextField        txtIdUsuario;
    @FXML private TextField        txtUsuario;
    @FXML private TextField        txtPassword;
    @FXML private TextField        txtConfirmarPassword;
    @FXML private ComboBox<String> cbRol;
    @FXML private TextField        txtFotoPerfil;
    @FXML private TextField        txtBuscarTabla;

    @FXML private TableView<usuarioModelo>           tablaUsuarios;
    @FXML private TableColumn<usuarioModelo, Number> colId;
    @FXML private TableColumn<usuarioModelo, String> colUsuario;
    @FXML private TableColumn<usuarioModelo, String> colRol;
    @FXML private TableColumn<usuarioModelo, String> colEstado;

    @FXML private Label     lblUsuario;
    @FXML private ImageView imgFotoPerfil;

    private final ObservableList<usuarioModelo> listaUsuarios = FXCollections.observableArrayList();
    private final conexion con = new conexion();

    @FXML
    public void initialize() {
        CargarPerfil.aplicar(lblUsuario, imgFotoPerfil);

        if (!"Administrador".equalsIgnoreCase(SesionManager.getInstancia().getRol())) {
            mostrarAlerta(Alert.AlertType.ERROR, "Acceso denegado",
                    "Solo los administradores pueden gestionar usuarios del sistema.");
            desactivarFormulario();
            return;
        }

        cbRol.getItems().addAll("Administrador", "Usuario");

        colId.setCellValueFactory(d      -> d.getValue().idUsuarioProperty());
        colUsuario.setCellValueFactory(d -> d.getValue().usuarioProperty());
        colRol.setCellValueFactory(d     -> d.getValue().rolProperty());
        colEstado.setCellValueFactory(d  -> d.getValue().estadoProperty());

        tablaUsuarios.setRowFactory(tv -> new TableRow<>() {
            @Override
            protected void updateItem(usuarioModelo u, boolean empty) {
                super.updateItem(u, empty);
                if (u == null || empty) setStyle("");
                else if ("Inactivo".equalsIgnoreCase(u.getEstado()))
                    setStyle("-fx-background-color:#e0e0e0; -fx-text-fill:#888;");
                else setStyle("");
            }
        });

        FilteredList<usuarioModelo> listaFiltrada = new FilteredList<>(listaUsuarios, p -> true);
        if (txtBuscarTabla != null) {
            txtBuscarTabla.textProperty().addListener((obs, o, n) ->
                listaFiltrada.setPredicate(u -> {
                    if (n == null || n.isBlank()) return true;
                    return u.getUsuario().toLowerCase().contains(n.toLowerCase());
                })
            );
        }
        tablaUsuarios.setItems(listaFiltrada);

        tablaUsuarios.getSelectionModel().selectedItemProperty()
                .addListener((obs, old, sel) -> { if (sel != null) cargarEnFormulario(sel); });

        cargarUsuarios();
        generarSiguienteId();
    }

    @FXML
    public void guardarUsuario() {
        if (!validarCampos()) return;

        if (!txtPassword.getText().equals(txtConfirmarPassword.getText())) {
            mostrarAlerta(Alert.AlertType.WARNING, "Contraseñas distintas",
                    "La contraseña y su confirmación no coinciden.");
            return;
        }

        String sql = "INSERT INTO tbl_usuario (usuario, password, foto_perfil, rol, estado) VALUES (?, ?, ?, ?, 'Activo')";
        try (Connection c = con.establecerConexion();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, txtUsuario.getText().trim());
            ps.setString(2, txtPassword.getText());
            ps.setString(3, txtFotoPerfil.getText().trim());
            ps.setString(4, cbRol.getValue());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            int nuevoId  = rs.next() ? rs.getInt(1) : 0;

            listaUsuarios.add(new usuarioModelo(nuevoId, txtUsuario.getText().trim(), cbRol.getValue(), "Activo"));
            mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Usuario creado correctamente.");
            limpiar();

        } catch (SQLException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error al guardar", e.getMessage());
        }
    }

    @FXML
    private void fnEditar() {
        usuarioModelo sel = tablaUsuarios.getSelectionModel().getSelectedItem();
        if (sel == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Atención", "Selecciona un usuario de la tabla para editar.");
            return;
        }
        if (!validarCamposEdicion()) return;

        boolean cambiarPassword = !txtPassword.getText().isBlank();
        if (cambiarPassword && !txtPassword.getText().equals(txtConfirmarPassword.getText())) {
            mostrarAlerta(Alert.AlertType.WARNING, "Contraseñas distintas",
                    "La contraseña y su confirmación no coinciden.");
            return;
        }

        String sql = cambiarPassword
            ? "UPDATE tbl_usuario SET usuario=?, password=?, foto_perfil=?, rol=? WHERE id_usuario=?"
            : "UPDATE tbl_usuario SET usuario=?, foto_perfil=?, rol=? WHERE id_usuario=?";

        try (Connection c = con.establecerConexion();
             PreparedStatement ps = c.prepareStatement(sql)) {

            if (cambiarPassword) {
                ps.setString(1, txtUsuario.getText().trim());
                ps.setString(2, txtPassword.getText());
                ps.setString(3, txtFotoPerfil.getText().trim());
                ps.setString(4, cbRol.getValue());
                ps.setInt(5, sel.getIdUsuario());
            } else {
                ps.setString(1, txtUsuario.getText().trim());
                ps.setString(2, txtFotoPerfil.getText().trim());
                ps.setString(3, cbRol.getValue());
                ps.setInt(4, sel.getIdUsuario());
            }
            ps.executeUpdate();

            sel.setRol(cbRol.getValue());
            tablaUsuarios.refresh();
            mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Usuario actualizado correctamente.");
            limpiar();

        } catch (SQLException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error al editar", e.getMessage());
        }
    }

    @FXML
    private void fnEliminar() {
        usuarioModelo sel = tablaUsuarios.getSelectionModel().getSelectedItem();
        if (sel == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Atención", "Selecciona un usuario de la tabla para eliminar.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirmar eliminación");
        confirm.setHeaderText(null);
        confirm.setContentText("¿Eliminar al usuario '" + sel.getUsuario() + "'?");

        confirm.showAndWait().ifPresent(resp -> {
            if (resp == ButtonType.OK) {
                try (Connection c = con.establecerConexion();
                     PreparedStatement ps = c.prepareStatement("DELETE FROM tbl_usuario WHERE id_usuario=?")) {

                    ps.setInt(1, sel.getIdUsuario());
                    ps.executeUpdate();
                    listaUsuarios.remove(sel);
                    mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Usuario eliminado correctamente.");
                    limpiar();

                } catch (SQLException e) {
                    mostrarAlerta(Alert.AlertType.ERROR, "Error al eliminar", e.getMessage());
                }
            }
        });
    }

    @FXML
    private void fnDeshabilitar() {
        usuarioModelo sel = tablaUsuarios.getSelectionModel().getSelectedItem();
        if (sel == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Atención", "Selecciona un usuario de la tabla.");
            return;
        }

        String nuevoEstado = "Activo".equalsIgnoreCase(sel.getEstado()) ? "Inactivo" : "Activo";

        try (Connection c = con.establecerConexion();
             PreparedStatement ps = c.prepareStatement("UPDATE tbl_usuario SET estado=? WHERE id_usuario=?")) {

            ps.setString(1, nuevoEstado);
            ps.setInt(2, sel.getIdUsuario());
            ps.executeUpdate();

            sel.setEstado(nuevoEstado);
            tablaUsuarios.refresh();
            mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Usuario marcado como " + nuevoEstado + ".");

        } catch (SQLException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", e.getMessage());
        }
    }

    @FXML
    private void fnBuscar() {
        String idTexto = txtIdUsuario.getText().trim();
        if (idTexto.isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Atención",
                    "Escribe un ID en el campo 'ID Usuario' y presiona Buscar.");
            return;
        }

        int idBuscar;
        try {
            idBuscar = Integer.parseInt(idTexto);
        } catch (NumberFormatException ex) {
            mostrarAlerta(Alert.AlertType.WARNING, "ID inválido", "El ID debe ser un número entero.");
            return;
        }

        for (usuarioModelo u : listaUsuarios) {
            if (u.getIdUsuario() == idBuscar) {
                tablaUsuarios.getSelectionModel().select(u);
                tablaUsuarios.scrollTo(u);
                cargarEnFormulario(u);
                return;
            }
        }

        String sql = "SELECT id_usuario, usuario, rol, estado FROM tbl_usuario WHERE id_usuario=?";
        try (Connection c = con.establecerConexion();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, idBuscar);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                cargarEnFormulario(new usuarioModelo(
                        rs.getInt("id_usuario"), rs.getString("usuario"),
                        rs.getString("rol"), rs.getString("estado")));
                mostrarAlerta(Alert.AlertType.INFORMATION, "Encontrado",
                        "Usuario encontrado y cargado en el formulario.");
            } else {
                mostrarAlerta(Alert.AlertType.WARNING, "No encontrado",
                        "No existe ningún usuario con el ID " + idBuscar + ".");
            }
        } catch (SQLException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error de búsqueda", e.getMessage());
        }
    }

    @FXML
    public void limpiar() {
        txtUsuario.clear();
        txtPassword.clear();
        txtConfirmarPassword.clear();
        txtFotoPerfil.clear();
        cbRol.setValue(null);
        tablaUsuarios.getSelectionModel().clearSelection();
        generarSiguienteId();
    }

    private void cargarUsuarios() {
        listaUsuarios.clear();
        String sql = "SELECT id_usuario, usuario, rol, estado FROM tbl_usuario";
        try (Connection c = con.establecerConexion();
             Statement st  = c.createStatement();
             ResultSet rs  = st.executeQuery(sql)) {

            while (rs.next()) {
                listaUsuarios.add(new usuarioModelo(
                        rs.getInt("id_usuario"), rs.getString("usuario"),
                        rs.getString("rol"), rs.getString("estado")));
            }
        } catch (SQLException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error al cargar usuarios", e.getMessage());
        }
    }

    private void cargarEnFormulario(usuarioModelo u) {
        txtIdUsuario.setText(String.valueOf(u.getIdUsuario()));
        txtUsuario.setText(u.getUsuario());
        txtPassword.clear();
        txtConfirmarPassword.clear();
        cbRol.setValue(u.getRol());
        txtFotoPerfil.clear();
    }

    private void generarSiguienteId() {
        String sql = "SELECT ISNULL(MAX(id_usuario), 0) + 1 AS siguiente FROM tbl_usuario";
        try (Connection c = con.establecerConexion();
             Statement st  = c.createStatement();
             ResultSet rs  = st.executeQuery(sql)) {

            if (rs.next()) txtIdUsuario.setText(String.valueOf(rs.getInt("siguiente")));

        } catch (SQLException e) {
            txtIdUsuario.setText("1");
        }
    }

    private boolean validarCampos() {
        if (txtUsuario.getText().trim().isEmpty() ||
            txtPassword.getText().isBlank()        ||
            cbRol.getValue() == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Campos vacíos",
                    "Usuario, contraseña y rol son obligatorios.");
            return false;
        }
        return true;
    }

    private boolean validarCamposEdicion() {
        if (txtUsuario.getText().trim().isEmpty() || cbRol.getValue() == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Campos vacíos",
                    "Usuario y rol son obligatorios.");
            return false;
        }
        return true;
    }

    private void desactivarFormulario() {
        if (txtUsuario         != null) txtUsuario.setDisable(true);
        if (txtPassword        != null) txtPassword.setDisable(true);
        if (txtConfirmarPassword != null) txtConfirmarPassword.setDisable(true);
        if (cbRol              != null) cbRol.setDisable(true);
        if (txtFotoPerfil      != null) txtFotoPerfil.setDisable(true);
        if (tablaUsuarios      != null) tablaUsuarios.setDisable(true);
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    // ── Navegación ────────────────────────────────────────────────────────────
    @FXML private void irAInicio(ActionEvent e)              { Navegacion.irA("/vistasFinales/vistaInicio.fxml", e); }
    @FXML private void irAOrdenCliente(ActionEvent e)        { Navegacion.irA("/vistasFinales/vistaOrdenCliente.fxml", e); }
    @FXML private void irAPagoVenta(ActionEvent e)           { Navegacion.irA("/vistasFinales/vistaPagoVenta.fxml", e); }
    @FXML private void irAGestionEnvios(ActionEvent e)       { Navegacion.irA("/vistasFinales/vistaGestionEnvios.fxml", e); }
    @FXML private void irAGestionReclamos(ActionEvent e)     { Navegacion.irA("/vistasFinales/vistaGestionReclamos.fxml", e); }
    @FXML private void irASolicitudProduccion(ActionEvent e) { Navegacion.irA("/vistasFinales/vistaSolicitudDeProduccion.fxml", e); }
    @FXML private void irAOrdenProduccion(ActionEvent e)     { Navegacion.irA("/vistasFinales/vistaOrdenProduccion.fxml", e); }
    @FXML private void irASalidaMateriales(ActionEvent e)    { Navegacion.irA("/vistasFinales/vistaRecepcion.fxml", e); }
    @FXML private void irASalidaProductos(ActionEvent e)     { Navegacion.irA("/vistasFinales/vistaRecepcion.fxml", e); }
    @FXML private void irAOrdenProveedor(ActionEvent e)      { Navegacion.irA("/vistasFinales/vistaOrdenProveedor.fxml", e); }
    @FXML private void irAPagoCompra(ActionEvent e)          { Navegacion.irA("/vistasFinales/vistaPagoCompra.fxml", e); }
    @FXML private void irARegistroProducto(ActionEvent e)    { Navegacion.irA("/vistasFinales/vistaRegistroProducto.fxml", e); }
    @FXML private void irARegistroEmpleado(ActionEvent e)    { Navegacion.irA("/vistasFinales/vistaRegistroDeEmpleado.fxml", e); }
    @FXML private void irARegistroCliente(ActionEvent e)     { Navegacion.irA("/vistasFinales/vistaRegistroDeCliente.fxml", e); }
    @FXML private void irARegistroSuplidor(ActionEvent e)    { Navegacion.irA("/vistasFinales/vistaRegistroSuplidor.fxml", e); }
    @FXML private void irARegistroMaquinaria(ActionEvent e)  { Navegacion.irA("/vistasFinales/vistaRegistroMaquinaria.fxml", e); }
    @FXML private void irAReportesVentas(ActionEvent e)      { Navegacion.irA("/vistasFinales/vistaConsultas.fxml", e); }
    @FXML private void irAReportesCompras(ActionEvent e)     { Navegacion.irA("/vistasFinales/vistaConsultas.fxml", e); }
    @FXML private void irAReportesInventario(ActionEvent e)  { Navegacion.irA("/vistasFinales/vistaConsultas.fxml", e); }
    @FXML private void irAReportesProduccion(ActionEvent e)  { Navegacion.irA("/vistasFinales/vistaConsultas.fxml", e); }
    @FXML private void irAMantenimientoMaquinaria(ActionEvent e) { Navegacion.irA("/vistasFinales/vistaMantenimientoMaquinaria.fxml", e); }
    @FXML private void irAConsultas(ActionEvent e)           { Navegacion.irA("/vistasFinales/vistaConsultas.fxml", e); }
    @FXML private void irAGestionUsuarios(ActionEvent e)     { Navegacion.irA("/vistasFinales/vistaGestionUsuarios.fxml", e); }
    @FXML private void salir(ActionEvent e)                  { Navegacion.salir(e); }
}
