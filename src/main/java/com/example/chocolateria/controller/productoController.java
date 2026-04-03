package com.example.chocolateria.controller;

import com.example.chocolateria.baseDeDatos.conexion;
import com.example.chocolateria.modelo.productoModelo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.sql.*;

public class productoController {

    @FXML private TextField        txtCodigo;
    @FXML private TextField        txtNombre;
    @FXML private TextField        txtPrecioUnitario;
    @FXML private TextField        txtPrecioMayor;
    @FXML private TextArea         txtDescripcion;
    @FXML private ComboBox<String> cbUnidadMedida;
    @FXML private TextField        txtBuscarTabla;

    @FXML private CheckBox chkBombones, chkTabletas, chkTrufas, chkBanos;
    @FXML private CheckBox chkRellenos, chkCoberturas, chkGranolas, chkMateriaPrima, chkEmpaque;
    @FXML private CheckBox chkTerminado, chkSemielaborado, chkInsumo;
    @FXML private CheckBox chkMateriaP, chkEmpaqueT, chkSubproducto;

    @FXML private TableView<productoModelo>           tablaProductos;
    @FXML private TableColumn<productoModelo, String> colCodigo;
    @FXML private TableColumn<productoModelo, String> colNombre;
    @FXML private TableColumn<productoModelo, Number> colPrecioU;
    @FXML private TableColumn<productoModelo, Number> colPrecioM;
    @FXML private TableColumn<productoModelo, String> colUnidad;
    @FXML private TableColumn<productoModelo, String> colCategoria;
    @FXML private TableColumn<productoModelo, String> colTipo;
    @FXML private TableColumn<productoModelo, Number> colStock;

    private final ObservableList<productoModelo> lista = FXCollections.observableArrayList();
    private final conexion con = new conexion();

    @FXML
    public void initialize() {
        cbUnidadMedida.setItems(FXCollections.observableArrayList(
            "Unidad", "Caja", "Paquete", "Kilogramo", "Gramo",
            "Litro", "Mililitro", "Bolsa", "Barra", "Docena"));

        colCodigo.setCellValueFactory(d    -> d.getValue().codigoProperty());
        colNombre.setCellValueFactory(d    -> d.getValue().nombreProperty());
        colPrecioU.setCellValueFactory(d   -> d.getValue().precioUnitarioProperty());
        colPrecioM.setCellValueFactory(d   -> d.getValue().precioMayorProperty());
        colUnidad.setCellValueFactory(d    -> d.getValue().unidadMedidaProperty());
        colCategoria.setCellValueFactory(d -> d.getValue().categoriaProperty());
        colTipo.setCellValueFactory(d      -> d.getValue().tipoProperty());
        colStock.setCellValueFactory(d     -> d.getValue().stockProperty());

        for (CheckBox cb : new CheckBox[]{chkBombones,chkTabletas,chkTrufas,chkBanos,
                chkRellenos,chkCoberturas,chkGranolas,chkMateriaPrima,chkEmpaque}) {
            cb.selectedProperty().addListener((obs, old, nv) -> {
                if (tablaProductos.getSelectionModel().getSelectedItem() == null)
                    actualizarCodigo();
            });
        }

        FilteredList<productoModelo> listaFiltrada = new FilteredList<>(lista, p -> true);
        txtBuscarTabla.textProperty().addListener((obs, o, nv) ->
            listaFiltrada.setPredicate(p -> {
                if (nv == null || nv.isBlank()) return true;
                String f = nv.toLowerCase();
                return p.getCodigo().toLowerCase().contains(f)
                    || p.getNombre().toLowerCase().contains(f)
                    || p.getCategoria().toLowerCase().contains(f);
            }));
        tablaProductos.setItems(listaFiltrada);
        tablaProductos.getSelectionModel().selectedItemProperty().addListener(
            (obs, old, sel) -> { if (sel != null) cargarEnFormulario(sel); });

        cargarProductos();
        txtCodigo.setText("Selecciona categoría");
    }

    private void actualizarCodigo() {
        String p = getPrefijo();
        if (p == null) { txtCodigo.setText("Selecciona categoría"); return; }
        generarCodigo(p);
    }

