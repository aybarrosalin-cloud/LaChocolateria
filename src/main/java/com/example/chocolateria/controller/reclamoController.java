package com.example.chocolateria.controller;

import com.example.chocolateria.baseDeDatos.conexion;
import com.example.chocolateria.modelo.reclamoModelo;
import com.example.chocolateria.modelo.ordenClienteModelo;
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

public class reclamoController {

    @FXML private TextField        txtId;
    @FXML private ComboBox<String> cbCliente;
    @FXML private ComboBox<String> cbTipoReclamo;
    @FXML private ComboBox<String> cbEstado;
    @FXML private ComboBox<String> cbOrden;
    @FXML private TextArea         txtDescripcion;
    @FXML private TextField        txtBuscarTabla;

    // RadioButtons prioridad
    @FXML private RadioButton rbAlta;
    @FXML private RadioButton rbMedia;
    @FXML private RadioButton rbBaja;

    // Tabla ordenes del cliente
    @FXML private TableView<ordenClienteModelo>               tablaOrdenes;
    @FXML private TableColumn<ordenClienteModelo, Number>     colOrdenId;
    @FXML private TableColumn<ordenClienteModelo, String>     colOrdenCliente;
    @FXML private TableColumn<ordenClienteModelo, LocalDate>  colOrdenFecha;
    @FXML private TableColumn<ordenClienteModelo, LocalDate>  colOrdenEntrega;
    @FXML private TableColumn<ordenClienteModelo, String>     colOrdenEstado;
    @FXML private TableColumn<ordenClienteModelo, String>     colOrdenMetodo;

    // Tabla reclamos
    @FXML private TableView<reclamoModelo>                tablaReclamos;
    @FXML private TableColumn<reclamoModelo, Number>      colId;
    @FXML private TableColumn<reclamoModelo, String>      colCliente;
    @FXML private TableColumn<reclamoModelo, String>      colTipo;
    @FXML private TableColumn<reclamoModelo, String>      colEstado;
    @FXML private TableColumn<reclamoModelo, String>      colPrioridad;
    @FXML private TableColumn<reclamoModelo, Number>      colOrden;
    @FXML private TableColumn<reclamoModelo, LocalDate>   colFecha;

    private final ObservableList<reclamoModelo>      listaReclamos = FXCollections.observableArrayList();
    private final ObservableList<ordenClienteModelo> listaOrdenes  = FXCollections.observableArrayList();
    private final conexion con = new conexion();
    private final Map<String, Integer> mapaClientes = new HashMap<>();
    private final Map<String, Integer> mapaOrdenes  = new HashMap<>();
    private ToggleGroup grupoPrioridad;

    @FXML private Label lblUsuario;
    @FXML private ImageView imgFotoPerfil;

