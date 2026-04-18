package com.example.chocolateria.controller;

import com.example.chocolateria.baseDeDatos.conexion;
import com.example.chocolateria.modelo.ordenClienteModelo;
import com.example.chocolateria.modelo.ordenDetalleModelo;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;

import java.sql.*;
import java.time.LocalDate;

public class ordenClienteController {

    // Formulario
    @FXML private TextField        txtCodigo;
    @FXML private ComboBox<String> cmbCliente;
    @FXML private DatePicker       dpFecha;
    @FXML private DatePicker       dpFecha1;
    @FXML private ChoiceBox<String> cbMetodoPago;
    @FXML private ChoiceBox<String> cbEstado;
    @FXML private TextArea         txtObservaciones;

    // Agregar producto
    @FXML private TextField txtIdProducto;
    @FXML private TextField txtNombreProducto;
    @FXML private TextField txtCantidad;
    @FXML private TextField txtPrecio;

    // Tabla detalle (productos de la orden en curso)
    @FXML private TableView<ordenDetalleModelo>               tablaDetalle;
    @FXML private TableColumn<ordenDetalleModelo, String>     colDetCodigo;
    @FXML private TableColumn<ordenDetalleModelo, String>     colDetProducto;
    @FXML private TableColumn<ordenDetalleModelo, Number>     colDetCantidad;
    @FXML private TableColumn<ordenDetalleModelo, Number>     colDetPrecio;

    // Tabla historial órdenes
    @FXML private TextField                                       txtBuscar;
    @FXML private TableView<ordenClienteModelo>                   tablaProductos;
    @FXML private TableColumn<ordenClienteModelo, Number>         colId;
    @FXML private TableColumn<ordenClienteModelo, String>         colCliente;
    @FXML private TableColumn<ordenClienteModelo, LocalDate>      colFechaReg;
    @FXML private TableColumn<ordenClienteModelo, LocalDate>      colFechaEnt;
    @FXML private TableColumn<ordenClienteModelo, String>         colEstadoTabla;
    @FXML private TableColumn<ordenClienteModelo, String>         colMetodoPago;
    @FXML private TableColumn<ordenClienteModelo, String>         colProductos;

    private final ObservableList<ordenClienteModelo>  listaOrdenes = FXCollections.observableArrayList();
    private final ObservableList<ordenDetalleModelo>  listaDetalle = FXCollections.observableArrayList();
    private final conexion con = new conexion();
    private int idClienteSeleccionado = 0;

    @FXML private Label lblUsuario;
    @FXML private ImageView imgFotoPerfil;

