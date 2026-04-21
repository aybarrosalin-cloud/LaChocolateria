package com.example.chocolateria.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.image.ImageView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import static com.example.chocolateria.controller.PermisoRol.Pantalla.*;

public class inicioController {

    // ── Sidebar: paneles colapsables ──────────────────────────────────────────
    @FXML private TitledPane paneVentas;
    @FXML private TitledPane paneProduccion;
    @FXML private TitledPane paneInventario;
    @FXML private TitledPane paneCompras;
    @FXML private TitledPane paneRegistros;
    @FXML private TitledPane paneReportes;
    @FXML private TitledPane paneMantenimiento;
    @FXML private TitledPane paneConsultas;
    @FXML private TitledPane paneAdministracion;

    // ── Sidebar: botones del menu ─────────────────────────────────────────────
    @FXML private Button btnOrdenCliente;
    @FXML private Button btnPagoVenta;
    @FXML private Button btnGestionEnvios;
    @FXML private Button btnGestionReclamos;
    @FXML private Button btnSolicitudProduccion;
    @FXML private Button btnOrdenProduccion;
    @FXML private Button btnSalidaMateriales;
    @FXML private Button btnSalidaProductos;
    @FXML private Button btnOrdenProveedor;
    @FXML private Button btnPagoCompra;
    @FXML private Button btnRegProducto;
    @FXML private Button btnRegEmpleado;
    @FXML private Button btnRegCliente;
    @FXML private Button btnRegSuplidor;
    @FXML private Button btnRegMaquinaria;
    @FXML private Button btnMantenimientoMaquinaria;

    // ── Cards del dashboard ───────────────────────────────────────────────────
    @FXML private Button cardVentas;
    @FXML private Button cardProduccion;
    @FXML private Button cardInventario;
    @FXML private Button cardCompras;
    @FXML private Button cardRegistros;
    @FXML private Button cardReportes;

    // ── Header ────────────────────────────────────────────────────────────────
    @FXML private Label     lblFecha;
    @FXML private Label     lblUsuario;
    @FXML private ImageView imgFotoPerfil;

    @FXML
    public void initialize() {
        CargarPerfil.aplicar(lblUsuario, imgFotoPerfil);
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("EEEE, d 'de' MMMM 'de' yyyy", new Locale("es", "ES"));
        lblFecha.setText(LocalDate.now().format(fmt));
        aplicarPermisos();
    }

