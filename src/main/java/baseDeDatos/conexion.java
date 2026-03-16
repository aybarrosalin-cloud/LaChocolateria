package baseDeDatos;

import javax.swing.*;
import java.sql.*;

public class conexion {
    Connection connection = null;

    String usuario = "dba";
    String contrase = "YaraConexion";
    String db = "agenda";
    String server = "26.136.71.206";
    String puerto = "1433";
    String cadena = "jdbc:sqlserver://" + server + "." + puerto + "/" + db;

    public Connection establecerConexion() {
        try {
            String cadena = "jdbc:sqlserver://" + server + ":" + puerto + ";"
                    + "databaseName=" + db + ";" + "encrypt=true" + ";" + "trustServerCertificate=true";
            connection = DriverManager.getConnection(cadena, usuario, contrase);
            JOptionPane.showMessageDialog(null, "se conecto a la bd");

            // imprime el error
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "error en la conexion a la bd" + e.toString());
        }
        return connection;
    }
}