    @FXML
    public void initialize() {
        CargarPerfil.aplicar(lblUsuario, imgFotoPerfil);

        cbMetodoPago.setItems(FXCollections.observableArrayList(
            "Efectivo", "Transferencia", "Tarjeta", "Cheque", "Crédito"));

        cbEstado.setItems(FXCollections.observableArrayList(
            "Pendiente", "En proceso", "Completada", "Cancelada", "Entregada"));

        cargarClientes();

        // Columnas detalle
        colDetCodigo.setCellValueFactory(d   -> d.getValue().codigoProperty());
        colDetProducto.setCellValueFactory(d -> d.getValue().productoProperty());
        colDetCantidad.setCellValueFactory(d -> d.getValue().cantidadProperty());
        colDetPrecio.setCellValueFactory(d   -> d.getValue().precioProperty());
        tablaDetalle.setItems(listaDetalle);

        // Columnas historial
        colId.setCellValueFactory(d          -> d.getValue().idOrdenProperty());
        colCliente.setCellValueFactory(d     -> d.getValue().clienteProperty());
        colFechaReg.setCellValueFactory(d    -> d.getValue().fechaRegistroProperty());
        colFechaEnt.setCellValueFactory(d    -> d.getValue().fechaEntregaProperty());
        colEstadoTabla.setCellValueFactory(d -> d.getValue().estadoProperty());
        colMetodoPago.setCellValueFactory(d  -> d.getValue().metodoPagoProperty());
        colProductos.setCellValueFactory(d   ->
            new SimpleStringProperty(cargarResumenProductos(d.getValue().getIdOrden())));

        // Color por estado
        tablaProductos.setRowFactory(tv -> new TableRow<>() {
            @Override
            protected void updateItem(ordenClienteModelo o, boolean empty) {
                super.updateItem(o, empty);
                if (o == null || empty) { setStyle(""); return; }
                switch (o.getEstado()) {
                    case "Completada", "Entregada" -> setStyle("-fx-background-color:#e8f5e9;");
                    case "Cancelada"               -> setStyle("-fx-background-color:#fde8e8;");
                    case "En proceso"              -> setStyle("-fx-background-color:#fff8e1;");
                    default                        -> setStyle("");
                }
            }
        });

        // Filtro
        FilteredList<ordenClienteModelo> listaFiltrada = new FilteredList<>(listaOrdenes, p -> true);
        txtBuscar.textProperty().addListener((obs, oldVal, newVal) ->
            listaFiltrada.setPredicate(o -> {
                if (newVal == null || newVal.isBlank()) return true;
                String f = newVal.toLowerCase();
                return o.getCliente().toLowerCase().contains(f)
                    || o.getEstado().toLowerCase().contains(f)
                    || String.valueOf(o.getIdOrden()).contains(f);
            })
        );
        tablaProductos.setItems(listaFiltrada);

        // Click en historial → cargar formulario
        tablaProductos.getSelectionModel().selectedItemProperty().addListener(
            (obs, old, sel) -> {
                if (sel != null) {
                    cargarEnFormulario(sel);
                    cargarDetalle(sel.getIdOrden());
                }
            }
        );

        cargarOrdenes();
        generarSiguienteId();
    }

    private void cargarClientes() {
        try (Connection conn = con.establecerConexion();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(
                 "SELECT id_cliente, nombre + ' ' + apellido AS nombre_completo FROM tbl_clientes ORDER BY nombre")) {
            while (rs.next()) {
                String nombre = rs.getString("nombre_completo");
                cmbCliente.getItems().add(nombre);
            }
        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", e.getMessage());
        }
    }

