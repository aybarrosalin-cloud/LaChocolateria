package com.example.chocolateria.controller;

import com.example.chocolateria.baseDeDatos.conexion;
import com.example.chocolateria.modelo.recepcionDetalleModelo;
import com.example.chocolateria.modelo.recepcionModelo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.*;
import java.time.LocalDate;

public class recepcionController {

    @FXML private TextField  txtIdRecepcion;
    @FXML private TextField  txtNumeroOrden;
    @FXML private DatePicker dpFechaRecepcion;
    @FXML private TextField  txtRncProveedor;
    @FXML private TextField  txtMontoTotal;
    @FXML private TextArea   txtObservaciones;
    @FXML private TextField  txtBuscarTabla;

    @FXML private TextField txtCodigoDetalle;
    @FXML private Label     lblNombreProducto;
    @FXML private TextField txtCantidadDetalle;
    @FXML private TextField txtPrecioDetalle;

    @FXML private TableView<recepcionDetalleModelo>           tablaDetalle;
    @FXML private TableColumn<recepcionDetalleModelo, String> colDetCodigo;
    @FXML private TableColumn<recepcionDetalleModelo, String> colDetProducto;
    @FXML private TableColumn<recepcionDetalleModelo, Number> colDetCantidad;
    @FXML private TableColumn<recepcionDetalleModelo, Number> colDetPrecio;
    @FXML private TableColumn<recepcionDetalleModelo, Number> colDetMonto;

    @FXML private TableView<recepcionModelo>                      tablaRecepciones;
    @FXML private TableColumn<recepcionModelo, Number>            colId;
    @FXML private TableColumn<recepcionModelo, String>            colNumeroOrden;
    @FXML private TableColumn<recepcionModelo, String>            colRnc;
    @FXML private TableColumn<recepcionModelo, LocalDate>         colFecha;
    @FXML private TableColumn<recepcionModelo, Number>            colMonto;
    @FXML private TableColumn<recepcionModelo, String>            colObservaciones;

    private final ObservableList<recepcionModelo>        lista        = FXCollections.observableArrayList();
    private final ObservableList<recepcionDetalleModelo> listaDetalle = FXCollections.observableArrayList();
    private final conexion con = new conexion();
    private String productoSeleccionado = "";

    @FXML
    public void initialize() {

        colId.setCellValueFactory(d            -> d.getValue().idRecepcionProperty());
        colNumeroOrden.setCellValueFactory(d   -> d.getValue().numeroOrdenProperty());
        colRnc.setCellValueFactory(d           -> d.getValue().rncProveedorProperty());
        colFecha.setCellValueFactory(d         -> d.getValue().fechaRecepcionProperty());
        colMonto.setCellValueFactory(d         -> d.getValue().montoTotalProperty());
        colObservaciones.setCellValueFactory(d -> d.getValue().observacionesProperty());

        colDetCodigo.setCellValueFactory(d    -> d.getValue().codigoProductoProperty());
        colDetProducto.setCellValueFactory(d  -> d.getValue().productoProperty());
        colDetCantidad.setCellValueFactory(d  -> d.getValue().cantidadRecibidaProperty());
        colDetPrecio.setCellValueFactory(d    -> d.getValue().precioUnitarioProperty());
        colDetMonto.setCellValueFactory(d     -> d.getValue().montoProductoProperty());
        tablaDetalle.setItems(listaDetalle);

        FilteredList<recepcionModelo> listaFiltrada = new FilteredList<>(lista, p -> true);
        if (txtBuscarTabla != null) {
            txtBuscarTabla.textProperty().addListener((obs, oldVal, newVal) ->
                    listaFiltrada.setPredicate(r -> {
                        if (newVal == null || newVal.isBlank()) return true;
                        String f = newVal.toLowerCase();
                        return r.getRncProveedor().toLowerCase().contains(f)
                                || r.getNumeroOrden().toLowerCase().contains(f);
                    })
            );
        }
        tablaRecepciones.setItems(listaFiltrada);

        tablaRecepciones.getSelectionModel().selectedItemProperty().addListener(
                (obs, old, sel) -> {
                    if (sel != null) {
                        cargarEnFormulario(sel);
                        cargarDetalle(sel.getIdRecepcion());
                    }
                }
        );

        cargarRecepciones();
        generarSiguienteId();
    }