    // ── Logica de permisos ────────────────────────────────────────────────────
    private void aplicarPermisos() {
        String rol = SesionManager.getInstancia().getRol();

        // Ventas
        ocultar(btnOrdenCliente,    !PermisoRol.tieneAcceso(rol, ORDEN_CLIENTE));
        ocultar(btnPagoVenta,       !PermisoRol.tieneAcceso(rol, PAGO_VENTA));
        ocultar(btnGestionEnvios,   !PermisoRol.tieneAcceso(rol, GESTION_ENVIOS));
        ocultar(btnGestionReclamos, !PermisoRol.tieneAcceso(rol, GESTION_RECLAMOS));
        ocultarSiVacio(paneVentas,
            btnOrdenCliente, btnPagoVenta, btnGestionEnvios, btnGestionReclamos);

        // Produccion
        ocultar(btnSolicitudProduccion, !PermisoRol.tieneAcceso(rol, SOLICITUD_PRODUCCION));
        ocultar(btnOrdenProduccion,     !PermisoRol.tieneAcceso(rol, ORDEN_PRODUCCION));
        ocultarSiVacio(paneProduccion, btnSolicitudProduccion, btnOrdenProduccion);

        // Inventario
        boolean verRecepcion = PermisoRol.tieneAcceso(rol, RECEPCION);
        ocultar(btnSalidaMateriales, !verRecepcion);
        ocultar(btnSalidaProductos,  !verRecepcion);
        ocultarSiVacio(paneInventario, btnSalidaMateriales, btnSalidaProductos);

        // Compras
        ocultar(btnOrdenProveedor, !PermisoRol.tieneAcceso(rol, ORDEN_PROVEEDOR));
        ocultar(btnPagoCompra,     !PermisoRol.tieneAcceso(rol, PAGO_COMPRA));
        ocultarSiVacio(paneCompras, btnOrdenProveedor, btnPagoCompra);

        // Registros
        ocultar(btnRegProducto,   !PermisoRol.tieneAcceso(rol, REG_PRODUCTO));
        ocultar(btnRegEmpleado,   !PermisoRol.tieneAcceso(rol, REG_EMPLEADO));
        ocultar(btnRegCliente,    !PermisoRol.tieneAcceso(rol, REG_CLIENTE));
        ocultar(btnRegSuplidor,   !PermisoRol.tieneAcceso(rol, REG_SUPLIDOR));
        ocultar(btnRegMaquinaria, !PermisoRol.tieneAcceso(rol, REG_MAQUINARIA));
        ocultarSiVacio(paneRegistros,
            btnRegProducto, btnRegEmpleado, btnRegCliente, btnRegSuplidor, btnRegMaquinaria);

        // Reportes (todos van a consultas, se muestran si tiene acceso a consultas)
        ocultar(paneReportes, !PermisoRol.tieneAcceso(rol, CONSULTAS));

        // Mantenimiento
        ocultar(btnMantenimientoMaquinaria, !PermisoRol.tieneAcceso(rol, MANTENIMIENTO));
        ocultarSiVacio(paneMantenimiento, btnMantenimientoMaquinaria);

        // Consultas
        ocultar(paneConsultas, !PermisoRol.tieneAcceso(rol, CONSULTAS));

        // Administracion
        ocultar(paneAdministracion, !PermisoRol.tieneAcceso(rol, GESTION_USUARIOS));

        // Cards del dashboard
        ocultar(cardVentas,    !tieneAlguno(rol, ORDEN_CLIENTE, PAGO_VENTA, GESTION_ENVIOS, GESTION_RECLAMOS));
        ocultar(cardProduccion,!tieneAlguno(rol, SOLICITUD_PRODUCCION, ORDEN_PRODUCCION));
        ocultar(cardInventario,!PermisoRol.tieneAcceso(rol, RECEPCION));
        ocultar(cardCompras,   !tieneAlguno(rol, ORDEN_PROVEEDOR, PAGO_COMPRA));
        ocultar(cardRegistros, !tieneAlguno(rol, REG_PRODUCTO, REG_EMPLEADO, REG_CLIENTE, REG_SUPLIDOR, REG_MAQUINARIA));
        ocultar(cardReportes,  !PermisoRol.tieneAcceso(rol, CONSULTAS));
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private void ocultar(Node nodo, boolean condicion) {
        if (nodo == null) return;
        nodo.setVisible(!condicion);
        nodo.setManaged(!condicion);
    }

    /** Oculta el TitledPane si ninguno de sus botones es visible */
    private void ocultarSiVacio(TitledPane pane, Button... botones) {
        if (pane == null) return;
        boolean alguno = false;
        for (Button b : botones) {
            if (b != null && b.isManaged()) { alguno = true; break; }
        }
        ocultar(pane, !alguno);
    }

    private boolean tieneAlguno(String rol, PermisoRol.Pantalla... pantallas) {
        for (PermisoRol.Pantalla p : pantallas) {
            if (PermisoRol.tieneAcceso(rol, p)) return true;
        }
        return false;
    }

    // ── Navegacion ────────────────────────────────────────────────────────────
    @FXML private void irAInicio(ActionEvent e) {}

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

    @FXML private void cardVentas(ActionEvent e)     { Navegacion.irA("/vistasFinales/vistaOrdenCliente.fxml", e); }
    @FXML private void cardProduccion(ActionEvent e) { Navegacion.irA("/vistasFinales/vistaSolicitudDeProduccion.fxml", e); }
    @FXML private void cardInventario(ActionEvent e) { Navegacion.irA("/vistasFinales/vistaRecepcion.fxml", e); }
    @FXML private void cardCompras(ActionEvent e)    { Navegacion.irA("/vistasFinales/vistaOrdenProveedor.fxml", e); }
    @FXML private void cardRegistros(ActionEvent e)  { Navegacion.irA("/vistasFinales/vistaRegistroDeCliente.fxml", e); }
    @FXML private void cardReportes(ActionEvent e)   { Navegacion.irA("/vistasFinales/vistaConsultas.fxml", e); }
}
