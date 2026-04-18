package com.example.chocolateria.controller;

import com.example.chocolateria.baseDeDatos.conexion;
import com.example.chocolateria.modelo.ordenProveedorDetalleModelo;
import com.example.chocolateria.modelo.ordenProveedorModelo;
import javafx.beans.property.SimpleStringProperty;
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

public class ordenProveedorController {

    @FXML private TextField        txtId;
    @FXML private ComboBox<String> cbRncProveedor;
    @FXML private DatePicker       dpFechaRequerida;
    @FXML private ComboBox<String> cbPrioridad;
    @FXML private ComboBox<String> cbEstadoPago;
    @FXML private TextArea         txtDescripcion;
    @FXML private TextField        txtMontoTotal;
    @FXML private TextField        txtBuscarTabla;

    @FXML private CheckBox chkBombones, chkTabletas, chkTrufas, chkBanos;
    @FXML private CheckBox chkRellenos, chkCoberturas, chkMateriaPrima, chkEmpaque;

    @FXML private TextField txtCodigoProducto;
    @FXML private Label     lblNombreProducto;
    @FXML private TextField txtCantidadDetalle;
    @FXML private TextField txtPrecioDetalle;

    @FXML private TableView<ordenProveedorDetalleModelo>           tablaDetalle;
    @FXML private TableColumn<ordenProveedorDetalleModelo, String> colDetCodigo;
    @FXML private TableColumn<ordenProveedorDetalleModelo, String> colDetProducto;
    @FXML private TableColumn<ordenProveedorDetalleModelo, Number> colDetCantidad;
    @FXML private TableColumn<ordenProveedorDetalleModelo, Number> colDetPrecio;
    @FXML private TableColumn<ordenProveedorDetalleModelo, Number> colDetSubtotal;

    @FXML private TableView<ordenProveedorModelo>                  tablaOrdenes;
    @FXML private TableColumn<ordenProveedorModelo, Number>        colId;
    @FXML private TableColumn<ordenProveedorModelo, String>        colRnc;
    @FXML private TableColumn<ordenProveedorModelo, LocalDate>     colFecha;
    @FXML private TableColumn<ordenProveedorModelo, String>        colPrioridad;
    @FXML private TableColumn<ordenProveedorModelo, String>        colCategoria;
    @FXML private TableColumn<ordenProveedorModelo, Number>        colMonto;
    @FXML private TableColumn<ordenProveedorModelo, String>        colEstadoPago;
    @FXML private TableColumn<ordenProveedorModelo, String>        colProductos;

    private final ObservableList<ordenProveedorModelo>        lista        = FXCollections.observableArrayList();
    private final ObservableList<ordenProveedorDetalleModelo> listaDetalle = FXCollections.observableArrayList();
    private final conexion con = new conexion();
    private final Map<String, String> mapaRncNombre = new HashMap<>();
    private String productoSeleccionado = "";

    @FXML private Label lblUsuario;
    @FXML private ImageView imgFotoPerfil;