    @FXML
    private void buscarProductoDetalle() {
        String codigo = txtCodigoDetalle.getText().trim();
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
                productoSeleccionado = rs.getString("nombre");
                lblNombreProducto.setText(productoSeleccionado);
                // Pre-llenar el precio con el precio unitario del producto
                String precio = rs.getString("precio_unitario");
                if (precio != null && !precio.isBlank()) {
                    txtPrecioDetalle.setText(precio);
                }
            } else {
                productoSeleccionado = "";
                lblNombreProducto.setText("No encontrado");
                mostrarAlerta(Alert.AlertType.WARNING, "No encontrado",
                        "No existe un producto con el código " + codigo + ".");
            }
        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", e.getMessage());
        }
    }

    @FXML
    private void cargarDesdeOrden() {
        String numOrden = txtNumeroOrden.getText().trim();
        if (numOrden.isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Atención", "Escribe el número de orden.");
            return;
        }
        try (Connection conn = con.establecerConexion();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT rnc_proveedor, producto, monto_total FROM tbl_orden_proveedor WHERE codigo = ?")) {
            ps.setString(1, numOrden);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                txtRncProveedor.setText(rs.getString("rnc_proveedor"));
                txtMontoTotal.setText(String.format("%.2f", rs.getDouble("monto_total")));
                dpFechaRecepcion.setValue(LocalDate.now());
                mostrarAlerta(Alert.AlertType.INFORMATION, "Orden cargada",
                        "Datos cargados desde la orden. Agrega los productos recibidos en el detalle.");
            } else {
                mostrarAlerta(Alert.AlertType.WARNING, "No encontrado",
                        "No se encontró ninguna orden con ese número.");
            }
        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", e.getMessage());
        }
    }

    @FXML
    private void agregarProducto() {
        if (productoSeleccionado.isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Atención", "Busca un producto por código antes de agregar.");
            return;
        }
        if (txtCantidadDetalle.getText().trim().isEmpty() || txtPrecioDetalle.getText().trim().isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Atención", "Ingresa cantidad y precio.");
            return;
        }
        try {
            int    cantidad = Integer.parseInt(txtCantidadDetalle.getText().trim());
            double precio   = Double.parseDouble(txtPrecioDetalle.getText().trim());

            if (cantidad <= 0 || precio <= 0) {
                mostrarAlerta(Alert.AlertType.WARNING, "Valores inválidos", "Cantidad y precio deben ser mayores a 0.");
                return;
            }

            String codigo = txtCodigoDetalle.getText().trim();
            double monto  = cantidad * precio;

            for (recepcionDetalleModelo d : listaDetalle) {
                if (d.getCodigoProducto().equals(codigo)) {
                    mostrarAlerta(Alert.AlertType.WARNING, "Duplicado",
                            "Este producto ya está en la lista.");
                    return;
                }
            }

            listaDetalle.add(new recepcionDetalleModelo(0, 0, codigo, productoSeleccionado, cantidad, precio, monto));
            recalcularTotal();

            txtCodigoDetalle.clear();
            lblNombreProducto.setText("");
            txtCantidadDetalle.clear();
            txtPrecioDetalle.clear();
            productoSeleccionado = "";

        } catch (NumberFormatException ex) {
            mostrarAlerta(Alert.AlertType.WARNING, "Valores inválidos", "Cantidad debe ser entero y precio número válido.");
        }
    }

    @FXML
    private void quitarProducto() {
        recepcionDetalleModelo sel = tablaDetalle.getSelectionModel().getSelectedItem();
        if (sel == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Atención", "Selecciona un producto para quitarlo.");
            return;
        }
        listaDetalle.remove(sel);
        recalcularTotal();
    }

    @FXML
    private void guardarRecepcion() {
        if (!validarCampos()) return;

        double montoTotal = listaDetalle.stream().mapToDouble(recepcionDetalleModelo::getMontoProducto).sum();

        String sqlMaestro = "INSERT INTO tbl_recepcion (rnc_proveedor, numero_orden, fecha_recepcion, monto_total, observaciones, codigo_orden) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = con.establecerConexion();
             PreparedStatement ps = conn.prepareStatement(sqlMaestro, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, txtRncProveedor.getText().trim());
            ps.setString(2, txtNumeroOrden.getText().trim());
            ps.setDate(3, Date.valueOf(dpFechaRecepcion.getValue()));
            ps.setDouble(4, montoTotal);
            ps.setString(5, txtObservaciones.getText().trim());
            ps.setString(6, txtNumeroOrden.getText().trim());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (!rs.next()) throw new SQLException("No se obtuvo el ID de la recepción.");
            int nuevoId = rs.getInt(1);

            String sqlDetalle = "INSERT INTO tbl_recepcion_detalle (id_recepcion, codigo_producto, producto, cantidad_recibida, precio_unitario, monto_producto) VALUES (?, ?, ?, ?, ?, ?)";
            String sqlEntrada = "INSERT INTO tbl_entrada_de_almacen (id_producto, cantidad, fecha_entrada) VALUES (?, ?, ?)";
            String sqlStock   = "UPDATE tbl_producto SET stock = stock + ? WHERE codigo = ?";

            try (PreparedStatement psDet   = conn.prepareStatement(sqlDetalle);
                 PreparedStatement psEnt   = conn.prepareStatement(sqlEntrada);
                 PreparedStatement psStock = conn.prepareStatement(sqlStock)) {

                for (recepcionDetalleModelo det : listaDetalle) {
                    // 1. Detalle recepcion
                    psDet.setInt(1, nuevoId);
                    psDet.setString(2, det.getCodigoProducto());
                    psDet.setString(3, det.getProducto());
                    psDet.setInt(4, det.getCantidadRecibida());
                    psDet.setDouble(5, det.getPrecioUnitario());
                    psDet.setDouble(6, det.getMontoProducto());
                    psDet.addBatch();

                    // 2. Entrada almacen — usa el codigo directamente
                    psEnt.setString(1, det.getCodigoProducto());
                    psEnt.setInt(2, det.getCantidadRecibida());
                    psEnt.setTimestamp(3, Timestamp.valueOf(dpFechaRecepcion.getValue().atStartOfDay()));
                    psEnt.addBatch();

                    // 3. Actualizar stock
                    psStock.setInt(1, det.getCantidadRecibida());
                    psStock.setString(2, det.getCodigoProducto());
                    psStock.addBatch();
                }

                psDet.executeBatch();
                psEnt.executeBatch();
                psStock.executeBatch();
            }

            lista.add(0, new recepcionModelo(nuevoId,
                    txtRncProveedor.getText().trim(),
                    txtNumeroOrden.getText().trim(),
                    0, dpFechaRecepcion.getValue(),
                    montoTotal, txtObservaciones.getText().trim()));

            mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito",
                    "Recepción guardada. Stock actualizado para " + listaDetalle.size() + " producto(s).");
            limpiarCampos();

        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error al guardar", e.getMessage());
        }
    }

    @FXML
    private void fnEliminar() {
        recepcionModelo sel = tablaRecepciones.getSelectionModel().getSelectedItem();
        if (sel == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Atención", "Selecciona una recepción para eliminar.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirmar eliminación");
        confirm.setHeaderText(null);
        confirm.setContentText("¿Eliminar la recepción #" + sel.getIdRecepcion() + "?\nNota: el stock no se revertirá automáticamente.");

        confirm.showAndWait().ifPresent(resp -> {
            if (resp == ButtonType.OK) {
                try (Connection conn = con.establecerConexion()) {
                    try (PreparedStatement ps = conn.prepareStatement(
                            "DELETE FROM tbl_recepcion_detalle WHERE id_recepcion=?")) {
                        ps.setInt(1, sel.getIdRecepcion());
                        ps.executeUpdate();
                    }
                    try (PreparedStatement ps = conn.prepareStatement(
                            "DELETE FROM tbl_recepcion WHERE id_recepcion=?")) {
                        ps.setInt(1, sel.getIdRecepcion());
                        ps.executeUpdate();
                    }
                    lista.remove(sel);
                    listaDetalle.clear();
                    mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Recepción eliminada correctamente.");
                    limpiarCampos();
                } catch (Exception e) {
                    mostrarAlerta(Alert.AlertType.ERROR, "Error al eliminar", e.getMessage());
                }
            }
        });
    }

    @FXML
    private void fnBuscar() {
        String idTexto = txtIdRecepcion.getText().trim();
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

        for (recepcionModelo r : lista) {
            if (r.getIdRecepcion() == idBuscar) {
                tablaRecepciones.getSelectionModel().select(r);
                tablaRecepciones.scrollTo(r);
                cargarEnFormulario(r);
                cargarDetalle(r.getIdRecepcion());
                return;
            }
        }
        mostrarAlerta(Alert.AlertType.WARNING, "No encontrado", "No existe una recepción con el ID " + idBuscar + ".");
    }

    @FXML
    private void limpiarCampos() {
        txtIdRecepcion.clear();
        txtNumeroOrden.clear();
        dpFechaRecepcion.setValue(null);
        txtRncProveedor.clear();
        txtMontoTotal.clear();
        txtObservaciones.clear();
        txtCodigoDetalle.clear();
        lblNombreProducto.setText("");
        txtCantidadDetalle.clear();
        txtPrecioDetalle.clear();
        productoSeleccionado = "";
        listaDetalle.clear();
        tablaRecepciones.getSelectionModel().clearSelection();
        generarSiguienteId();
    }

    private void cargarRecepciones() {
        lista.clear();
        String sql = "SELECT id_recepcion, rnc_proveedor, numero_orden, fecha_recepcion, monto_total, observaciones " +
                "FROM tbl_recepcion ORDER BY fecha_recepcion DESC";
        try (Connection conn = con.establecerConexion();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Date d = rs.getDate("fecha_recepcion");
                lista.add(new recepcionModelo(
                        rs.getInt("id_recepcion"),
                        rs.getString("rnc_proveedor"),
                        rs.getString("numero_orden"),
                        0,
                        d != null ? d.toLocalDate() : null,
                        rs.getDouble("monto_total"),
                        rs.getString("observaciones") != null ? rs.getString("observaciones") : ""
                ));
            }
        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error al cargar recepciones", e.getMessage());
        }
    }

    private void cargarDetalle(int idRecepcion) {
        listaDetalle.clear();
        String sql = "SELECT id_detalle, id_recepcion, codigo_producto, producto, cantidad_recibida, precio_unitario, monto_producto " +
                "FROM tbl_recepcion_detalle WHERE id_recepcion = ?";
        try (Connection conn = con.establecerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idRecepcion);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                listaDetalle.add(new recepcionDetalleModelo(
                        rs.getInt("id_detalle"),
                        rs.getInt("id_recepcion"),
                        rs.getString("codigo_producto") != null ? rs.getString("codigo_producto") : "",
                        rs.getString("producto"),
                        rs.getInt("cantidad_recibida"),
                        rs.getDouble("precio_unitario"),
                        rs.getDouble("monto_producto")
                ));
            }
        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error al cargar detalle", e.getMessage());
        }
    }

    private void recalcularTotal() {
        double total = listaDetalle.stream().mapToDouble(recepcionDetalleModelo::getMontoProducto).sum();
        txtMontoTotal.setText(String.format("%.2f", total));
    }

    private void cargarEnFormulario(recepcionModelo r) {
        txtIdRecepcion.setText(String.valueOf(r.getIdRecepcion()));
        txtNumeroOrden.setText(r.getNumeroOrden());
        dpFechaRecepcion.setValue(r.getFechaRecepcion());
        txtRncProveedor.setText(r.getRncProveedor());
        txtMontoTotal.setText(String.format("%.2f", r.getMontoTotal()));
        txtObservaciones.setText(r.getObservaciones());
    }

    private void generarSiguienteId() {
        String sql = "SELECT ISNULL(MAX(id_recepcion), 0) + 1 AS siguiente FROM tbl_recepcion";
        try (Connection conn = con.establecerConexion();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) txtIdRecepcion.setText(String.valueOf(rs.getInt("siguiente")));
        } catch (Exception e) {
            txtIdRecepcion.setText("1");
        }
    }

    private boolean validarCampos() {
        if (dpFechaRecepcion.getValue() == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Fecha requerida", "Selecciona la fecha de recepción.");
            return false;
        }
        if (txtRncProveedor.getText().trim().isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "RNC requerido", "Ingresa el RNC del proveedor.");
            return false;
        }
        if (listaDetalle.isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Productos requeridos", "Agrega al menos un producto antes de guardar.");
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