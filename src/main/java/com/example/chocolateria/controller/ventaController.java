package com.example.chocolateria.controller;

import com.example.chocolateria.baseDeDatos.conexion;
import com.example.chocolateria.modelo.pagoVentaModelo;
import com.example.chocolateria.modelo.ventaModelo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.*;
import java.time.LocalDate;

public class ventaController {

    private static final double ITBIS_PORCENTAJE = 0.18;

    @FXML private TextField        txtIdVenta;
    @FXML private TextField        txtIdOrden;
    @FXML private Label            lblInfoOrden;
    @FXML private DatePicker       dpFechaVenta;
    @FXML private TextField        txtIdEmpleado;
    @FXML private Label            lblNombreEmpleado;
    @FXML private ComboBox<String> cbTipoPago;
    @FXML private ComboBox<String> cbMetodoPago;
    @FXML private ComboBox<String> cbTipoNcf;
    @FXML private TextField        txtSubtotal;
    @FXML private TextField        txtDescuento;
    @FXML private TextField        txtItbis;
    @FXML private TextField        txtTotal;
    @FXML private TextField        txtMontoPagado;
    @FXML private TextField        txtBalancePendiente;
    @FXML private TextField        txtEstadoPago;
    @FXML private TextField        txtNcf;
    @FXML private TextField        txtBuscarTabla;

    @FXML private DatePicker       dpFechaPago;
    @FXML private TextField        txtMontoAbono;
    @FXML private ComboBox<String> cbMetodoPagoAbono;
    @FXML private TextField        txtReferencia;

    @FXML private TableView<pagoVentaModelo>                tablaPagos;
    @FXML private TableColumn<pagoVentaModelo, Number>      colPagoId;
    @FXML private TableColumn<pagoVentaModelo, LocalDate>   colPagoFecha;
    @FXML private TableColumn<pagoVentaModelo, Number>      colPagoMonto;
    @FXML private TableColumn<pagoVentaModelo, String>      colPagoMetodo;
    @FXML private TableColumn<pagoVentaModelo, String>      colPagoRef;

    @FXML private TableView<ventaModelo>                    tablaVentas;
    @FXML private TableColumn<ventaModelo, Number>          colId;
    @FXML private TableColumn<ventaModelo, String>          colCliente;
    @FXML private TableColumn<ventaModelo, LocalDate>       colFecha;
    @FXML private TableColumn<ventaModelo, Number>          colSubtotal;
    @FXML private TableColumn<ventaModelo, Number>          colItbis;
    @FXML private TableColumn<ventaModelo, Number>          colDescuento;
    @FXML private TableColumn<ventaModelo, Number>          colTotal;
    @FXML private TableColumn<ventaModelo, Number>          colPagado;
    @FXML private TableColumn<ventaModelo, Number>          colBalance;
    @FXML private TableColumn<ventaModelo, String>          colEstado;
    @FXML private TableColumn<ventaModelo, String>          colNcf;

    private final ObservableList<ventaModelo>     listaVentas = FXCollections.observableArrayList();
    private final ObservableList<pagoVentaModelo> listaPagos  = FXCollections.observableArrayList();
    private final conexion con = new conexion();
    private int    idVentaSeleccionada = 0;
    private int    idEmpleadoSeleccionado = 0;
    private double subtotalBase = 0.0;

