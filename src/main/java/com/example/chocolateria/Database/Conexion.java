package com.example.chocolateria.Database;

import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Conexion {

    private Connection connection;

    private final String usuario = "usuario_normal";
    private final String contrase = "Normal123*";
    private final String db = "lachoco";
    private final String server = "localhost";
    private final String puerto = "1433";

    private final String cadena =
            "jdbc:sqlserver://" + server + ":" + puerto + ";" +
                    "databaseName=" + db + ";" +
                    "encrypt=false;" +
                    "trustServerCertificate=true;";

    public Connection establecerConexion() {
        try {
            connection = DriverManager.getConnection(cadena, usuario, contrase);
            JOptionPane.showMessageDialog(null, "Conexión exitosa");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error de conexión:\n" + e.getMessage());
        }
        return connection;
    }

    // INSERT CON DATOS
    public void insertarCliente(String nombre, String apellido, String email, String telefono) {

        String sql = "INSERT INTO tbl_cliente (nombre, apellido, email, telefono) VALUES (?,?,?,?)";

        try (Connection con = establecerConexion();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setString(1, nombre);
            pstmt.setString(2, apellido);
            pstmt.setString(3, email);
            pstmt.setString(4, telefono);

            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}