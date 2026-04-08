package com.example.chocolateria.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class inicioController {

    @FXML private Label lblFecha;
    @FXML private Label lblUsuario;
    @FXML private ImageView imgFotoPerfil;

    @FXML
    public void initialize() {
        CargarPerfil.aplicar(lblUsuario, imgFotoPerfil);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, d 'de' MMMM 'de' yyyy", new Locale("es", "ES"));
        lblFecha.setText(LocalDate.now().format(formatter));
    }

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

    @FXML private void salir(ActionEvent e)                  { Navegacion.irA("/vistasFinales/vistaPrincipal.fxml", e, 949, 533); }

    @FXML private void cardVentas(ActionEvent e)             { Navegacion.irA("/vistasFinales/vistaOrdenCliente.fxml", e); }
    @FXML private void cardProduccion(ActionEvent e)         { Navegacion.irA("/vistasFinales/vistaSolicitudDeProduccion.fxml", e); }
    @FXML private void cardInventario(ActionEvent e)         { Navegacion.irA("/vistasFinales/vistaRecepcion.fxml", e); }
    @FXML private void cardCompras(ActionEvent e)            { Navegacion.irA("/vistasFinales/vistaOrdenProveedor.fxml", e); }
    @FXML private void cardRegistros(ActionEvent e)          { Navegacion.irA("/vistasFinales/vistaRegistroDeCliente.fxml", e); }
    @FXML private void cardReportes(ActionEvent e)           { Navegacion.irA("/vistasFinales/vistaConsultas.fxml", e); }
}