    @FXML
    public void initialize() {
        CargarPerfil.aplicar(lblUsuario, imgFotoPerfil);
        cbPrioridad.setItems(FXCollections.observableArrayList("Alta", "Media", "Baja"));
        cbEstadoPago.setItems(FXCollections.observableArrayList("Pendiente", "Parcial", "Pagado", "Cancelado"));
        cargarProveedores();

        colDetCodigo.setCellValueFactory(d   -> d.getValue().codigoProdProperty());
        colDetProducto.setCellValueFactory(d -> d.getValue().productoProperty());
        colDetCantidad.setCellValueFactory(d -> d.getValue().cantidadProperty());
        colDetPrecio.setCellValueFactory(d   -> d.getValue().precioProperty());
        colDetSubtotal.setCellValueFactory(d -> d.getValue().subtotalProperty());
        tablaDetalle.setItems(listaDetalle);
        listaDetalle.addListener((javafx.collections.ListChangeListener<ordenProveedorDetalleModelo>) c -> recalcularMonto());

        colId.setCellValueFactory(d         -> d.getValue().codigoProperty());
        colRnc.setCellValueFactory(d        -> d.getValue().proveedorProperty());
        colFecha.setCellValueFactory(d      -> d.getValue().fechaRequeridaProperty());
        colPrioridad.setCellValueFactory(d  -> d.getValue().prioridadProperty());
        colCategoria.setCellValueFactory(d  -> d.getValue().categoriaProperty());
        colMonto.setCellValueFactory(d      -> d.getValue().montoTotalProperty());
        colEstadoPago.setCellValueFactory(d -> d.getValue().estadoPagoProperty());
        colProductos.setCellValueFactory(d  -> new SimpleStringProperty(cargarResumenProductos(d.getValue().getCodigo())));

        tablaOrdenes.setRowFactory(tv -> new TableRow<>() {
            @Override
            protected void updateItem(ordenProveedorModelo o, boolean empty) {
                super.updateItem(o, empty);
                if (o == null || empty) { setStyle(""); return; }
                switch (o.getEstadoPago()) {
                    case "Pagado"    -> setStyle("-fx-background-color:#e8f5e9;");
                    case "Cancelado" -> setStyle("-fx-background-color:#fde8e8;");
                    case "Parcial"   -> setStyle("-fx-background-color:#fff8e1;");
                    default          -> setStyle("");
                }
            }
        });

        FilteredList<ordenProveedorModelo> listaFiltrada = new FilteredList<>(lista, p -> true);
        txtBuscarTabla.textProperty().addListener((obs, o, nv) ->
            listaFiltrada.setPredicate(ord -> {
                if (nv == null || nv.isBlank()) return true;
                String f = nv.toLowerCase();
                return ord.getProveedor().toLowerCase().contains(f)
                    || ord.getEstadoPago().toLowerCase().contains(f);
            }));
        tablaOrdenes.setItems(listaFiltrada);

        tablaOrdenes.getSelectionModel().selectedItemProperty().addListener(
            (obs, old, sel) -> {
                if (sel != null) { cargarEnFormulario(sel); cargarDetalle(sel.getCodigo()); }
            });

        cargarOrdenes();
        generarSiguienteId();
    }