    @FXML
    public void initialize() {
        CargarPerfil.aplicar(lblUsuario, imgFotoPerfil);
        cbTipoReclamo.setItems(FXCollections.observableArrayList(
            "Producto en mal estado",
            "Producto incorrecto",
            "Entrega tardía",
            "Producto dañado en envio",
            "Cantidad incorrecta",
            "Problema de facturacion",
            "Mal servicio al cliente",
            "Producto no recibido",
            "Calidad no satisfactoria",
            "Otro"
        ));

        cbEstado.setItems(FXCollections.observableArrayList(
            "Abierto", "En proceso", "Resuelto", "Cerrado", "Rechazado"
        ));

        // Grupo de RadioButtons
        grupoPrioridad = new ToggleGroup();
        rbAlta.setToggleGroup(grupoPrioridad);
        rbMedia.setToggleGroup(grupoPrioridad);
        rbBaja.setToggleGroup(grupoPrioridad);
        rbMedia.setSelected(true);

        cargarClientes();

        // Al cambiar cliente: cargar sus ordenes en combobox y tabla
        cbCliente.setOnAction(e -> {
            String item = cbCliente.getValue();
            if (item != null) {
                int idCliente = mapaClientes.getOrDefault(item, 0);
                cargarOrdenesCliente(idCliente);
            }
        });

        // Columnas tabla ordenes
        colOrdenId.setCellValueFactory(d      -> d.getValue().idOrdenProperty());
        colOrdenCliente.setCellValueFactory(d -> d.getValue().clienteProperty());
        colOrdenFecha.setCellValueFactory(d   -> d.getValue().fechaRegistroProperty());
        colOrdenEntrega.setCellValueFactory(d -> d.getValue().fechaEntregaProperty());
        colOrdenEstado.setCellValueFactory(d  -> d.getValue().estadoProperty());
        colOrdenMetodo.setCellValueFactory(d  -> d.getValue().metodoPagoProperty());
        tablaOrdenes.setItems(listaOrdenes);

        // Click en orden -> seleccionar en combobox
        tablaOrdenes.getSelectionModel().selectedItemProperty().addListener(
            (obs, old, sel) -> {
                if (sel != null) {
                    String item = sel.getIdOrden() + " - " + sel.getFechaRegistro();
                    cbOrden.setValue(item);
                }
            });

        // Columnas tabla reclamos
        colId.setCellValueFactory(d        -> d.getValue().idReclamoProperty());
        colCliente.setCellValueFactory(d   -> d.getValue().clienteProperty());
        colTipo.setCellValueFactory(d      -> d.getValue().tipoReclamoProperty());
        colEstado.setCellValueFactory(d    -> d.getValue().estadoProperty());
        colPrioridad.setCellValueFactory(d -> d.getValue().prioridadProperty());
        colOrden.setCellValueFactory(d     -> d.getValue().idOrdenProperty());
        colFecha.setCellValueFactory(d     -> d.getValue().fechaReclamoProperty());

        // Color por prioridad
        tablaReclamos.setRowFactory(tv -> new TableRow<>() {
            @Override
            protected void updateItem(reclamoModelo r, boolean empty) {
                super.updateItem(r, empty);
                if (r == null || empty) { setStyle(""); return; }
                switch (r.getPrioridad()) {
                    case "Alta"  -> setStyle("-fx-background-color:#fde8e8;");
                    case "Media" -> setStyle("-fx-background-color:#fff8e1;");
                    case "Baja"  -> setStyle("-fx-background-color:#e8f5e9;");
                    default      -> setStyle("");
                }
            }
        });

        FilteredList<reclamoModelo> listaFiltrada = new FilteredList<>(listaReclamos, p -> true);
        txtBuscarTabla.textProperty().addListener((obs, o, nv) ->
            listaFiltrada.setPredicate(r -> {
                if (nv == null || nv.isBlank()) return true;
                String f = nv.toLowerCase();
                return r.getCliente().toLowerCase().contains(f)
                    || r.getTipoReclamo().toLowerCase().contains(f)
                    || r.getEstado().toLowerCase().contains(f)
                    || r.getPrioridad().toLowerCase().contains(f);
            }));
        tablaReclamos.setItems(listaFiltrada);

        tablaReclamos.getSelectionModel().selectedItemProperty().addListener(
            (obs, old, sel) -> { if (sel != null) cargarEnFormulario(sel); });

        cargarReclamos();
        generarSiguienteId();
    }