    @FXML
    public void initialize() {
        cbTipoPago.setItems(FXCollections.observableArrayList("Contado", "Crédito"));
        cbMetodoPago.setItems(FXCollections.observableArrayList("Efectivo", "Transferencia", "Tarjeta", "Cheque"));
        cbMetodoPagoAbono.setItems(FXCollections.observableArrayList("Efectivo", "Transferencia", "Tarjeta", "Cheque"));
        cbTipoNcf.setItems(FXCollections.observableArrayList("B01 - Crédito Fiscal", "B02 - Consumidor Final"));
        cbTipoNcf.setValue("B02 - Consumidor Final");

        // Calcular ITBIS y total cuando cambia el descuento
        txtDescuento.textProperty().addListener((obs, o, n) -> calcularMontos());

        // Columnas historial ventas
        colId.setCellValueFactory(d       -> d.getValue().idVentaProperty());
        colCliente.setCellValueFactory(d  -> d.getValue().clienteProperty());
        colFecha.setCellValueFactory(d    -> d.getValue().fechaVentaProperty());
        colSubtotal.setCellValueFactory(d -> d.getValue().subtotalProperty());
        colItbis.setCellValueFactory(d    -> d.getValue().itbisProperty());
        colDescuento.setCellValueFactory(d-> d.getValue().descuentoProperty());
        colTotal.setCellValueFactory(d    -> d.getValue().montoTotalProperty());
        colPagado.setCellValueFactory(d   -> d.getValue().montoPagadoProperty());
        colBalance.setCellValueFactory(d  -> d.getValue().balancePendienteProperty());
        colEstado.setCellValueFactory(d   -> d.getValue().estadoPagoProperty());
        colNcf.setCellValueFactory(d      -> d.getValue().ncfProperty());

        // Columnas pagos
        colPagoId.setCellValueFactory(d    -> d.getValue().idPagoProperty());
        colPagoFecha.setCellValueFactory(d -> d.getValue().fechaPagoProperty());
        colPagoMonto.setCellValueFactory(d -> d.getValue().montoPagadoProperty());
        colPagoMetodo.setCellValueFactory(d-> d.getValue().metodoPagoProperty());
        colPagoRef.setCellValueFactory(d   -> d.getValue().numeroReferenciaProperty());
        tablaPagos.setItems(listaPagos);

        // Color por estado
        tablaVentas.setRowFactory(tv -> new TableRow<>() {
            @Override
            protected void updateItem(ventaModelo v, boolean empty) {
                super.updateItem(v, empty);
                if (v == null || empty) { setStyle(""); return; }
                switch (v.getEstadoPago()) {
                    case "Pagado"   -> setStyle("-fx-background-color:#e8f5e9;");
                    case "Pendiente"-> setStyle("-fx-background-color:#fde8e8;");
                    case "Parcial"  -> setStyle("-fx-background-color:#fff8e1;");
                    default         -> setStyle("");
                }
            }
        });

        FilteredList<ventaModelo> listaFiltrada = new FilteredList<>(listaVentas, p -> true);
        if (txtBuscarTabla != null) {
            txtBuscarTabla.textProperty().addListener((obs, oldVal, newVal) ->
                    listaFiltrada.setPredicate(v -> {
                        if (newVal == null || newVal.isBlank()) return true;
                        String f = newVal.toLowerCase();
                        return v.getCliente().toLowerCase().contains(f)
                                || v.getEstadoPago().toLowerCase().contains(f)
                                || v.getNcf().toLowerCase().contains(f);
                    })
            );
        }
        tablaVentas.setItems(listaFiltrada);

        tablaVentas.getSelectionModel().selectedItemProperty().addListener(
                (obs, old, sel) -> {
                    if (sel != null) {
                        cargarEnFormulario(sel);
                        cargarPagos(sel.getIdVenta());
                    }
                }
        );

        cargarVentas();
        generarSiguienteId();
    }