    private String getPrefijo() {
        if (chkBombones.isSelected())     return "CHOC";
        if (chkTabletas.isSelected())     return "TABL";
        if (chkTrufas.isSelected())       return "TRUF";
        if (chkBanos.isSelected())        return "BAÑO";
        if (chkRellenos.isSelected())     return "RLLE";
        if (chkCoberturas.isSelected())   return "COBE";
        if (chkGranolas.isSelected())     return "GRAN";
        if (chkMateriaPrima.isSelected()) return "MATP";
        if (chkEmpaque.isSelected())      return "EMPA";
        return null;
    }

    private void generarCodigo(String prefijo) {
        String sql = "SELECT MAX(CAST(SUBSTRING(codigo,6,LEN(codigo)) AS INT)) AS ultimo " +
                     "FROM tbl_producto WHERE codigo LIKE ?";
        try (Connection conn = con.establecerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, prefijo + "-%");
            ResultSet rs = ps.executeQuery();
            int ultimo = rs.next() ? rs.getInt("ultimo") : 0;
            txtCodigo.setText(String.format("%s-%03d", prefijo, ultimo + 1));
        } catch (Exception e) {
            txtCodigo.setText(prefijo + "-001");
        }
    }

    @FXML
    private void guardar() {
        if (!validarCampos()) return;
        String categoria = getSeleccionados(chkBombones,chkTabletas,chkTrufas,chkBanos,
                                            chkRellenos,chkCoberturas,chkGranolas,chkMateriaPrima,chkEmpaque);
        String tipo      = getSeleccionados(chkTerminado,chkSemielaborado,chkInsumo,
                                            chkMateriaP,chkEmpaqueT,chkSubproducto);
        if (categoria.isEmpty()) { mostrarAlerta(Alert.AlertType.WARNING,"Categoría requerida","Selecciona al menos una categoría."); return; }
        if (tipo.isEmpty())      { mostrarAlerta(Alert.AlertType.WARNING,"Tipo requerido","Selecciona al menos un tipo."); return; }
        if (txtCodigo.getText().equals("Selecciona categoría")) {
            mostrarAlerta(Alert.AlertType.WARNING,"Código","Selecciona una categoría para generar el código."); return; }

        String sql = "INSERT INTO tbl_producto(codigo,nombre,precio_unitario,precio_mayor," +
                     "descripcion,unidad_medida,categoria,tipo) VALUES(?,?,?,?,?,?,?,?)";
        try (Connection conn = con.establecerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, txtCodigo.getText().trim());
            ps.setString(2, txtNombre.getText().trim());
            ps.setDouble(3, Double.parseDouble(txtPrecioUnitario.getText().trim()));
            ps.setDouble(4, Double.parseDouble(txtPrecioMayor.getText().trim()));
            ps.setString(5, txtDescripcion.getText().trim());
            ps.setString(6, cbUnidadMedida.getValue() != null ? cbUnidadMedida.getValue() : "");
            ps.setString(7, categoria);
            ps.setString(8, tipo);
            ps.executeUpdate();
            lista.add(0, new productoModelo(txtCodigo.getText().trim(), txtNombre.getText().trim(),
                Double.parseDouble(txtPrecioUnitario.getText().trim()),
                Double.parseDouble(txtPrecioMayor.getText().trim()),
                txtDescripcion.getText().trim(),
                cbUnidadMedida.getValue() != null ? cbUnidadMedida.getValue() : "",
                categoria, tipo, 0));
            mostrarAlerta(Alert.AlertType.INFORMATION,"Éxito","Producto guardado: " + txtCodigo.getText().trim());
            limpiar();
        } catch (NumberFormatException e) {
            mostrarAlerta(Alert.AlertType.WARNING,"Precios inválidos","Los precios deben ser números válidos.");
        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR,"Error al guardar", e.getMessage());
        }
    }

    @FXML
    private void fnEditar() {
        productoModelo sel = tablaProductos.getSelectionModel().getSelectedItem();
        if (sel == null) { mostrarAlerta(Alert.AlertType.WARNING,"Atención","Selecciona un producto para editar."); return; }
        if (!validarCampos()) return;
        String categoria = getSeleccionados(chkBombones,chkTabletas,chkTrufas,chkBanos,
                                            chkRellenos,chkCoberturas,chkGranolas,chkMateriaPrima,chkEmpaque);
        String tipo      = getSeleccionados(chkTerminado,chkSemielaborado,chkInsumo,
                                            chkMateriaP,chkEmpaqueT,chkSubproducto);
        if (categoria.isEmpty()) { mostrarAlerta(Alert.AlertType.WARNING,"Categoría requerida","Selecciona al menos una categoría."); return; }
        if (tipo.isEmpty())      { mostrarAlerta(Alert.AlertType.WARNING,"Tipo requerido","Selecciona al menos un tipo."); return; }

        String sql = "UPDATE tbl_producto SET nombre=?,precio_unitario=?,precio_mayor=?," +
                     "descripcion=?,unidad_medida=?,categoria=?,tipo=? WHERE codigo=?";
        try (Connection conn = con.establecerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, txtNombre.getText().trim());
            ps.setDouble(2, Double.parseDouble(txtPrecioUnitario.getText().trim()));
            ps.setDouble(3, Double.parseDouble(txtPrecioMayor.getText().trim()));
            ps.setString(4, txtDescripcion.getText().trim());
            ps.setString(5, cbUnidadMedida.getValue() != null ? cbUnidadMedida.getValue() : "");
            ps.setString(6, categoria);
            ps.setString(7, tipo);
            ps.setString(8, sel.getCodigo());
            ps.executeUpdate();
            sel.setNombre(txtNombre.getText().trim());
            sel.setPrecioUnitario(Double.parseDouble(txtPrecioUnitario.getText().trim()));
            sel.setPrecioMayor(Double.parseDouble(txtPrecioMayor.getText().trim()));
            sel.setDescripcion(txtDescripcion.getText().trim());
            sel.setUnidadMedida(cbUnidadMedida.getValue() != null ? cbUnidadMedida.getValue() : "");
            sel.setCategoria(categoria);
            sel.setTipo(tipo);
            tablaProductos.refresh();
            mostrarAlerta(Alert.AlertType.INFORMATION,"Éxito","Producto actualizado correctamente.");
            limpiar();
        } catch (NumberFormatException e) {
            mostrarAlerta(Alert.AlertType.WARNING,"Precios inválidos","Los precios deben ser números válidos.");
        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR,"Error al editar", e.getMessage());
        }
    }

    @FXML
    private void fnEliminar() {
        productoModelo sel = tablaProductos.getSelectionModel().getSelectedItem();
        if (sel == null) { mostrarAlerta(Alert.AlertType.WARNING,"Atención","Selecciona un producto para eliminar."); return; }
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setHeaderText(null);
        confirm.setContentText("¿Eliminar " + sel.getNombre() + " (" + sel.getCodigo() + ")?");
        confirm.showAndWait().ifPresent(r -> {
            if (r == ButtonType.OK) {
                try (Connection conn = con.establecerConexion();
                     PreparedStatement ps = conn.prepareStatement("DELETE FROM tbl_producto WHERE codigo=?")) {
                    ps.setString(1, sel.getCodigo());
                    ps.executeUpdate();
                    lista.remove(sel);
                    mostrarAlerta(Alert.AlertType.INFORMATION,"Éxito","Producto eliminado.");
                    limpiar();
                } catch (Exception e) {
                    mostrarAlerta(Alert.AlertType.ERROR,"Error al eliminar", e.getMessage());
                }
            }
        });
    }

    @FXML
    private void fnBuscar() {
        String cod = txtCodigo.getText().trim();
        if (cod.isEmpty() || cod.equals("Selecciona categoría")) {
            mostrarAlerta(Alert.AlertType.WARNING,"Atención","Escribe un código para buscar."); return; }
        for (productoModelo p : lista) {
            if (p.getCodigo().equalsIgnoreCase(cod)) {
                tablaProductos.getSelectionModel().select(p);
                tablaProductos.scrollTo(p);
                cargarEnFormulario(p);
                return;
            }
        }
        mostrarAlerta(Alert.AlertType.WARNING,"No encontrado","No existe producto con código " + cod + ".");
    }

    @FXML
    private void limpiar() {
        txtCodigo.setText("Selecciona categoría");
        txtNombre.clear(); txtPrecioUnitario.clear();
        txtPrecioMayor.clear(); txtDescripcion.clear();
        cbUnidadMedida.setValue(null);
        for (CheckBox cb : new CheckBox[]{chkBombones,chkTabletas,chkTrufas,chkBanos,
            chkRellenos,chkCoberturas,chkGranolas,chkMateriaPrima,chkEmpaque,
            chkTerminado,chkSemielaborado,chkInsumo,chkMateriaP,chkEmpaqueT,chkSubproducto})
            cb.setSelected(false);
        tablaProductos.getSelectionModel().clearSelection();
    }

    private void cargarProductos() {
        lista.clear();
        String sql = "SELECT codigo,nombre,precio_unitario,precio_mayor,descripcion," +
                     "unidad_medida,categoria,tipo,stock FROM tbl_producto ORDER BY codigo";
        try (Connection conn = con.establecerConexion();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) lista.add(new productoModelo(
                rs.getString("codigo"), rs.getString("nombre"),
                rs.getDouble("precio_unitario"), rs.getDouble("precio_mayor"),
                rs.getString("descripcion")   != null ? rs.getString("descripcion")   : "",
                rs.getString("unidad_medida") != null ? rs.getString("unidad_medida") : "",
                rs.getString("categoria"), rs.getString("tipo"), rs.getInt("stock")));
        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR,"Error al cargar productos", e.getMessage());
        }
    }

    private void cargarEnFormulario(productoModelo p) {
        txtCodigo.setText(p.getCodigo());
        txtNombre.setText(p.getNombre());
        txtPrecioUnitario.setText(String.valueOf(p.getPrecioUnitario()));
        txtPrecioMayor.setText(String.valueOf(p.getPrecioMayor()));
        txtDescripcion.setText(p.getDescripcion());
        cbUnidadMedida.setValue(p.getUnidadMedida());
        String cat = p.getCategoria() != null ? p.getCategoria() : "";
        chkBombones.setSelected(cat.contains("Bombones"));
        chkTabletas.setSelected(cat.contains("Tabletas"));
        chkTrufas.setSelected(cat.contains("Trufas"));
        chkBanos.setSelected(cat.contains("Baños"));
        chkRellenos.setSelected(cat.contains("Rellenos"));
        chkCoberturas.setSelected(cat.contains("Coberturas"));
        chkGranolas.setSelected(cat.contains("Granolas"));
        chkMateriaPrima.setSelected(cat.contains("Materia prima"));
        chkEmpaque.setSelected(cat.contains("Empaque / Presentación"));
        String tip = p.getTipo() != null ? p.getTipo() : "";
        chkTerminado.setSelected(tip.contains("Terminado"));
        chkSemielaborado.setSelected(tip.contains("Semielaborado"));
        chkInsumo.setSelected(tip.contains("Insumo"));
        chkMateriaP.setSelected(tip.contains("Materia prima"));
        chkEmpaqueT.setSelected(tip.contains("Empaque"));
        chkSubproducto.setSelected(tip.contains("Subproducto"));
    }

    private String getSeleccionados(CheckBox... cbs) {
        StringBuilder sb = new StringBuilder();
        for (CheckBox cb : cbs) if (cb.isSelected()) {
            if (sb.length() > 0) sb.append(", ");
            sb.append(cb.getText());
        }
        return sb.toString();
    }

    private boolean validarCampos() {
        if (txtNombre.getText().trim().isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING,"Campo requerido","El nombre es obligatorio."); return false; }
        if (txtPrecioUnitario.getText().trim().isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING,"Campo requerido","El precio unitario es obligatorio."); return false; }
        if (txtPrecioMayor.getText().trim().isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING,"Campo requerido","El precio al por mayor es obligatorio."); return false; }
        try {
            Double.parseDouble(txtPrecioUnitario.getText().trim());
            Double.parseDouble(txtPrecioMayor.getText().trim());
        } catch (NumberFormatException e) {
            mostrarAlerta(Alert.AlertType.WARNING,"Precios inválidos","Los precios deben ser números válidos."); return false; }
        return true;
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String msg) {
        Alert a = new Alert(tipo); a.setTitle(titulo); a.setHeaderText(null); a.setContentText(msg); a.showAndWait();
    }
}
