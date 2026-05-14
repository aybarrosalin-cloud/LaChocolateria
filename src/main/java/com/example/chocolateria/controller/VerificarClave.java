package com.example.chocolateria.controller;

import com.example.chocolateria.baseDeDatos.conexion;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.concurrent.atomic.AtomicBoolean;

// dialogo de verificacion de clave de administrador
// corre en hilo de fondo para no congelar la ui
public class VerificarClave {

    public static boolean pedirClaveAdmin() {
        AtomicBoolean verificado = new AtomicBoolean(false);

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Acceso a Consulta");
        dialog.setHeaderText(null);

        // campo de contrasena
        PasswordField pfClave = new PasswordField();
        pfClave.setPromptText("Contraseña de administrador");
        pfClave.setStyle("-fx-background-color:white; -fx-border-color:#9b59b6; "
                + "-fx-border-radius:6; -fx-background-radius:6; -fx-font-size:12px;");
        pfClave.setPrefHeight(34);

        // mensaje de error o estado
        Label lblError = new Label(" ");
        lblError.setStyle("-fx-text-fill:#a83c5b; -fx-font-size:11px; -fx-font-weight:bold;");

        // contenido
        VBox contenido = new VBox(8);
        contenido.setPadding(new Insets(16, 24, 8, 24));
        contenido.setAlignment(Pos.CENTER_LEFT);

        HBox titulo = new HBox(8);
        titulo.setAlignment(Pos.CENTER_LEFT);
        Label icono = new Label("🔒");
        icono.setFont(Font.font(20));
        Label lblTitulo = new Label("Área protegida");
        lblTitulo.setStyle("-fx-text-fill:#3B1A5C; -fx-font-size:14px; -fx-font-weight:bold;");
        titulo.getChildren().addAll(icono, lblTitulo);

        Label lblSub = new Label("Ingresa la clave de un Administrador o Gerente General.");
        lblSub.setStyle("-fx-text-fill:#6d3c87; -fx-font-size:11px;");
        lblSub.setWrapText(true);

        Label lblCampo = new Label("Contraseña:");
        lblCampo.setStyle("-fx-text-fill:#3B1A5C; -fx-font-weight:bold; -fx-font-size:11px;");

        contenido.getChildren().addAll(titulo, lblSub, lblCampo, pfClave, lblError);

        dialog.getDialogPane().setContent(contenido);
        dialog.getDialogPane().setStyle("-fx-background-color:#f3eaf8;");

        // botones
        javafx.scene.Node nodeOk     = dialog.getDialogPane().lookupButton(ButtonType.OK);
        javafx.scene.Node nodeCancel = dialog.getDialogPane().lookupButton(ButtonType.CANCEL);
        if (nodeOk     != null) nodeOk.setStyle("-fx-background-color:#6d3c87; -fx-text-fill:white; -fx-font-weight:bold; -fx-background-radius:8;");
        if (nodeCancel != null) nodeCancel.setStyle("-fx-background-color:#a83c5b; -fx-text-fill:white; -fx-font-weight:bold; -fx-background-radius:8;");

        if (nodeOk instanceof Button) {
            Button okBtn = (Button) nodeOk;

            // deshabilitar ok mientras este vacio
            okBtn.disableProperty().unbind();
            okBtn.disableProperty().bind(pfClave.textProperty().isEmpty());

            // enter dispara ok
            pfClave.setOnAction(e -> okBtn.fire());

            // interceptar ok: verificar en hilo de fondo
            okBtn.addEventFilter(javafx.event.ActionEvent.ACTION, event -> {
                event.consume(); // evitar que se cierre solo

                String clave = pfClave.getText().trim();
                if (clave.isBlank()) return;

                // deshabilitar mientras verifica
                okBtn.disableProperty().unbind();
                okBtn.setDisable(true);
                pfClave.setDisable(true);
                lblError.setStyle("-fx-text-fill:#6d3c87; -fx-font-size:11px;");
                lblError.setText("Verificando...");

                Thread t = new Thread(() -> {
                    boolean ok = verificarEnDB(clave);
                    Platform.runLater(() -> {
                        if (ok) {
                            verificado.set(true);
                            dialog.close(); // Cierra el dialog con éxito
                        } else {
                            // mostrar error y permitir reintentar
                            pfClave.setDisable(false);
                            pfClave.clear();
                            pfClave.requestFocus();
                            okBtn.disableProperty().bind(pfClave.textProperty().isEmpty());
                            lblError.setStyle("-fx-text-fill:#a83c5b; -fx-font-size:11px; -fx-font-weight:bold;");
                            lblError.setText("❌ Contraseña incorrecta. Intenta de nuevo.");
                        }
                    });
                });
                t.setDaemon(true);
                t.start();
            });
        }

        dialog.showAndWait(); // Espera hasta que el dialog se cierre (Cancel o éxito)
        return verificado.get();
    }

    // verificacion en bd (en hilo de fondo)

    private static boolean verificarEnDB(String clave) {
        conexion con = new conexion();
        String sql = "SELECT password FROM tbl_usuario "
                + "WHERE rol IN ('Administrador','Gerente General') AND estado = 'Activo'";
        try (Connection conn = con.establecerConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String hashAlmacenado = rs.getString("password");
                if (CifradoUtil.verificarPassword(clave, hashAlmacenado)) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
