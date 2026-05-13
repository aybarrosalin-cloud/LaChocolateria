package com.example.chocolateria.controller;

import com.example.chocolateria.baseDeDatos.conexion;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Optional;

/**
 * Dialogo de verificacion de clave de administrador.
 * Se usa para proteger el acceso a las vistas de consulta historica.
 */
public class VerificarClave {

    /**
     * Muestra el dialogo de clave y devuelve true si el usuario
     * ingreso una contrasena valida de algun Administrador o Gerente General.
     */
    public static boolean pedirClaveAdmin() {
        // Usamos TextInputDialog como base (probado y estable en el proyecto)
        // pero reemplazamos su contenido con un PasswordField oculto
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Acceso a Consulta");
        dialog.setHeaderText(null);

        // ── PasswordField personalizado ───────────────────────────────────────
        PasswordField pfClave = new PasswordField();
        pfClave.setPromptText("Ingresa la contraseña de administrador");
        pfClave.setStyle("-fx-background-color:white; -fx-border-color:#9b59b6; "
                + "-fx-border-radius:6; -fx-background-radius:6; -fx-font-size:12px;");
        pfClave.setPrefHeight(34);

        // ── Contenido ────────────────────────────────────────────────────────
        VBox contenido = new VBox(10);
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

        contenido.getChildren().addAll(titulo, lblSub, lblCampo, pfClave);

        // Reemplaza el contenido del dialog con nuestro VBox
        dialog.getDialogPane().setContent(contenido);
        dialog.getDialogPane().setStyle("-fx-background-color:#f3eaf8;");

        // Estilo de los botones (lookup seguro con null-check)
        javafx.scene.Node btnOk     = dialog.getDialogPane().lookupButton(ButtonType.OK);
        javafx.scene.Node btnCancel = dialog.getDialogPane().lookupButton(ButtonType.CANCEL);
        if (btnOk     != null) btnOk.setStyle("-fx-background-color:#6d3c87; -fx-text-fill:white; -fx-font-weight:bold; -fx-background-radius:8;");
        if (btnCancel != null) btnCancel.setStyle("-fx-background-color:#a83c5b; -fx-text-fill:white; -fx-font-weight:bold; -fx-background-radius:8;");

        // CRÍTICO: TextInputDialog deshabilita OK cuando su TextField interno está vacío.
        // Como reemplazamos el contenido, el TextField interno siempre está vacío → OK queda gris.
        // Desconectamos ese binding y lo enlazamos a nuestro PasswordField.
        if (btnOk != null) {
            btnOk.disableProperty().unbind();
            btnOk.disableProperty().bind(pfClave.textProperty().isEmpty());
        }

        // Enter en el PasswordField dispara OK
        if (btnOk instanceof javafx.scene.control.Button) {
            pfClave.setOnAction(e -> ((javafx.scene.control.Button) btnOk).fire());
        }

        // Mostrar y esperar
        // showAndWait() de TextInputDialog retorna Optional.empty() si el usuario canceló
        Optional<String> resultado = dialog.showAndWait();
        if (resultado.isEmpty()) return false; // Canceló o cerró con X

        // Leer la clave directamente del PasswordField (no del TextField interno del dialog)
        String clave = pfClave.getText();
        if (clave == null || clave.isBlank()) return false;

        return verificarEnDB(clave.trim());
    }

    // ── Verificacion en BD ────────────────────────────────────────────────────

    private static boolean verificarEnDB(String clave) {
        conexion con = new conexion();
        String sql = "SELECT COUNT(*) FROM tbl_usuario "
                + "WHERE password = ? AND rol IN ('Administrador','Gerente General') AND estado = 'Activo'";
        try (Connection conn = con.establecerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, clave);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
