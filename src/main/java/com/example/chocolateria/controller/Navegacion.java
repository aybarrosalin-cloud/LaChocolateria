package com.example.chocolateria.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

public class Navegacion {

    // ── Mapa FXML → permiso requerido ────────────────────────────────────────
    private static final Map<String, PermisoRol.Pantalla> PERMISOS_FXML = new HashMap<>();

    static {
        PERMISOS_FXML.put("/vistasFinales/vistaOrdenCliente.fxml",             PermisoRol.Pantalla.ORDEN_CLIENTE);
        PERMISOS_FXML.put("/vistasFinales/vistaPagoVenta.fxml",                PermisoRol.Pantalla.PAGO_VENTA);
        PERMISOS_FXML.put("/vistasFinales/vistaGestionEnvios.fxml",            PermisoRol.Pantalla.GESTION_ENVIOS);
        PERMISOS_FXML.put("/vistasFinales/vistaGestionReclamos.fxml",          PermisoRol.Pantalla.GESTION_RECLAMOS);
        PERMISOS_FXML.put("/vistasFinales/vistaSolicitudDeProduccion.fxml",    PermisoRol.Pantalla.SOLICITUD_PRODUCCION);
        PERMISOS_FXML.put("/vistasFinales/vistaOrdenProduccion.fxml",          PermisoRol.Pantalla.ORDEN_PRODUCCION);
        PERMISOS_FXML.put("/vistasFinales/vistaRecepcion.fxml",                PermisoRol.Pantalla.RECEPCION);
        PERMISOS_FXML.put("/vistasFinales/vistaOrdenProveedor.fxml",           PermisoRol.Pantalla.ORDEN_PROVEEDOR);
        PERMISOS_FXML.put("/vistasFinales/vistaPagoCompra.fxml",               PermisoRol.Pantalla.PAGO_COMPRA);
        PERMISOS_FXML.put("/vistasFinales/vistaRegistroProducto.fxml",         PermisoRol.Pantalla.REG_PRODUCTO);
        PERMISOS_FXML.put("/vistasFinales/vistaRegistroDeEmpleado.fxml",       PermisoRol.Pantalla.REG_EMPLEADO);
        PERMISOS_FXML.put("/vistasFinales/vistaRegistroDeCliente.fxml",        PermisoRol.Pantalla.REG_CLIENTE);
        PERMISOS_FXML.put("/vistasFinales/vistaRegistroSuplidor.fxml",         PermisoRol.Pantalla.REG_SUPLIDOR);
        PERMISOS_FXML.put("/vistasFinales/vistaRegistroMaquinaria.fxml",       PermisoRol.Pantalla.REG_MAQUINARIA);
        PERMISOS_FXML.put("/vistasFinales/vistaMantenimientoMaquinaria.fxml",  PermisoRol.Pantalla.MANTENIMIENTO);
        PERMISOS_FXML.put("/vistasFinales/vistaGestionUsuarios.fxml",          PermisoRol.Pantalla.GESTION_USUARIOS);
        // Todas las vistas de consulta requieren permiso CONSULTAS
        PERMISOS_FXML.put("/vistasFinales/vistaConsultasGenerales.fxml",       PermisoRol.Pantalla.CONSULTAS);
        PERMISOS_FXML.put("/vistasFinales/vistaConsultaClientes.fxml",         PermisoRol.Pantalla.CONSULTAS);
        PERMISOS_FXML.put("/vistasFinales/vistaConsultaEmpleados.fxml",        PermisoRol.Pantalla.CONSULTAS);
        PERMISOS_FXML.put("/vistasFinales/vistaConsultaProductos.fxml",        PermisoRol.Pantalla.CONSULTAS);
        PERMISOS_FXML.put("/vistasFinales/vistaConsultaSuplidores.fxml",       PermisoRol.Pantalla.CONSULTAS);
        PERMISOS_FXML.put("/vistasFinales/vistaConsultaMaquinarias.fxml",      PermisoRol.Pantalla.CONSULTAS);
        PERMISOS_FXML.put("/vistasFinales/vistaConsultaEnvios.fxml",           PermisoRol.Pantalla.CONSULTAS);
        PERMISOS_FXML.put("/vistasFinales/vistaConsultaReclamos.fxml",         PermisoRol.Pantalla.CONSULTAS);
        PERMISOS_FXML.put("/vistasFinales/vistaConsultaOrdenCliente.fxml",     PermisoRol.Pantalla.CONSULTAS);
        PERMISOS_FXML.put("/vistasFinales/vistaConsultaOrdenProduccion.fxml",  PermisoRol.Pantalla.CONSULTAS);
        PERMISOS_FXML.put("/vistasFinales/vistaConsultaOrdenProveedor.fxml",   PermisoRol.Pantalla.CONSULTAS);
        PERMISOS_FXML.put("/vistasFinales/vistaConsultaPagosVenta.fxml",       PermisoRol.Pantalla.CONSULTAS);
        PERMISOS_FXML.put("/vistasFinales/vistaConsultaPagosCompra.fxml",      PermisoRol.Pantalla.CONSULTAS);
        PERMISOS_FXML.put("/vistasFinales/vistaConsultaRecepcion.fxml",        PermisoRol.Pantalla.CONSULTAS);
        PERMISOS_FXML.put("/vistasFinales/vistaConsultaSolicitudesProduccion.fxml", PermisoRol.Pantalla.CONSULTAS);
        PERMISOS_FXML.put("/vistasFinales/vistaConsultaMantenimientoMaquinaria.fxml", PermisoRol.Pantalla.CONSULTAS);
        PERMISOS_FXML.put("/vistasFinales/vistaConsultaUsuarios.fxml",         PermisoRol.Pantalla.GESTION_USUARIOS);
        // vistaInicio y vistaPrincipal no requieren permiso (null = libre)
    }