    @FXML
    private void buscarProducto() {
        String codigo = txtIdProducto.getText().trim();
        if (codigo.isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Atención", "Escribe el código del producto.");
            return;
        }
        try (Connection conn = con.establecerConexion();
             PreparedStatement ps = conn.prepareStatement(
                 "SELECT nombre, precio_unitario FROM tbl_producto WHERE codigo = ?")) {
            ps.setString(1, codigo);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                txtNombreProducto.setText(rs.getString("nombre"));
                txtPrecio.setText(rs.getString("precio_unitario"));
            } else {
                txtNombreProducto.clear();
                txtPrecio.clear();
                mostrarAlerta(Alert.AlertType.WARNING, "No encontrado",
                    "No existe producto con código " + codigo + ".");
            }
        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", e.getMessage());
        }
    }

    @FXML
    private void agregarProducto() {
        if (txtNombreProducto.getText().trim().isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Atención", "Busca un producto antes de agregar.");
            return;
        }
        if (txtCantidad.getText().trim().isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Atención", "Ingresa la cantidad.");
            return;
        }
        try {
            int    cantidad = Integer.parseInt(txtCantidad.getText().trim());
            double precio   = txtPrecio.getText().trim().isEmpty()
                ? 0.0 : Double.parseDouble(txtPrecio.getText().trim());

            if (cantidad <= 0) {
                mostrarAlerta(Alert.AlertType.WARNING, "Cantidad inválida", "La cantidad debe ser mayor a 0.");
                return;
            }

            String codigo = txtIdProducto.getText().trim();
            for (ordenDetalleModelo d : listaDetalle) {
                if (d.getCodigo().equals(codigo)) {
                    mostrarAlerta(Alert.AlertType.WARNING, "Duplicado", "Este producto ya está en la lista.");
                    return;
                }
            }

            listaDetalle.add(new ordenDetalleModelo(0, 0, codigo,
                txtNombreProducto.getText().trim(), "", cantidad, precio));

            txtIdProducto.clear();
            txtNombreProducto.clear();
            txtCantidad.clear();
            txtPrecio.clear();

        } catch (NumberFormatException ex) {
            mostrarAlerta(Alert.AlertType.WARNING, "Valor inválido", "Cantidad y precio deben ser números válidos.");
        }
    }

    @FXML
    private void quitarProducto() {
        ordenDetalleModelo sel = tablaDetalle.getSelectionModel().getSelectedItem();
        if (sel == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Atención", "Selecciona un producto para quitarlo.");
            return;
        }
        listaDetalle.remove(sel);
    }

    @FXML
    private void guardar() {
        if (!validarCampos()) return;

        String sql = "INSERT INTO tbl_orden_cliente (id_cliente, cliente, fecha_registro, fecha_entrega, metodo_pago, estado, observaciones) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = con.establecerConexion();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            String clienteNombre = cmbCliente.getValue();
            int idCliente = buscarIdCliente(conn, clienteNombre);

            ps.setInt(1, idCliente);
            ps.setString(2, clienteNombre);
            ps.setDate(3, Date.valueOf(dpFecha.getValue()));
            ps.setObject(4, dpFecha1.getValue() != null ? Date.valueOf(dpFecha1.getValue()) : null);
            ps.setString(5, cbMetodoPago.getValue());
            ps.setString(6, cbEstado.getValue());
            ps.setString(7, txtObservaciones.getText().trim());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (!rs.next()) throw new SQLException("No se obtuvo ID de la orden.");
            int nuevoId = rs.getInt(1);

            // Insertar detalle
            String sqlDet = "INSERT INTO tbl_orden_detalle (id_orden, codigo, producto, categoria, cantidad, precio) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement psDet = conn.prepareStatement(sqlDet)) {
                for (ordenDetalleModelo d : listaDetalle) {
                    psDet.setInt(1, nuevoId);
                    psDet.setString(2, d.getCodigo());
                    psDet.setString(3, d.getProducto());
                    psDet.setString(4, d.getCategoria());
                    psDet.setInt(5, d.getCantidad());
                    psDet.setDouble(6, d.getPrecio());
                    psDet.addBatch();
                }
                psDet.executeBatch();
            }

            listaOrdenes.add(0, new ordenClienteModelo(nuevoId, idCliente, clienteNombre,
                dpFecha.getValue(), dpFecha1.getValue(),
                cbMetodoPago.getValue(), cbEstado.getValue(),
                txtObservaciones.getText().trim()));

            mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito",
                "Orden #" + nuevoId + " guardada con " + listaDetalle.size() + " producto(s).");
            limpiarCampos();

        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error al guardar", e.getMessage());
        }
    }

    @FXML
    private void fnEditar() {
        ordenClienteModelo sel = tablaProductos.getSelectionModel().getSelectedItem();
        if (sel == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Atención", "Selecciona una orden para editar.");
            return;
        }
        if (!validarCampos()) return;

        String sql = "UPDATE tbl_orden_cliente SET cliente=?, fecha_registro=?, fecha_entrega=?, " +
                     "metodo_pago=?, estado=?, observaciones=? WHERE id_orden=?";

        try (Connection conn = con.establecerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, cmbCliente.getValue());
            ps.setDate(2, Date.valueOf(dpFecha.getValue()));
            ps.setObject(3, dpFecha1.getValue() != null ? Date.valueOf(dpFecha1.getValue()) : null);
            ps.setString(4, cbMetodoPago.getValue());
            ps.setString(5, cbEstado.getValue());
            ps.setString(6, txtObservaciones.getText().trim());
            ps.setInt(7, sel.getIdOrden());
            ps.executeUpdate();

            // Reemplazar detalle
            try (PreparedStatement psDel = conn.prepareStatement(
                 "DELETE FROM tbl_orden_detalle WHERE id_orden=?")) {
                psDel.setInt(1, sel.getIdOrden());
                psDel.executeUpdate();
            }
            String sqlDet = "INSERT INTO tbl_orden_detalle (id_orden, codigo, producto, categoria, cantidad, precio) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement psDet = conn.prepareStatement(sqlDet)) {
                for (ordenDetalleModelo d : listaDetalle) {
                    psDet.setInt(1, sel.getIdOrden());
                    psDet.setString(2, d.getCodigo());
                    psDet.setString(3, d.getProducto());
                    psDet.setString(4, d.getCategoria());
                    psDet.setInt(5, d.getCantidad());
                    psDet.setDouble(6, d.getPrecio());
                    psDet.addBatch();
                }
                psDet.executeBatch();
            }

            sel.setCliente(cmbCliente.getValue());
            sel.setFechaRegistro(dpFecha.getValue());
            sel.setFechaEntrega(dpFecha1.getValue());
            sel.setMetodoPago(cbMetodoPago.getValue());
            sel.setEstado(cbEstado.getValue());
            sel.setObservaciones(txtObservaciones.getText().trim());
            tablaProductos.refresh();

            mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Orden actualizada correctamente.");
            limpiarCampos();

        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error al editar", e.getMessage());
        }
    }

    @FXML
    private void fnEliminar() {
        ordenClienteModelo sel = tablaProductos.getSelectionModel().getSelectedItem();
        if (sel == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Atención", "Selecciona una orden para eliminar.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirmar eliminación");
        confirm.setHeaderText(null);
        confirm.setContentText("¿Eliminar la orden #" + sel.getIdOrden() + " de " + sel.getCliente() + "?");

        confirm.showAndWait().ifPresent(resp -> {
            if (resp == ButtonType.OK) {
                try (Connection conn = con.establecerConexion()) {
                    try (PreparedStatement ps = conn.prepareStatement(
                         "DELETE FROM tbl_orden_detalle WHERE id_orden=?")) {
                        ps.setInt(1, sel.getIdOrden());
                        ps.executeUpdate();
                    }
                    try (PreparedStatement ps = conn.prepareStatement(
                         "DELETE FROM tbl_orden_cliente WHERE id_orden=?")) {
                        ps.setInt(1, sel.getIdOrden());
                        ps.executeUpdate();
                    }
                    listaOrdenes.remove(sel);
                    listaDetalle.clear();
                    mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Orden eliminada correctamente.");
                    limpiarCampos();
                } catch (Exception e) {
                    mostrarAlerta(Alert.AlertType.ERROR, "Error al eliminar", e.getMessage());
                }
            }
        });
    }

    @FXML
    private void fnBuscar() {
        String idTexto = txtCodigo.getText().trim();
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
        for (ordenClienteModelo o : listaOrdenes) {
            if (o.getIdOrden() == idBuscar) {
                tablaProductos.getSelectionModel().select(o);
                tablaProductos.scrollTo(o);
                cargarEnFormulario(o);
                cargarDetalle(o.getIdOrden());
                return;
            }
        }
        mostrarAlerta(Alert.AlertType.WARNING, "No encontrado",
            "No existe una orden con el ID " + idBuscar + ".");
    }

    @FXML
    private void limpiarCampos() {
        txtCodigo.clear();
        cmbCliente.setValue(null);
        dpFecha.setValue(null);
        dpFecha1.setValue(null);
        cbMetodoPago.setValue(null);
        cbEstado.setValue(null);
        txtObservaciones.clear();
        txtIdProducto.clear();
        txtNombreProducto.clear();
        txtCantidad.clear();
        txtPrecio.clear();
        listaDetalle.clear();
        tablaProductos.getSelectionModel().clearSelection();
        generarSiguienteId();
    }

    private void cargarOrdenes() {
        listaOrdenes.clear();
        String sql = "SELECT o.id_orden, o.id_cliente, o.cliente, o.fecha_registro, o.fecha_entrega, " +
                     "o.metodo_pago, o.estado, o.observaciones " +
                     "FROM tbl_orden_cliente o ORDER BY o.fecha_registro DESC";
        try (Connection conn = con.establecerConexion();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Date dReg = rs.getDate("fecha_registro");
                Date dEnt = rs.getDate("fecha_entrega");
                listaOrdenes.add(new ordenClienteModelo(
                    rs.getInt("id_orden"),
                    rs.getInt("id_cliente"),
                    rs.getString("cliente"),
                    dReg != null ? dReg.toLocalDate() : null,
                    dEnt != null ? dEnt.toLocalDate() : null,
                    rs.getString("metodo_pago"),
                    rs.getString("estado"),
                    rs.getString("observaciones") != null ? rs.getString("observaciones") : ""
                ));
            }
        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error al cargar órdenes", e.getMessage());
        }
    }

    private void cargarDetalle(int idOrden) {
        listaDetalle.clear();
        String sql = "SELECT id_detalle, id_orden, codigo, producto, categoria, cantidad, precio " +
                     "FROM tbl_orden_detalle WHERE id_orden = ?";
        try (Connection conn = con.establecerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idOrden);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                listaDetalle.add(new ordenDetalleModelo(
                    rs.getInt("id_detalle"),
                    rs.getInt("id_orden"),
                    rs.getString("codigo"),
                    rs.getString("producto"),
                    rs.getString("categoria") != null ? rs.getString("categoria") : "",
                    rs.getInt("cantidad"),
                    rs.getDouble("precio")
                ));
            }
        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error al cargar detalle", e.getMessage());
        }
    }

    private String cargarResumenProductos(int idOrden) {
        StringBuilder sb = new StringBuilder();
        String sql = "SELECT producto FROM tbl_orden_detalle WHERE id_orden = ?";
        try (Connection conn = con.establecerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idOrden);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                if (sb.length() > 0) sb.append(", ");
                sb.append(rs.getString("producto"));
            }
        } catch (Exception ignored) {}
        return sb.toString();
    }

    private int buscarIdCliente(Connection conn, String nombreCompleto) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(
             "SELECT id_cliente FROM tbl_clientes WHERE nombre + ' ' + apellido = ?")) {
            ps.setString(1, nombreCompleto);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt("id_cliente");
        }
        return 0;
    }

    private void cargarEnFormulario(ordenClienteModelo o) {
        txtCodigo.setText(String.valueOf(o.getIdOrden()));
        cmbCliente.setValue(o.getCliente());
        dpFecha.setValue(o.getFechaRegistro());
        dpFecha1.setValue(o.getFechaEntrega());
        cbMetodoPago.setValue(o.getMetodoPago());
        cbEstado.setValue(o.getEstado());
        txtObservaciones.setText(o.getObservaciones());
    }

    private void generarSiguienteId() {
        String sql = "SELECT ISNULL(MAX(id_orden), 0) + 1 AS siguiente FROM tbl_orden_cliente";
        try (Connection conn = con.establecerConexion();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) txtCodigo.setText(String.valueOf(rs.getInt("siguiente")));
        } catch (Exception e) {
            txtCodigo.setText("1");
        }
    }

    private boolean validarCampos() {
        if (cmbCliente.getValue() == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Campo requerido", "Selecciona un cliente.");
            return false;
        }
        if (dpFecha.getValue() == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Campo requerido", "Selecciona la fecha de registro.");
            return false;
        }
        if (cbMetodoPago.getValue() == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Campo requerido", "Selecciona el método de pago.");
            return false;
        }
        if (cbEstado.getValue() == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Campo requerido", "Selecciona el estado.");
            return false;
        }
        if (listaDetalle.isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Productos requeridos",
                "Agrega al menos un producto a la orden.");
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