    @FXML
    private void cargarDesdeOrden() {
        String idTexto = txtIdOrden.getText().trim();
        if (idTexto.isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Atención", "Escribe el ID de la orden.");
            return;
        }
        try {
            int id = Integer.parseInt(idTexto);
            try (Connection conn = con.establecerConexion();
                 PreparedStatement ps = conn.prepareStatement(
                         "SELECT cliente, producto, cantidad, metodo_pago FROM tbl_orden_cliente WHERE id_orden = ?")) {
                ps.setInt(1, id);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    String cliente  = rs.getString("cliente");
                    String producto = rs.getString("producto");
                    int    cantidad = rs.getInt("cantidad");

                    // Buscar precio del producto
                    double precioUnit = buscarPrecioProducto(conn, producto);
                    subtotalBase = precioUnit * cantidad;

                    lblInfoOrden.setText(cliente + " - " + producto + " x" + cantidad);
                    txtSubtotal.setText(String.format("%.2f", subtotalBase));
                    txtDescuento.setText("0.00");
                    if (rs.getString("metodo_pago") != null)
                        cbMetodoPago.setValue(rs.getString("metodo_pago"));
                    dpFechaVenta.setValue(LocalDate.now());
                    calcularMontos();
                } else {
                    mostrarAlerta(Alert.AlertType.WARNING, "No encontrado",
                            "No existe una orden con el ID " + id + ".");
                }
            }
        } catch (NumberFormatException ex) {
            mostrarAlerta(Alert.AlertType.WARNING, "ID inválido", "El ID debe ser un número.");
        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", e.getMessage());
        }
    }

    @FXML
    private void buscarEmpleado() {
        String idTexto = txtIdEmpleado.getText().trim();
        if (idTexto.isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Atención", "Escribe el ID del empleado.");
            return;
        }
        try {
            int id = Integer.parseInt(idTexto);
            try (Connection conn = con.establecerConexion();
                 PreparedStatement ps = conn.prepareStatement(
                         "SELECT nombre, apellido FROM tbl_empleado WHERE id_empleado = ?")) {
                ps.setInt(1, id);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    idEmpleadoSeleccionado = id;
                    lblNombreEmpleado.setText(rs.getString("nombre") + " " + rs.getString("apellido"));
                } else {
                    idEmpleadoSeleccionado = 0;
                    lblNombreEmpleado.setText("No encontrado");
                }
            }
        } catch (NumberFormatException ex) {
            mostrarAlerta(Alert.AlertType.WARNING, "ID inválido", "El ID debe ser un número.");
        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", e.getMessage());
        }
    }

    @FXML
    private void registrarVenta() {
        if (!validarCamposVenta()) return;

        try {
            double subtotal  = Double.parseDouble(txtSubtotal.getText().trim());
            double descuento = Double.parseDouble(txtDescuento.getText().trim());
            double itbis     = Double.parseDouble(txtItbis.getText().trim());
            double total     = Double.parseDouble(txtTotal.getText().trim());
            String tipoPago  = cbTipoPago.getValue();
            String estadoPago = "Contado".equals(tipoPago) ? "Pagado" : "Pendiente";
            double montoPagado = "Contado".equals(tipoPago) ? total : 0.0;
            double balance     = total - montoPagado;

            // Generar NCF
            String ncf = generarNcf(cbTipoNcf.getValue());

            String sql = "INSERT INTO tbl_venta (fecha_venta, monto_total, id_empleado, descuento, " +
                    "subtotal, itbis, tipo_pago, estado_pago, monto_pagado, balance_pendiente, " +
                    "id_orden, metodo_pago) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            try (Connection conn = con.establecerConexion();
                 PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

                ps.setDate(1, Date.valueOf(dpFechaVenta.getValue()));
                ps.setDouble(2, total);
                ps.setInt(3, idEmpleadoSeleccionado);
                ps.setDouble(4, descuento);
                ps.setDouble(5, subtotal);
                ps.setDouble(6, itbis);
                ps.setString(7, tipoPago);
                ps.setString(8, estadoPago);
                ps.setDouble(9, montoPagado);
                ps.setDouble(10, balance);
                ps.setObject(11, txtIdOrden.getText().trim().isEmpty() ? null
                        : Integer.parseInt(txtIdOrden.getText().trim()));
                ps.setString(12, cbMetodoPago.getValue());
                ps.executeUpdate();

                ResultSet rs = ps.getGeneratedKeys();
                int nuevoId = rs.next() ? rs.getInt(1) : 0;
                idVentaSeleccionada = nuevoId;
                txtIdVenta.setText(String.valueOf(nuevoId));
                txtNcf.setText(ncf);

                // Actualizar id_comprobante con el NCF
                try (PreparedStatement psNcf = conn.prepareStatement(
                        "UPDATE tbl_venta SET id_comprobante = ? WHERE id_venta = ?")) {
                    psNcf.setString(1, ncf);
                    psNcf.setInt(2, nuevoId);
                    psNcf.executeUpdate();
                }

                String cliente = lblInfoOrden.getText().contains("-")
                        ? lblInfoOrden.getText().split("-")[0].trim() : lblInfoOrden.getText();

                ventaModelo nueva = new ventaModelo(nuevoId,
                        txtIdOrden.getText().trim().isEmpty() ? 0 : Integer.parseInt(txtIdOrden.getText().trim()),
                        dpFechaVenta.getValue(), cliente, subtotal, descuento, itbis, total,
                        montoPagado, balance, tipoPago, estadoPago,
                        cbMetodoPago.getValue() != null ? cbMetodoPago.getValue() : "",
                        ncf, idEmpleadoSeleccionado, lblNombreEmpleado.getText());
                listaVentas.add(0, nueva);

                actualizarResumenUI(montoPagado, total, estadoPago);
                mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito",
                        "Venta registrada. NCF: " + ncf + "\nTotal: RD$ " + String.format("%.2f", total));
            }

        } catch (NumberFormatException e) {
            mostrarAlerta(Alert.AlertType.WARNING, "Error", "Verifica los montos ingresados.");
        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error al registrar venta", e.getMessage());
        }
    }

    @FXML
    private void registrarAbono() {
        if (idVentaSeleccionada == 0) {
            mostrarAlerta(Alert.AlertType.WARNING, "Atención",
                    "Selecciona una venta de la tabla o regístrala primero.");
            return;
        }
        if (!validarCamposAbono()) return;

        try {
            double montoAbono = Double.parseDouble(txtMontoAbono.getText().trim());
            double balance    = Double.parseDouble(txtBalancePendiente.getText().trim());

            if (montoAbono > balance) {
                mostrarAlerta(Alert.AlertType.WARNING, "Monto excedido",
                        String.format("El abono (RD$ %.2f) supera el balance pendiente (RD$ %.2f).",
                                montoAbono, balance));
                return;
            }

            String sql = "INSERT INTO tbl_pago_venta (id_venta, fecha_pago, monto_pagado, metodo_pago, numero_referencia) " +
                    "VALUES (?, ?, ?, ?, ?)";

            try (Connection conn = con.establecerConexion();
                 PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

                ps.setInt(1, idVentaSeleccionada);
                ps.setDate(2, Date.valueOf(dpFechaPago.getValue()));
                ps.setDouble(3, montoAbono);
                ps.setString(4, cbMetodoPagoAbono.getValue());
                ps.setString(5, txtReferencia.getText().trim());
                ps.executeUpdate();

                ResultSet rs = ps.getGeneratedKeys();
                int nuevoId = rs.next() ? rs.getInt(1) : 0;

                // Actualizar monto_pagado y balance en tbl_venta
                double nuevoPagado = Double.parseDouble(txtMontoPagado.getText().trim()) + montoAbono;
                double total       = Double.parseDouble(txtTotal.getText().trim());
                double nuevoBalance= total - nuevoPagado;
                String nuevoEstado = nuevoBalance <= 0 ? "Pagado"
                        : nuevoPagado > 0  ? "Parcial" : "Pendiente";

                try (PreparedStatement psUpd = conn.prepareStatement(
                        "UPDATE tbl_venta SET monto_pagado=?, balance_pendiente=?, estado_pago=? WHERE id_venta=?")) {
                    psUpd.setDouble(1, nuevoPagado);
                    psUpd.setDouble(2, nuevoBalance);
                    psUpd.setString(3, nuevoEstado);
                    psUpd.setInt(4, idVentaSeleccionada);
                    psUpd.executeUpdate();
                }

                listaPagos.add(new pagoVentaModelo(nuevoId, idVentaSeleccionada,
                        dpFechaPago.getValue(), montoAbono,
                        cbMetodoPagoAbono.getValue(), txtReferencia.getText().trim(), ""));

                // Actualizar fila en tabla
                for (ventaModelo v : listaVentas) {
                    if (v.getIdVenta() == idVentaSeleccionada) {
                        v.setMontoPagado(nuevoPagado);
                        v.setBalancePendiente(nuevoBalance);
                        v.setEstadoPago(nuevoEstado);
                        tablaVentas.refresh();
                        break;
                    }
                }

                actualizarResumenUI(nuevoPagado, total, nuevoEstado);
                limpiarCamposAbono();
                mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito",
                        String.format("Abono de RD$ %.2f registrado. Estado: %s", montoAbono, nuevoEstado));
            }

        } catch (NumberFormatException e) {
            mostrarAlerta(Alert.AlertType.WARNING, "Monto inválido", "El monto debe ser un número válido.");
        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error al registrar abono", e.getMessage());
        }
    }

    @FXML
    private void fnBuscar() {
        String idTexto = txtIdVenta.getText().trim();
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
        for (ventaModelo v : listaVentas) {
            if (v.getIdVenta() == idBuscar) {
                tablaVentas.getSelectionModel().select(v);
                tablaVentas.scrollTo(v);
                cargarEnFormulario(v);
                cargarPagos(v.getIdVenta());
                return;
            }
        }
        mostrarAlerta(Alert.AlertType.WARNING, "No encontrado", "No existe una venta con el ID " + idBuscar + ".");
    }

    @FXML
    private void fnEliminar() {
        ventaModelo sel = tablaVentas.getSelectionModel().getSelectedItem();
        if (sel == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Atención", "Selecciona una venta para eliminar.");
            return;
        }
        if (!"Pendiente".equals(sel.getEstadoPago())) {
            mostrarAlerta(Alert.AlertType.WARNING, "No permitido",
                    "Solo se pueden eliminar ventas en estado Pendiente sin pagos.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirmar eliminación");
        confirm.setHeaderText(null);
        confirm.setContentText("¿Eliminar la venta #" + sel.getIdVenta() + "?");

        confirm.showAndWait().ifPresent(resp -> {
            if (resp == ButtonType.OK) {
                try (Connection conn = con.establecerConexion();
                     PreparedStatement ps = conn.prepareStatement(
                             "DELETE FROM tbl_venta WHERE id_venta=?")) {
                    ps.setInt(1, sel.getIdVenta());
                    ps.executeUpdate();
                    listaVentas.remove(sel);
                    listaPagos.clear();
                    mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Venta eliminada.");
                    limpiar();
                } catch (Exception e) {
                    mostrarAlerta(Alert.AlertType.ERROR, "Error al eliminar", e.getMessage());
                }
            }
        });
    }

    @FXML
    private void limpiar() {
        txtIdVenta.clear();
        txtIdOrden.clear();
        lblInfoOrden.setText("");
        dpFechaVenta.setValue(null);
        txtIdEmpleado.clear();
        lblNombreEmpleado.setText("");
        idEmpleadoSeleccionado = 0;
        cbTipoPago.setValue(null);
        cbMetodoPago.setValue(null);
        cbTipoNcf.setValue("B02 - Consumidor Final");
        txtSubtotal.clear();
        txtDescuento.clear();
        txtItbis.clear();
        txtTotal.clear();
        txtMontoPagado.clear();
        txtBalancePendiente.clear();
        txtEstadoPago.clear();
        txtNcf.clear();
        txtEstadoPago.setStyle("-fx-background-color:#f0eaf8; -fx-font-weight:bold; -fx-border-color:purple; -fx-border-radius:6; -fx-background-radius:6;");
        subtotalBase = 0.0;
        idVentaSeleccionada = 0;
        listaPagos.clear();
        limpiarCamposAbono();
        tablaVentas.getSelectionModel().clearSelection();
        generarSiguienteId();
    }

    private void limpiarCamposAbono() {
        dpFechaPago.setValue(null);
        txtMontoAbono.clear();
        cbMetodoPagoAbono.setValue(null);
        txtReferencia.clear();
    }

    private void calcularMontos() {
        try {
            double descuento = txtDescuento.getText().trim().isEmpty()
                    ? 0.0 : Double.parseDouble(txtDescuento.getText().trim());
            double base  = subtotalBase - descuento;
            double itbis = base * ITBIS_PORCENTAJE;
            double total = base + itbis;
            txtItbis.setText(String.format("%.2f", itbis));
            txtTotal.setText(String.format("%.2f", total));
        } catch (NumberFormatException ignored) {}
    }

    private String generarNcf(String tipoNcf) throws Exception {
        String codigo = tipoNcf.startsWith("B01") ? "B01" : "B02";
        try (Connection conn = con.establecerConexion();
             PreparedStatement ps = conn.prepareStatement(
                     "UPDATE tbl_secuencia_ncf SET secuencia = secuencia + 1 OUTPUT INSERTED.secuencia WHERE tipo_ncf = ?")) {
            ps.setString(1, codigo);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int seq = rs.getInt(1);
                return String.format("%s%08d", codigo, seq);
            }
        }
        return codigo + "00000001";
    }

    private double buscarPrecioProducto(Connection conn, String nombreProducto) {
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT precio_unitario FROM tbl_producto WHERE nombre = ?")) {
            ps.setString(1, nombreProducto);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return Double.parseDouble(rs.getString("precio_unitario"));
        } catch (Exception ignored) {}
        return 0.0;
    }

    private void cargarVentas() {
        listaVentas.clear();
        String sql = "SELECT v.id_venta, v.id_orden, v.fecha_venta, " +
                "ISNULL(o.cliente, '') AS cliente, " +
                "v.subtotal, v.descuento, v.itbis, v.monto_total, " +
                "v.monto_pagado, v.balance_pendiente, v.tipo_pago, " +
                "v.estado_pago, v.metodo_pago, " +
                "ISNULL(CAST(v.id_comprobante AS VARCHAR), '') AS ncf, " +
                "v.id_empleado, " +
                "ISNULL(e.nombre + ' ' + e.apellido, '') AS empleado " +
                "FROM tbl_venta v " +
                "LEFT JOIN tbl_orden_cliente o ON v.id_orden = o.id_orden " +
                "LEFT JOIN tbl_empleado e ON v.id_empleado = e.id_empleado " +
                "ORDER BY v.fecha_venta DESC";
        try (Connection conn = con.establecerConexion();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Date d = rs.getDate("fecha_venta");
                listaVentas.add(new ventaModelo(
                        rs.getInt("id_venta"),
                        rs.getInt("id_orden"),
                        d != null ? d.toLocalDate() : null,
                        rs.getString("cliente"),
                        rs.getDouble("subtotal"),
                        rs.getDouble("descuento"),
                        rs.getDouble("itbis"),
                        rs.getDouble("monto_total"),
                        rs.getDouble("monto_pagado"),
                        rs.getDouble("balance_pendiente"),
                        rs.getString("tipo_pago"),
                        rs.getString("estado_pago"),
                        rs.getString("metodo_pago") != null ? rs.getString("metodo_pago") : "",
                        rs.getString("ncf"),
                        rs.getInt("id_empleado"),
                        rs.getString("empleado")
                ));
            }
        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error al cargar ventas", e.getMessage());
        }
    }

    private void cargarPagos(int idVenta) {
        listaPagos.clear();
        idVentaSeleccionada = idVenta;
        String sql = "SELECT id_pago, id_venta, fecha_pago, monto_pagado, metodo_pago, " +
                "numero_referencia, observaciones FROM tbl_pago_venta WHERE id_venta = ?";
        try (Connection conn = con.establecerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idVenta);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Date d = rs.getDate("fecha_pago");
                listaPagos.add(new pagoVentaModelo(
                        rs.getInt("id_pago"),
                        rs.getInt("id_venta"),
                        d != null ? d.toLocalDate() : null,
                        rs.getDouble("monto_pagado"),
                        rs.getString("metodo_pago"),
                        rs.getString("numero_referencia") != null ? rs.getString("numero_referencia") : "",
                        rs.getString("observaciones") != null ? rs.getString("observaciones") : ""
                ));
            }
        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error al cargar pagos", e.getMessage());
        }
    }

    private void cargarEnFormulario(ventaModelo v) {
        txtIdVenta.setText(String.valueOf(v.getIdVenta()));
        txtIdOrden.setText(String.valueOf(v.getIdOrden()));
        lblInfoOrden.setText(v.getCliente());
        dpFechaVenta.setValue(v.getFechaVenta());
        txtIdEmpleado.setText(String.valueOf(v.getIdEmpleado()));
        lblNombreEmpleado.setText(v.getEmpleado());
        idEmpleadoSeleccionado = v.getIdEmpleado();
        cbTipoPago.setValue(v.getTipoPago());
        cbMetodoPago.setValue(v.getMetodoPago());
        txtNcf.setText(v.getNcf());
        subtotalBase = v.getSubtotal();
        txtSubtotal.setText(String.format("%.2f", v.getSubtotal()));
        txtDescuento.setText(String.format("%.2f", v.getDescuento()));
        txtItbis.setText(String.format("%.2f", v.getItbis()));
        txtTotal.setText(String.format("%.2f", v.getMontoTotal()));
        actualizarResumenUI(v.getMontoPagado(), v.getMontoTotal(), v.getEstadoPago());
        idVentaSeleccionada = v.getIdVenta();
    }

    private void actualizarResumenUI(double pagado, double total, String estado) {
        double balance = total - pagado;
        txtMontoPagado.setText(String.format("%.2f", pagado));
        txtBalancePendiente.setText(String.format("%.2f", balance));
        txtEstadoPago.setText(estado);
        String color = switch (estado) {
            case "Pagado"    -> "-fx-text-fill:#2e7d32;";
            case "Parcial"   -> "-fx-text-fill:#e65100;";
            default          -> "-fx-text-fill:#c0392b;";
        };
        txtEstadoPago.setStyle("-fx-background-color:#f0eaf8; -fx-font-weight:bold; " +
                "-fx-border-color:purple; -fx-border-radius:6; " +
                "-fx-background-radius:6; " + color);
    }

    private void generarSiguienteId() {
        String sql = "SELECT ISNULL(MAX(id_venta), 0) + 1 AS siguiente FROM tbl_venta";
        try (Connection conn = con.establecerConexion();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) txtIdVenta.setText(String.valueOf(rs.getInt("siguiente")));
        } catch (Exception e) {
            txtIdVenta.setText("1");
        }
    }

    private boolean validarCamposVenta() {
        if (dpFechaVenta.getValue() == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Fecha requerida", "Selecciona la fecha de venta.");
            return false;
        }
        if (idEmpleadoSeleccionado == 0) {
            mostrarAlerta(Alert.AlertType.WARNING, "Empleado requerido", "Busca y selecciona el empleado responsable.");
            return false;
        }
        if (cbTipoPago.getValue() == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Tipo pago requerido", "Selecciona el tipo de pago.");
            return false;
        }
        if (txtSubtotal.getText().trim().isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Subtotal requerido", "Carga una orden o ingresa el subtotal.");
            return false;
        }
        return true;
    }

    private boolean validarCamposAbono() {
        if (dpFechaPago.getValue() == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Fecha requerida", "Selecciona la fecha del abono.");
            return false;
        }
        if (txtMontoAbono.getText().trim().isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Monto requerido", "Ingresa el monto del abono.");
            return false;
        }
        if (cbMetodoPagoAbono.getValue() == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Método requerido", "Selecciona el método de pago.");
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