    // ── Navegacion principal ─────────────────────────────────────────────────

    public static void irA(String fxmlPath, ActionEvent event) {
        irA(fxmlPath, event, 1020, 720);
    }

    public static void irA(String fxmlPath, ActionEvent event, double w, double h) {
        // 1. Verificar permiso de rol
        if (!tienePermiso(fxmlPath)) {
            Alert alerta = new Alert(Alert.AlertType.WARNING);
            alerta.setTitle("Acceso denegado");
            alerta.setHeaderText(null);
            alerta.setContentText("No tienes permisos para acceder a esta sección.\n"
                    + "Rol actual: " + SesionManager.getInstancia().getRol());
            alerta.showAndWait();
            return;
        }

        // 2. Para vistas de consulta, exigir clave de administrador
        if (esVistaConsulta(fxmlPath)) {
            if (!VerificarClave.pedirClaveAdmin()) {
                Alert alerta = new Alert(Alert.AlertType.WARNING);
                alerta.setTitle("Clave incorrecta");
                alerta.setHeaderText(null);
                alerta.setContentText("Clave incorrecta o cancelada.\nSolo los administradores pueden acceder a las consultas.");
                alerta.showAndWait();
                return;
            }
        }

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        try {
            FXMLLoader loader = new FXMLLoader(Navegacion.class.getResource(fxmlPath));
            Parent root = loader.load();
            stage.setScene(new Scene(root, w, h));
            stage.show();
        } catch (Exception ex) {
            ex.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error de navegación");
            alert.setHeaderText(null);
            alert.setContentText("No se pudo abrir la pantalla:\n" + ex.getMessage());
            alert.showAndWait();
        }
    }

    public static void salir(ActionEvent event) {
        SesionManager.getInstancia().cerrarSesion();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        try {
            FXMLLoader loader = new FXMLLoader(Navegacion.class.getResource("/vistasFinales/vistaPrincipal.fxml"));
            Parent root = loader.load();
            stage.setScene(new Scene(root, 949, 533));
            stage.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // ── Verificacion de permisos ─────────────────────────────────────────────

    private static boolean tienePermiso(String fxmlPath) {
        PermisoRol.Pantalla pantallaRequerida = PERMISOS_FXML.get(fxmlPath);
        // Si la pantalla no está en el mapa, es libre (inicio, principal, etc.)
        if (pantallaRequerida == null) return true;
        String rol = SesionManager.getInstancia().getRol();
        return PermisoRol.tieneAcceso(rol, pantallaRequerida);
    }

    /**
     * Devuelve true si el FXML es una vista de consulta histórica individual (protegida con clave).
     * Excluye vistaConsultasGenerales.fxml (hub general) para no pedir clave en cada "Volver".
     * Solo aplica a las vistas específicas: vistaConsultaClientes, vistaConsultaEmpleados, etc.
     */
    private static boolean esVistaConsulta(String fxmlPath) {
        String nombre = fxmlPath.toLowerCase();
        return nombre.contains("/vistaconsulta") && !nombre.contains("consultasgenerales");
    }
}
