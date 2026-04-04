package com.example.chocolateria.controller;

import com.example.chocolateria.baseDeDatos.conexion;
import com.example.chocolateria.modelo.envioModelo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class envioController {

    @FXML private TextField        txtId;
    @FXML private ComboBox<String> cbCliente;
    @FXML private DatePicker       dpFechaEnvio;
    @FXML private DatePicker       dpFechaEntrega;
    @FXML private ComboBox<String> cbTransportista;
    @FXML private ComboBox<String> cbTemperatura;
    @FXML private ComboBox<String> cbEstado;
    @FXML private TextField        txtNumeroGuia;
    @FXML private ComboBox<String> cbProvincia;
    @FXML private ComboBox<String> cbCiudad;
    @FXML private TextField        txtDireccion;
    @FXML private TextField        txtBuscarTabla;

    @FXML private TableView<envioModelo>                    tablaEnvios;
    @FXML private TableColumn<envioModelo, Number>          colId;
    @FXML private TableColumn<envioModelo, String>          colCliente;
    @FXML private TableColumn<envioModelo, LocalDate>       colFechaEnvio;
    @FXML private TableColumn<envioModelo, LocalDate>       colFechaEntrega;
    @FXML private TableColumn<envioModelo, String>          colTransportista;
    @FXML private TableColumn<envioModelo, String>          colEstado;
    @FXML private TableColumn<envioModelo, String>          colGuia;
    @FXML private TableColumn<envioModelo, String>          colProvincia;
    @FXML private TableColumn<envioModelo, String>          colCiudad;

    private final ObservableList<envioModelo> lista = FXCollections.observableArrayList();
    private final conexion con = new conexion();
    private final Map<String, Integer> mapaClientes = new HashMap<>();

    // Mapa provincias -> municipios RD
    private static final Map<String, String[]> PROVINCIAS = new LinkedHashMap<>();
    static {
        PROVINCIAS.put("Azua", new String[]{"Azua","Las Charcas","Las Yayas de Viajama","Padre Las Casas","Peralta","Sabana Yegua","Pueblo Viejo","Tábara Arriba","Villarpando","Guayabal","Estebanía"});
        PROVINCIAS.put("Bahoruco", new String[]{"Neyba","Galván","Los Ríos","Tamayo","Villa Jaragua","Postrer Río"});
        PROVINCIAS.put("Barahona", new String[]{"Barahona","Cabral","Enriquillo","Fundación","Jaquimeyes","La Ciénaga","Las Salinas","Paraíso","Polo","El Peñón","Pescadería","Vicente Noble","Canoa"});
        PROVINCIAS.put("Dajabón", new String[]{"Dajabón","El Pino","Loma de Cabrera","Partido","Restauración"});
        PROVINCIAS.put("Distrito Nacional", new String[]{"Santo Domingo de Guzmán"});
        PROVINCIAS.put("Duarte", new String[]{"San Francisco de Macorís","Arenoso","Castillo","Eugenio María de Hostos","Las Guáranas","Pimentel","Villa Riva","El Valle","Hostos"});
        PROVINCIAS.put("Elías Piña", new String[]{"Comendador","Bánica","El Llano","Hondo Valle","Juan Santiago","Pedro Santana"});
        PROVINCIAS.put("El Seibo", new String[]{"El Seibo","Miches"});
        PROVINCIAS.put("Espaillat", new String[]{"Moca","Cayetano Germosén","Gaspar Hernández","Jamao al Norte","José Contreras","La Vigía","Palm Rucio","San Víctor","Veragua"});
        PROVINCIAS.put("Hato Mayor", new String[]{"Hato Mayor del Rey","El Valle","Sabana de la Mar"});
        PROVINCIAS.put("Hermanas Mirabal", new String[]{"Salcedo","Tenares","Villa Tapia"});
        PROVINCIAS.put("Independencia", new String[]{"Jimaní","Cristóbal","Duvergé","La Descubierta","Mella","Postrer Río"});
        PROVINCIAS.put("La Altagracia", new String[]{"Higüey","San Rafael del Yuma"});
        PROVINCIAS.put("La Romana", new String[]{"La Romana","Guaymate","Villa Hermosa"});
        PROVINCIAS.put("La Vega", new String[]{"La Concepción de La Vega","Constanza","Jarabacoa","Jima Abajo"});
        PROVINCIAS.put("María Trinidad Sánchez", new String[]{"Nagua","Cabrera","El Factor","Río San Juan"});
        PROVINCIAS.put("Monseñor Nouel", new String[]{"Bonao","Maimón","Piedra Blanca"});
        PROVINCIAS.put("Monte Cristi", new String[]{"Monte Cristi","Castañuelas","Guayubín","Las Matas de Santa Cruz","Pepillo Salcedo","Villa Vásquez"});
        PROVINCIAS.put("Monte Plata", new String[]{"Monte Plata","Bayaguana","Peralvillo","Sabana Grande de Boyá","Yamasá"});
        PROVINCIAS.put("Pedernales", new String[]{"Pedernales","Oviedo"});
        PROVINCIAS.put("Peravia", new String[]{"Baní","Nizao"});
        PROVINCIAS.put("Puerto Plata", new String[]{"Puerto Plata","Altamira","Guananico","Imbert","Los Hidalgos","Luperón","Sosúa","Villa Isabela","Villa Montellano"});
        PROVINCIAS.put("Samaná", new String[]{"Samaná","Las Terrenas","Sánchez"});
        PROVINCIAS.put("Sánchez Ramírez", new String[]{"Cotuí","Cevicos","Fantino","La Mata"});
        PROVINCIAS.put("San Cristóbal", new String[]{"San Cristóbal","Bajos de Haina","Cambita Garabitos","Los Cacaos","Palenque","San Gregorio de Nigua","Villa Altagracia","Yaguate"});
        PROVINCIAS.put("San José de Ocoa", new String[]{"San José de Ocoa","Rancho Arriba","Sabana Larga"});
        PROVINCIAS.put("San Juan", new String[]{"San Juan de la Maguana","Bohechío","El Cercado","Juan de Herrera","Las Matas de Farfán","Vallejuelo"});
        PROVINCIAS.put("San Pedro de Macorís", new String[]{"San Pedro de Macorís","Consuelo","Gautier","Guayacanes","Quisqueya","Ramón Santana"});
        PROVINCIAS.put("Santiago", new String[]{"Santiago de los Caballeros","Bisonó","Jánico","Licey al Medio","Puñal","Sabana Iglesia","San José de las Matas","Tamboril","Villa González"});
        PROVINCIAS.put("Santiago Rodríguez", new String[]{"Sabaneta","Monción","San Ignacio de Sabaneta"});
        PROVINCIAS.put("Santo Domingo", new String[]{"Santo Domingo Este","Santo Domingo Norte","Santo Domingo Oeste","Boca Chica","Los Alcarrizos","Pedro Brand","San Antonio de Guerra"});
        PROVINCIAS.put("Valverde", new String[]{"Mao","Esperanza","Laguna Salada"});
    }

    @FXML
    public void initialize() {
        cbTemperatura.setItems(FXCollections.observableArrayList(
            "Temperatura ambiente (15-25°C)",
            "Refrigerado (2-8°C)",
            "Fresco (8-15°C)",
            "Congelado (-18°C o menos)"
        ));

        cbEstado.setItems(FXCollections.observableArrayList(
            "Pendiente", "En tránsito", "Entregado", "Devuelto", "Cancelado"
        ));

        // Cargar provincias
        cbProvincia.setItems(FXCollections.observableArrayList(PROVINCIAS.keySet()));
        cbProvincia.setOnAction(e -> actualizarCiudades());

        cargarClientes();
        cargarTransportistas();

        colId.setCellValueFactory(d           -> d.getValue().idEnvioProperty());
        colCliente.setCellValueFactory(d      -> d.getValue().clienteProperty());
        colFechaEnvio.setCellValueFactory(d   -> d.getValue().fechaEnvioProperty());
        colFechaEntrega.setCellValueFactory(d -> d.getValue().fechaEntregaProperty());
        colTransportista.setCellValueFactory(d-> d.getValue().transportistaProperty());
        colEstado.setCellValueFactory(d       -> d.getValue().estadoProperty());
        colGuia.setCellValueFactory(d         -> d.getValue().numeroGuiaProperty());
        colProvincia.setCellValueFactory(d    -> d.getValue().provinciaProperty());
        colCiudad.setCellValueFactory(d       -> d.getValue().ciudadProperty());

        tablaEnvios.setRowFactory(tv -> new TableRow<>() {
            @Override
            protected void updateItem(envioModelo e, boolean empty) {
                super.updateItem(e, empty);
                if (e == null || empty) { setStyle(""); return; }
                switch (e.getEstado()) {
                    case "Entregado"   -> setStyle("-fx-background-color:#e8f5e9;");
                    case "Cancelado","Devuelto" -> setStyle("-fx-background-color:#fde8e8;");
                    case "En tránsito" -> setStyle("-fx-background-color:#fff8e1;");
                    default            -> setStyle("");
                }
            }
        });

        FilteredList<envioModelo> listaFiltrada = new FilteredList<>(lista, p -> true);
        txtBuscarTabla.textProperty().addListener((obs, o, nv) ->
            listaFiltrada.setPredicate(e -> {
                if (nv == null || nv.isBlank()) return true;
                String f = nv.toLowerCase();
                return e.getCliente().toLowerCase().contains(f)
                    || e.getEstado().toLowerCase().contains(f)
                    || e.getTransportista().toLowerCase().contains(f)
                    || e.getProvincia().toLowerCase().contains(f);
            }));
        tablaEnvios.setItems(listaFiltrada);

        tablaEnvios.getSelectionModel().selectedItemProperty().addListener(
            (obs, old, sel) -> { if (sel != null) cargarEnFormulario(sel); });

        cargarEnvios();
        generarSiguienteId();
    }

    private void actualizarCiudades() {
        String prov = cbProvincia.getValue();
        cbCiudad.getItems().clear();
        if (prov != null && PROVINCIAS.containsKey(prov)) {
            cbCiudad.setItems(FXCollections.observableArrayList(PROVINCIAS.get(prov)));
        }
    }

    private void cargarClientes() {
        String sql = "SELECT id_cliente, nombre + ' ' + apellido AS nombre_completo FROM tbl_clientes ORDER BY nombre";
        try (Connection conn = con.establecerConexion();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                int id      = rs.getInt("id_cliente");
                String nom  = rs.getString("nombre_completo");
                String item = id + " - " + nom;
                cbCliente.getItems().add(item);
                mapaClientes.put(item, id);
            }
        } catch (Exception e) { mostrarAlerta(Alert.AlertType.ERROR,"Error",e.getMessage()); }
    }

    private void cargarTransportistas() {
        String sql = "SELECT nombre FROM tbl_transportista WHERE estado='Activo' ORDER BY nombre";
        try (Connection conn = con.establecerConexion();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) cbTransportista.getItems().add(rs.getString("nombre"));
        } catch (Exception e) { mostrarAlerta(Alert.AlertType.ERROR,"Error",e.getMessage()); }
    }

    @FXML
    private void guardar() {
        if (!validarCampos()) return;
        String itemCliente = cbCliente.getValue();
        int idCliente = mapaClientes.getOrDefault(itemCliente, 0);
        String nombreCliente = itemCliente != null && itemCliente.contains(" - ") ? itemCliente.split(" - ",2)[1] : itemCliente;

        String sql = "INSERT INTO tbl_envio(id_cliente,cliente,fecha_envio,fecha_entrega,transportista,temperatura_transporte,estado,numero_guia,provincia,ciudad,direccion) VALUES(?,?,?,?,?,?,?,?,?,?,?)";
        try (Connection conn = con.establecerConexion();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, idCliente);
            ps.setString(2, nombreCliente);
            ps.setDate(3, Date.valueOf(dpFechaEnvio.getValue()));
            ps.setObject(4, dpFechaEntrega.getValue() != null ? Date.valueOf(dpFechaEntrega.getValue()) : null);
            ps.setString(5, cbTransportista.getValue());
            ps.setString(6, cbTemperatura.getValue());
            ps.setString(7, cbEstado.getValue());
            ps.setString(8, txtNumeroGuia.getText().trim());
            ps.setString(9, cbProvincia.getValue());
            ps.setString(10, cbCiudad.getValue() != null ? cbCiudad.getValue() : "");
            ps.setString(11, txtDireccion.getText().trim());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            int nuevoId = rs.next() ? rs.getInt(1) : 0;

            lista.add(0, new envioModelo(nuevoId, idCliente, nombreCliente,
                dpFechaEnvio.getValue(), dpFechaEntrega.getValue(),
                cbTransportista.getValue(), cbTemperatura.getValue(),
                cbEstado.getValue(), txtNumeroGuia.getText().trim(),
                cbProvincia.getValue(), cbCiudad.getValue() != null ? cbCiudad.getValue() : "",
                txtDireccion.getText().trim()));

            mostrarAlerta(Alert.AlertType.INFORMATION,"Exito","Envio #" + nuevoId + " registrado correctamente.");
            limpiar();
        } catch (Exception e) { mostrarAlerta(Alert.AlertType.ERROR,"Error al guardar",e.getMessage()); }
    }

    @FXML
    private void fnEditar() {
        envioModelo sel = tablaEnvios.getSelectionModel().getSelectedItem();
        if (sel == null) { mostrarAlerta(Alert.AlertType.WARNING,"Atencion","Selecciona un envio para editar."); return; }
        if (!validarCampos()) return;
        String itemCliente   = cbCliente.getValue();
        int idCliente        = mapaClientes.getOrDefault(itemCliente, 0);
        String nombreCliente = itemCliente != null && itemCliente.contains(" - ") ? itemCliente.split(" - ",2)[1] : itemCliente;

        String sql = "UPDATE tbl_envio SET id_cliente=?,cliente=?,fecha_envio=?,fecha_entrega=?,transportista=?,temperatura_transporte=?,estado=?,numero_guia=?,provincia=?,ciudad=?,direccion=? WHERE id_envio=?";
        try (Connection conn = con.establecerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idCliente); ps.setString(2, nombreCliente);
            ps.setDate(3, Date.valueOf(dpFechaEnvio.getValue()));
            ps.setObject(4, dpFechaEntrega.getValue() != null ? Date.valueOf(dpFechaEntrega.getValue()) : null);
            ps.setString(5, cbTransportista.getValue()); ps.setString(6, cbTemperatura.getValue());
            ps.setString(7, cbEstado.getValue()); ps.setString(8, txtNumeroGuia.getText().trim());
            ps.setString(9, cbProvincia.getValue());
            ps.setString(10, cbCiudad.getValue() != null ? cbCiudad.getValue() : "");
            ps.setString(11, txtDireccion.getText().trim()); ps.setInt(12, sel.getIdEnvio());
            ps.executeUpdate();

            sel.setCliente(nombreCliente); sel.setFechaEnvio(dpFechaEnvio.getValue());
            sel.setFechaEntrega(dpFechaEntrega.getValue()); sel.setTransportista(cbTransportista.getValue());
            sel.setTemperaturaTransporte(cbTemperatura.getValue()); sel.setEstado(cbEstado.getValue());
            sel.setNumeroGuia(txtNumeroGuia.getText().trim()); sel.setProvincia(cbProvincia.getValue());
            sel.setCiudad(cbCiudad.getValue() != null ? cbCiudad.getValue() : "");
            sel.setDireccion(txtDireccion.getText().trim());
            tablaEnvios.refresh();
            mostrarAlerta(Alert.AlertType.INFORMATION,"Exito","Envio actualizado correctamente.");
            limpiar();
        } catch (Exception e) { mostrarAlerta(Alert.AlertType.ERROR,"Error al editar",e.getMessage()); }
    }

    @FXML
    private void fnEliminar() {
        envioModelo sel = tablaEnvios.getSelectionModel().getSelectedItem();
        if (sel == null) { mostrarAlerta(Alert.AlertType.WARNING,"Atencion","Selecciona un envio para eliminar."); return; }
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setHeaderText(null);
        confirm.setContentText("Eliminar el envio #" + sel.getIdEnvio() + "?");
        confirm.showAndWait().ifPresent(r -> {
            if (r == ButtonType.OK) {
                try (Connection conn = con.establecerConexion();
                     PreparedStatement ps = conn.prepareStatement("DELETE FROM tbl_envio WHERE id_envio=?")) {
                    ps.setInt(1, sel.getIdEnvio()); ps.executeUpdate();
                    lista.remove(sel);
                    mostrarAlerta(Alert.AlertType.INFORMATION,"Exito","Envio eliminado correctamente.");
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
            for (envioModelo e : lista) {
                if (e.getIdEnvio() == idBuscar) {
                    tablaEnvios.getSelectionModel().select(e);
                    tablaEnvios.scrollTo(e);
                    cargarEnFormulario(e); return;
                }
            }
            mostrarAlerta(Alert.AlertType.WARNING,"No encontrado","No existe envio con el ID " + idBuscar + ".");
        } catch (NumberFormatException ex) { mostrarAlerta(Alert.AlertType.WARNING,"ID invalido","El ID debe ser un numero entero."); }
    }

    @FXML
    private void limpiar() {
        txtId.clear(); cbCliente.setValue(null); dpFechaEnvio.setValue(null);
        dpFechaEntrega.setValue(null); cbTransportista.setValue(null);
        cbTemperatura.setValue(null); cbEstado.setValue(null);
        txtNumeroGuia.clear(); cbProvincia.setValue(null);
        cbCiudad.getItems().clear(); cbCiudad.setValue(null);
        txtDireccion.clear();
        tablaEnvios.getSelectionModel().clearSelection();
        generarSiguienteId();
    }

    private void cargarEnvios() {
        lista.clear();
        String sql = "SELECT id_envio,id_cliente,cliente,fecha_envio,fecha_entrega,transportista,temperatura_transporte,estado,numero_guia,provincia,ciudad,direccion FROM tbl_envio ORDER BY id_envio DESC";
        try (Connection conn = con.establecerConexion();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Date dEnv = rs.getDate("fecha_envio");
                Date dEnt = rs.getDate("fecha_entrega");
                lista.add(new envioModelo(
                    rs.getInt("id_envio"), rs.getInt("id_cliente"),
                    rs.getString("cliente"),
                    dEnv != null ? dEnv.toLocalDate() : null,
                    dEnt != null ? dEnt.toLocalDate() : null,
                    rs.getString("transportista"),
                    rs.getString("temperatura_transporte"),
                    rs.getString("estado"),
                    rs.getString("numero_guia") != null ? rs.getString("numero_guia") : "",
                    rs.getString("provincia"),
                    rs.getString("ciudad") != null ? rs.getString("ciudad") : "",
                    rs.getString("direccion")));
            }
        } catch (Exception e) { mostrarAlerta(Alert.AlertType.ERROR,"Error al cargar envios",e.getMessage()); }
    }

    private void cargarEnFormulario(envioModelo e) {
        txtId.setText(String.valueOf(e.getIdEnvio()));
        cbCliente.getItems().stream()
            .filter(i -> i.startsWith(e.getIdCliente() + " - "))
            .findFirst().ifPresent(cbCliente::setValue);
        dpFechaEnvio.setValue(e.getFechaEnvio());
        dpFechaEntrega.setValue(e.getFechaEntrega());
        cbTransportista.setValue(e.getTransportista());
        cbTemperatura.setValue(e.getTemperaturaTransporte());
        cbEstado.setValue(e.getEstado());
        txtNumeroGuia.setText(e.getNumeroGuia());
        cbProvincia.setValue(e.getProvincia());
        actualizarCiudades();
        cbCiudad.setValue(e.getCiudad());
        txtDireccion.setText(e.getDireccion());
    }

    private void generarSiguienteId() {
        try (Connection conn = con.establecerConexion();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT ISNULL(MAX(id_envio),0)+1 AS sig FROM tbl_envio")) {
            if (rs.next()) txtId.setText(String.valueOf(rs.getInt("sig")));
        } catch (Exception e) { txtId.setText("1"); }
    }

    private boolean validarCampos() {
        if (cbCliente.getValue() == null) { mostrarAlerta(Alert.AlertType.WARNING,"Requerido","Selecciona un cliente."); return false; }
        if (dpFechaEnvio.getValue() == null) { mostrarAlerta(Alert.AlertType.WARNING,"Requerido","Selecciona la fecha de envio."); return false; }
        if (cbTransportista.getValue() == null) { mostrarAlerta(Alert.AlertType.WARNING,"Requerido","Selecciona un transportista."); return false; }
        if (cbTemperatura.getValue() == null) { mostrarAlerta(Alert.AlertType.WARNING,"Requerido","Selecciona la temperatura de transporte."); return false; }
        if (cbEstado.getValue() == null) { mostrarAlerta(Alert.AlertType.WARNING,"Requerido","Selecciona el estado."); return false; }
        if (cbProvincia.getValue() == null) { mostrarAlerta(Alert.AlertType.WARNING,"Requerido","Selecciona la provincia."); return false; }
        if (txtDireccion.getText().trim().isEmpty()) { mostrarAlerta(Alert.AlertType.WARNING,"Requerido","Escribe la direccion."); return false; }
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
    @FXML private void salir(javafx.event.ActionEvent e)                  { Navegacion.irA("/vistasFinales/vistaPrincipal.fxml", e, 949, 533); }
}