    private void cargarProveedores() {
        String sql = "SELECT rnc, nombre + ' ' + apellido AS nombre_completo FROM tbl_suplidor ORDER BY nombre";
        try (Connection conn = con.establecerConexion();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                String rnc  = rs.getString("rnc");
                String nom  = rs.getString("nombre_completo");
                String item = rnc + " - " + nom;
                cbRncProveedor.getItems().add(item);
                mapaRncNombre.put(item, rnc);
            }
        } catch (Exception e) { mostrarAlerta(Alert.AlertType.ERROR,"Error",e.getMessage()); }
    }

    @FXML
    private void buscarProducto() {
        String codigo = txtCodigoProducto.getText().trim();
        if (codigo.isEmpty()) { mostrarAlerta(Alert.AlertType.WARNING,"Atención","Escribe el código del producto."); return; }
        try (Connection conn = con.establecerConexion();
             PreparedStatement ps = conn.prepareStatement("SELECT nombre, precio_unitario FROM tbl_producto WHERE codigo=?")) {
            ps.setString(1, codigo);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                productoSeleccionado = rs.getString("nombre");
                lblNombreProducto.setText(productoSeleccionado);
                txtPrecioDetalle.setText(rs.getString("precio_unitario"));
            } else {
                productoSeleccionado = "";
                lblNombreProducto.setText("No encontrado");
            }
        } catch (Exception e) { mostrarAlerta(Alert.AlertType.ERROR,"Error",e.getMessage()); }
    }

    @FXML
    private void agregarProducto() {
        if (productoSeleccionado.isEmpty()) { mostrarAlerta(Alert.AlertType.WARNING,"Atención","Busca un producto antes de agregar."); return; }
        if (txtCantidadDetalle.getText().trim().isEmpty() || txtPrecioDetalle.getText().trim().isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING,"Atención","Ingresa cantidad y precio."); return; }
        try {
            int cant    = Integer.parseInt(txtCantidadDetalle.getText().trim());
            double prec = Double.parseDouble(txtPrecioDetalle.getText().trim());
            String cod  = txtCodigoProducto.getText().trim();
            for (ordenProveedorDetalleModelo d : listaDetalle)
                if (d.getCodigoProd().equals(cod)) { mostrarAlerta(Alert.AlertType.WARNING,"Duplicado","Este producto ya está en la lista."); return; }
            listaDetalle.add(new ordenProveedorDetalleModelo(0, 0, cod, productoSeleccionado, cant, prec));
            txtCodigoProducto.clear(); lblNombreProducto.setText("");
            txtCantidadDetalle.clear(); txtPrecioDetalle.clear(); productoSeleccionado = "";
        } catch (NumberFormatException ex) { mostrarAlerta(Alert.AlertType.WARNING,"Valores inválidos","Cantidad debe ser entero y precio número válido."); }
    }

    @FXML
    private void quitarProducto() {
        ordenProveedorDetalleModelo sel = tablaDetalle.getSelectionModel().getSelectedItem();
        if (sel == null) { mostrarAlerta(Alert.AlertType.WARNING,"Atención","Selecciona un producto para quitarlo."); return; }
        listaDetalle.remove(sel);
    }

    @FXML
    private void guardar() {
        if (!validarCampos()) return;
        String itemRnc   = cbRncProveedor.getValue();
        String rnc       = mapaRncNombre.getOrDefault(itemRnc, itemRnc);
        String proveedor = itemRnc != null && itemRnc.contains(" - ") ? itemRnc.split(" - ", 2)[1] : itemRnc;
        String categoria = getSeleccionados(chkBombones,chkTabletas,chkTrufas,chkBanos,chkRellenos,chkCoberturas,chkMateriaPrima,chkEmpaque);
        if (categoria.isEmpty()) { mostrarAlerta(Alert.AlertType.WARNING,"Categoría requerida","Selecciona al menos una categoría."); return; }
        double monto = listaDetalle.stream().mapToDouble(ordenProveedorDetalleModelo::getSubtotal).sum();

        String sql = "INSERT INTO tbl_orden_proveedor(rnc_proveedor,proveedor,fecha_requerida,prioridad,categoria,estado_pago,monto_total,descripcion) VALUES(?,?,?,?,?,?,?,?)";
        try (Connection conn = con.establecerConexion();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1,rnc); ps.setString(2,proveedor);
            ps.setDate(3,Date.valueOf(dpFechaRequerida.getValue()));
            ps.setString(4,cbPrioridad.getValue()); ps.setString(5,categoria);
            ps.setString(6,cbEstadoPago.getValue()); ps.setDouble(7,monto);
            ps.setString(8,txtDescripcion.getText().trim());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (!rs.next()) throw new SQLException("No se obtuvo ID.");
            int nuevoId = rs.getInt(1);

            String sqlDet = "INSERT INTO tbl_orden_proveedor_detalle(id_orden,codigo_prod,producto,cantidad,precio) VALUES(?,?,?,?,?)";
            try (PreparedStatement psDet = conn.prepareStatement(sqlDet)) {
                for (ordenProveedorDetalleModelo d : listaDetalle) {
                    psDet.setInt(1,nuevoId); psDet.setString(2,d.getCodigoProd());
                    psDet.setString(3,d.getProducto()); psDet.setInt(4,d.getCantidad());
                    psDet.setDouble(5,d.getPrecio()); psDet.addBatch(); }
                psDet.executeBatch(); }

            lista.add(0, new ordenProveedorModelo(nuevoId,rnc,proveedor,dpFechaRequerida.getValue(),
                cbPrioridad.getValue(),categoria,cbEstadoPago.getValue(),monto,txtDescripcion.getText().trim()));
            mostrarAlerta(Alert.AlertType.INFORMATION,"Exito","Orden #" + nuevoId + " guardada.");
            limpiar();
        } catch (Exception e) { mostrarAlerta(Alert.AlertType.ERROR,"Error al guardar",e.getMessage()); }
    }

    @FXML
    private void fnEditar() {
        ordenProveedorModelo sel = tablaOrdenes.getSelectionModel().getSelectedItem();
        if (sel == null) { mostrarAlerta(Alert.AlertType.WARNING,"Atencion","Selecciona una orden para editar."); return; }
        if (!validarCampos()) return;
        String itemRnc   = cbRncProveedor.getValue();
        String rnc       = mapaRncNombre.getOrDefault(itemRnc, itemRnc);
        String proveedor = itemRnc != null && itemRnc.contains(" - ") ? itemRnc.split(" - ",2)[1] : itemRnc;
        String categoria = getSeleccionados(chkBombones,chkTabletas,chkTrufas,chkBanos,chkRellenos,chkCoberturas,chkMateriaPrima,chkEmpaque);
        if (categoria.isEmpty()) { mostrarAlerta(Alert.AlertType.WARNING,"Categoria requerida","Selecciona al menos una categoria."); return; }
        double monto = listaDetalle.stream().mapToDouble(ordenProveedorDetalleModelo::getSubtotal).sum();

        String sql = "UPDATE tbl_orden_proveedor SET rnc_proveedor=?,proveedor=?,fecha_requerida=?,prioridad=?,categoria=?,estado_pago=?,monto_total=?,descripcion=? WHERE codigo=?";
        try (Connection conn = con.establecerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1,rnc); ps.setString(2,proveedor);
            ps.setDate(3,Date.valueOf(dpFechaRequerida.getValue()));
            ps.setString(4,cbPrioridad.getValue()); ps.setString(5,categoria);
            ps.setString(6,cbEstadoPago.getValue()); ps.setDouble(7,monto);
            ps.setString(8,txtDescripcion.getText().trim()); ps.setInt(9,sel.getCodigo());
            ps.executeUpdate();

            try (PreparedStatement psDel = conn.prepareStatement("DELETE FROM tbl_orden_proveedor_detalle WHERE id_orden=?")) {
                psDel.setInt(1,sel.getCodigo()); psDel.executeUpdate(); }
            String sqlDet = "INSERT INTO tbl_orden_proveedor_detalle(id_orden,codigo_prod,producto,cantidad,precio) VALUES(?,?,?,?,?)";
            try (PreparedStatement psDet = conn.prepareStatement(sqlDet)) {
                for (ordenProveedorDetalleModelo d : listaDetalle) {
                    psDet.setInt(1,sel.getCodigo()); psDet.setString(2,d.getCodigoProd());
                    psDet.setString(3,d.getProducto()); psDet.setInt(4,d.getCantidad());
                    psDet.setDouble(5,d.getPrecio()); psDet.addBatch(); }
                psDet.executeBatch(); }

            sel.setRncProveedor(rnc); sel.setProveedor(proveedor);
            sel.setFechaRequerida(dpFechaRequerida.getValue()); sel.setPrioridad(cbPrioridad.getValue());
            sel.setCategoria(categoria); sel.setEstadoPago(cbEstadoPago.getValue());
            sel.setMontoTotal(monto); sel.setDescripcion(txtDescripcion.getText().trim());
            tablaOrdenes.refresh();
            mostrarAlerta(Alert.AlertType.INFORMATION,"Exito","Orden actualizada correctamente.");
            limpiar();
        } catch (Exception e) { mostrarAlerta(Alert.AlertType.ERROR,"Error al editar",e.getMessage()); }
    }

    @FXML
    private void fnEliminar() {
        ordenProveedorModelo sel = tablaOrdenes.getSelectionModel().getSelectedItem();
        if (sel == null) { mostrarAlerta(Alert.AlertType.WARNING,"Atencion","Selecciona una orden para eliminar."); return; }
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setHeaderText(null);
        confirm.setContentText("Eliminar la orden #" + sel.getCodigo() + "?");
        confirm.showAndWait().ifPresent(r -> {
            if (r == ButtonType.OK) {
                try (Connection conn = con.establecerConexion()) {
                    try (PreparedStatement ps = conn.prepareStatement("DELETE FROM tbl_orden_proveedor_detalle WHERE id_orden=?")) {
                        ps.setInt(1,sel.getCodigo()); ps.executeUpdate(); }
                    try (PreparedStatement ps = conn.prepareStatement("DELETE FROM tbl_orden_proveedor WHERE codigo=?")) {
                        ps.setInt(1,sel.getCodigo()); ps.executeUpdate(); }
                    lista.remove(sel); listaDetalle.clear();
                    mostrarAlerta(Alert.AlertType.INFORMATION,"Exito","Orden eliminada correctamente.");
                    limpiar();
                } catch (Exception e) { mostrarAlerta(Alert.AlertType.ERROR,"Error al eliminar",e.getMessage()); }
            }
        });
    }

    @FXML
    private void fnBuscar() {
        String idTexto = txtId.getText().trim();
        if (idTexto.isEmpty()) { mostrarAlerta(Alert.AlertType.WARNING,"Atencion","Escribe un ID para buscar."); return; }
        try {
            int idBuscar = Integer.parseInt(idTexto);
            for (ordenProveedorModelo o : lista) {
                if (o.getCodigo() == idBuscar) {
                    tablaOrdenes.getSelectionModel().select(o);
                    tablaOrdenes.scrollTo(o);
                    cargarEnFormulario(o); cargarDetalle(o.getCodigo()); return;
                }
            }
            mostrarAlerta(Alert.AlertType.WARNING,"No encontrado","No existe orden con el ID " + idBuscar + ".");
        } catch (NumberFormatException ex) { mostrarAlerta(Alert.AlertType.WARNING,"ID invalido","El ID debe ser un numero entero."); }
    }

    @FXML
    private void limpiar() {
        txtId.clear(); cbRncProveedor.setValue(null); dpFechaRequerida.setValue(null);
        cbPrioridad.setValue(null); cbEstadoPago.setValue(null);
        txtDescripcion.clear(); txtMontoTotal.clear();
        for (CheckBox cb : new CheckBox[]{chkBombones,chkTabletas,chkTrufas,chkBanos,
                chkRellenos,chkCoberturas,chkMateriaPrima,chkEmpaque}) cb.setSelected(false);
        txtCodigoProducto.clear(); lblNombreProducto.setText("");
        txtCantidadDetalle.clear(); txtPrecioDetalle.clear();
        productoSeleccionado = ""; listaDetalle.clear();
        tablaOrdenes.getSelectionModel().clearSelection();
        generarSiguienteId();
    }

    private void cargarOrdenes() {
        lista.clear();
        String sql = "SELECT codigo,rnc_proveedor,proveedor,fecha_requerida,prioridad,categoria,estado_pago,monto_total,descripcion FROM tbl_orden_proveedor ORDER BY codigo DESC";
        try (Connection conn = con.establecerConexion();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Date d = rs.getDate("fecha_requerida");
                lista.add(new ordenProveedorModelo(
                    rs.getInt("codigo"), rs.getString("rnc_proveedor"),
                    rs.getString("proveedor") != null ? rs.getString("proveedor") : "",
                    d != null ? d.toLocalDate() : null,
                    rs.getString("prioridad"), rs.getString("categoria"),
                    rs.getString("estado_pago"), rs.getDouble("monto_total"),
                    rs.getString("descripcion") != null ? rs.getString("descripcion") : ""));
            }
        } catch (Exception e) { mostrarAlerta(Alert.AlertType.ERROR,"Error al cargar ordenes",e.getMessage()); }
    }

    private void cargarDetalle(int idOrden) {
        listaDetalle.clear();
        String sql = "SELECT id_detalle,id_orden,codigo_prod,producto,cantidad,precio FROM tbl_orden_proveedor_detalle WHERE id_orden=?";
        try (Connection conn = con.establecerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idOrden);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) listaDetalle.add(new ordenProveedorDetalleModelo(
                rs.getInt("id_detalle"), rs.getInt("id_orden"),
                rs.getString("codigo_prod"), rs.getString("producto"),
                rs.getInt("cantidad"), rs.getDouble("precio")));
        } catch (Exception e) { mostrarAlerta(Alert.AlertType.ERROR,"Error al cargar detalle",e.getMessage()); }
    }

    private String cargarResumenProductos(int idOrden) {
        StringBuilder sb = new StringBuilder();
        try (Connection conn = con.establecerConexion();
             PreparedStatement ps = conn.prepareStatement("SELECT producto FROM tbl_orden_proveedor_detalle WHERE id_orden=?")) {
            ps.setInt(1, idOrden);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) { if (sb.length()>0) sb.append(", "); sb.append(rs.getString("producto")); }
        } catch (Exception ignored) {}
        return sb.toString();
    }

    private void cargarEnFormulario(ordenProveedorModelo o) {
        txtId.setText(String.valueOf(o.getCodigo()));
        cbRncProveedor.getItems().stream()
            .filter(i -> i.startsWith(o.getRncProveedor()))
            .findFirst().ifPresent(cbRncProveedor::setValue);
        dpFechaRequerida.setValue(o.getFechaRequerida());
        cbPrioridad.setValue(o.getPrioridad());
        cbEstadoPago.setValue(o.getEstadoPago());
        txtDescripcion.setText(o.getDescripcion());
        txtMontoTotal.setText(String.format("%.2f", o.getMontoTotal()));
        String cat = o.getCategoria() != null ? o.getCategoria() : "";
        chkBombones.setSelected(cat.contains("Bombones"));
        chkTabletas.setSelected(cat.contains("Tabletas"));
        chkTrufas.setSelected(cat.contains("Trufas"));
        chkBanos.setSelected(cat.contains("Baños"));
        chkRellenos.setSelected(cat.contains("Rellenos"));
        chkCoberturas.setSelected(cat.contains("Coberturas"));
        chkMateriaPrima.setSelected(cat.contains("Materia prima"));
        chkEmpaque.setSelected(cat.contains("Empaque"));
    }

    private void recalcularMonto() {
        double total = listaDetalle.stream().mapToDouble(ordenProveedorDetalleModelo::getSubtotal).sum();
        txtMontoTotal.setText(String.format("%.2f", total));
    }

    private void generarSiguienteId() {
        try (Connection conn = con.establecerConexion();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT ISNULL(MAX(codigo),0)+1 AS sig FROM tbl_orden_proveedor")) {
            if (rs.next()) txtId.setText(String.valueOf(rs.getInt("sig")));
        } catch (Exception e) { txtId.setText("1"); }
    }

    private String getSeleccionados(CheckBox... cbs) {
        StringBuilder sb = new StringBuilder();
        for (CheckBox cb : cbs) if (cb.isSelected()) { if (sb.length()>0) sb.append(", "); sb.append(cb.getText()); }
        return sb.toString();
    }

    private boolean validarCampos() {
        if (cbRncProveedor.getValue() == null) { mostrarAlerta(Alert.AlertType.WARNING,"Requerido","Selecciona un proveedor."); return false; }
        if (dpFechaRequerida.getValue() == null) { mostrarAlerta(Alert.AlertType.WARNING,"Requerido","Selecciona la fecha requerida."); return false; }
        if (cbPrioridad.getValue() == null) { mostrarAlerta(Alert.AlertType.WARNING,"Requerido","Selecciona la prioridad."); return false; }
        if (cbEstadoPago.getValue() == null) { mostrarAlerta(Alert.AlertType.WARNING,"Requerido","Selecciona el estado de pago."); return false; }
        if (listaDetalle.isEmpty()) { mostrarAlerta(Alert.AlertType.WARNING,"Productos requeridos","Agrega al menos un producto."); return false; }
        return true;
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String msg) {
        Alert a = new Alert(tipo); a.setTitle(titulo); a.setHeaderText(null); a.setContentText(msg); a.showAndWait();
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