    private void cargarClientes() {
        String sql = "SELECT id_cliente, nombre + ' ' + apellido AS nombre_completo FROM tbl_clientes ORDER BY nombre";
        try (Connection conn = con.establecerConexion();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                int id     = rs.getInt("id_cliente");
                String nom = rs.getString("nombre_completo");
                String item= id + " - " + nom;
                cbCliente.getItems().add(item);
                mapaClientes.put(item, id);
            }
        } catch (Exception e) { mostrarAlerta(Alert.AlertType.ERROR,"Error",e.getMessage()); }
    }

    private void cargarOrdenesCliente(int idCliente) {
        listaOrdenes.clear();
        cbOrden.getItems().clear();
        mapaOrdenes.clear();

        String sql = "SELECT id_orden, cliente, fecha_registro, fecha_entrega, estado, metodo_pago " +
                     "FROM tbl_orden_cliente WHERE id_cliente = ? ORDER BY fecha_registro DESC";
        try (Connection conn = con.establecerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idCliente);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int idOrden = rs.getInt("id_orden");
                Date dReg   = rs.getDate("fecha_registro");
                Date dEnt   = rs.getDate("fecha_entrega");
                LocalDate fechaReg = dReg != null ? dReg.toLocalDate() : null;
                LocalDate fechaEnt = dEnt != null ? dEnt.toLocalDate() : null;

                listaOrdenes.add(new ordenClienteModelo(
                    idOrden, idCliente, rs.getString("cliente"),
                    fechaReg, fechaEnt,
                    rs.getString("metodo_pago"),
                    rs.getString("estado"),
                    ""));

                String item = idOrden + " - " + fechaReg;
                cbOrden.getItems().add(item);
                mapaOrdenes.put(item, idOrden);
            }
        } catch (Exception e) { mostrarAlerta(Alert.AlertType.ERROR,"Error al cargar ordenes",e.getMessage()); }
    }

    @FXML
    private void guardar() {
        if (!validarCampos()) return;
        String itemCliente   = cbCliente.getValue();
        int idCliente        = mapaClientes.getOrDefault(itemCliente, 0);
        String nombreCliente = itemCliente != null && itemCliente.contains(" - ") ? itemCliente.split(" - ",2)[1] : itemCliente;
        String itemOrden     = cbOrden.getValue();
        int idOrden          = itemOrden != null ? mapaOrdenes.getOrDefault(itemOrden, 0) : 0;
        String prioridad     = rbAlta.isSelected() ? "Alta" : rbBaja.isSelected() ? "Baja" : "Media";

        String sql = "INSERT INTO tbl_reclamo(id_cliente,cliente,id_orden,tipo_reclamo,estado,prioridad,descripcion,fecha_reclamo) VALUES(?,?,?,?,?,?,?,?)";
        try (Connection conn = con.establecerConexion();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, idCliente);
            ps.setString(2, nombreCliente);
            ps.setObject(3, idOrden > 0 ? idOrden : null);
            ps.setString(4, cbTipoReclamo.getValue());
            ps.setString(5, cbEstado.getValue());
            ps.setString(6, prioridad);
            ps.setString(7, txtDescripcion.getText().trim());
            ps.setDate(8, Date.valueOf(LocalDate.now()));
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            int nuevoId = rs.next() ? rs.getInt(1) : 0;

            listaReclamos.add(0, new reclamoModelo(nuevoId, idCliente, nombreCliente,
                idOrden, cbTipoReclamo.getValue(), cbEstado.getValue(),
                prioridad, txtDescripcion.getText().trim(), LocalDate.now()));

            mostrarAlerta(Alert.AlertType.INFORMATION,"Exito","Reclamo #" + nuevoId + " registrado correctamente.");
            limpiar();
        } catch (Exception e) { mostrarAlerta(Alert.AlertType.ERROR,"Error al guardar",e.getMessage()); }
    }

    @FXML
    private void fnEditar() {
        reclamoModelo sel = tablaReclamos.getSelectionModel().getSelectedItem();
        if (sel == null) { mostrarAlerta(Alert.AlertType.WARNING,"Atencion","Selecciona un reclamo para editar."); return; }
        if (!validarCampos()) return;

        String itemCliente   = cbCliente.getValue();
        int idCliente        = mapaClientes.getOrDefault(itemCliente, 0);
        String nombreCliente = itemCliente != null && itemCliente.contains(" - ") ? itemCliente.split(" - ",2)[1] : itemCliente;
        String itemOrden     = cbOrden.getValue();
        int idOrden          = itemOrden != null ? mapaOrdenes.getOrDefault(itemOrden, 0) : 0;
        String prioridad     = rbAlta.isSelected() ? "Alta" : rbBaja.isSelected() ? "Baja" : "Media";

        String sql = "UPDATE tbl_reclamo SET id_cliente=?,cliente=?,id_orden=?,tipo_reclamo=?,estado=?,prioridad=?,descripcion=? WHERE id_reclamo=?";
        try (Connection conn = con.establecerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idCliente); ps.setString(2, nombreCliente);
            ps.setObject(3, idOrden > 0 ? idOrden : null);
            ps.setString(4, cbTipoReclamo.getValue());
            ps.setString(5, cbEstado.getValue());
            ps.setString(6, prioridad);
            ps.setString(7, txtDescripcion.getText().trim());
            ps.setInt(8, sel.getIdReclamo());
            ps.executeUpdate();

            sel.setCliente(nombreCliente); sel.setIdOrden(idOrden);
            sel.setTipoReclamo(cbTipoReclamo.getValue()); sel.setEstado(cbEstado.getValue());
            sel.setPrioridad(prioridad); sel.setDescripcion(txtDescripcion.getText().trim());
            tablaReclamos.refresh();
            mostrarAlerta(Alert.AlertType.INFORMATION,"Exito","Reclamo actualizado correctamente.");
            limpiar();
        } catch (Exception e) { mostrarAlerta(Alert.AlertType.ERROR,"Error al editar",e.getMessage()); }
    }

    @FXML
    private void fnEliminar() {
        reclamoModelo sel = tablaReclamos.getSelectionModel().getSelectedItem();
        if (sel == null) { mostrarAlerta(Alert.AlertType.WARNING,"Atencion","Selecciona un reclamo para eliminar."); return; }
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setHeaderText(null);
        confirm.setContentText("Eliminar el reclamo #" + sel.getIdReclamo() + "?");
        confirm.showAndWait().ifPresent(r -> {
            if (r == ButtonType.OK) {
                try (Connection conn = con.establecerConexion();
                     PreparedStatement ps = conn.prepareStatement("DELETE FROM tbl_reclamo WHERE id_reclamo=?")) {
                    ps.setInt(1, sel.getIdReclamo()); ps.executeUpdate();
                    listaReclamos.remove(sel);
                    mostrarAlerta(Alert.AlertType.INFORMATION,"Exito","Reclamo eliminado correctamente.");
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
            for (reclamoModelo r : listaReclamos) {
                if (r.getIdReclamo() == idBuscar) {
                    tablaReclamos.getSelectionModel().select(r);
                    tablaReclamos.scrollTo(r);
                    cargarEnFormulario(r); return;
                }
            }
            mostrarAlerta(Alert.AlertType.WARNING,"No encontrado","No existe reclamo con el ID " + idBuscar + ".");
        } catch (NumberFormatException ex) { mostrarAlerta(Alert.AlertType.WARNING,"ID invalido","El ID debe ser un numero entero."); }
    }

    @FXML
    private void limpiar() {
        txtId.clear(); cbCliente.setValue(null); cbTipoReclamo.setValue(null);
        cbEstado.setValue(null); cbOrden.getItems().clear(); cbOrden.setValue(null);
        txtDescripcion.clear(); rbMedia.setSelected(true);
        listaOrdenes.clear(); mapaOrdenes.clear();
        tablaReclamos.getSelectionModel().clearSelection();
        generarSiguienteId();
    }

    private void cargarReclamos() {
        listaReclamos.clear();
        String sql = "SELECT id_reclamo,id_cliente,cliente,ISNULL(id_orden,0) AS id_orden,tipo_reclamo,estado,prioridad,descripcion,fecha_reclamo FROM tbl_reclamo ORDER BY id_reclamo DESC";
        try (Connection conn = con.establecerConexion();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Date d = rs.getDate("fecha_reclamo");
                listaReclamos.add(new reclamoModelo(
                    rs.getInt("id_reclamo"), rs.getInt("id_cliente"),
                    rs.getString("cliente"), rs.getInt("id_orden"),
                    rs.getString("tipo_reclamo"), rs.getString("estado"),
                    rs.getString("prioridad"),
                    rs.getString("descripcion") != null ? rs.getString("descripcion") : "",
                    d != null ? d.toLocalDate() : null));
            }
        } catch (Exception e) { mostrarAlerta(Alert.AlertType.ERROR,"Error al cargar reclamos",e.getMessage()); }
    }

    private void cargarEnFormulario(reclamoModelo r) {
        txtId.setText(String.valueOf(r.getIdReclamo()));
        cbCliente.getItems().stream()
            .filter(i -> i.startsWith(r.getIdCliente() + " - "))
            .findFirst().ifPresent(item -> {
                cbCliente.setValue(item);
                cargarOrdenesCliente(r.getIdCliente());
            });
        cbTipoReclamo.setValue(r.getTipoReclamo());
        cbEstado.setValue(r.getEstado());
        txtDescripcion.setText(r.getDescripcion());
        switch (r.getPrioridad()) {
            case "Alta" -> rbAlta.setSelected(true);
            case "Baja" -> rbBaja.setSelected(true);
            default     -> rbMedia.setSelected(true);
        }
        if (r.getIdOrden() > 0) {
            cbOrden.getItems().stream()
                .filter(i -> i.startsWith(r.getIdOrden() + " - "))
                .findFirst().ifPresent(cbOrden::setValue);
        }
    }

    private void generarSiguienteId() {
        try (Connection conn = con.establecerConexion();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT ISNULL(MAX(id_reclamo),0)+1 AS sig FROM tbl_reclamo")) {
            if (rs.next()) txtId.setText(String.valueOf(rs.getInt("sig")));
        } catch (Exception e) { txtId.setText("1"); }
    }

    private boolean validarCampos() {
        if (cbCliente.getValue() == null) { mostrarAlerta(Alert.AlertType.WARNING,"Requerido","Selecciona un cliente."); return false; }
        if (cbTipoReclamo.getValue() == null) { mostrarAlerta(Alert.AlertType.WARNING,"Requerido","Selecciona el tipo de reclamo."); return false; }
        if (cbEstado.getValue() == null) { mostrarAlerta(Alert.AlertType.WARNING,"Requerido","Selecciona el estado."); return false; }
        if (txtDescripcion.getText().trim().isEmpty()) { mostrarAlerta(Alert.AlertType.WARNING,"Requerido","Escribe una descripcion del reclamo."); return false; }
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