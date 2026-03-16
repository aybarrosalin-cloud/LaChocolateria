package controller;

import baseDeDatos.conexion;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javax.swing.*;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class suplidorController implements Initializable {
    conexion conexion = new conexion();
    //formulario
    @FXML
    private TextField txtNombre;

    @FXML
    private TextField txtRNC;

    @FXML
    private TextField txtCel;

    @FXML
    private TextField txtCorreo;

    @FXML
    private ComboBox<String> cmbCiudad;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        actualizarDatos();
    }

    public ObservableList llenaCombo() {
        ObservableList<String> ciudad = FXCollections.observableArrayList();
        Statement stmt;

        String sql = "Select nombre from ciudad";
        conexion conexion = new conexion();
        try (Connection con = conexion.establecerConexion();
             PreparedStatement preparedStatement = conexion.establecerConexion().prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery();
        ) {
            while (resultSet.next()) {
                ciudad.add(resultSet.getString("nombre"));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);

        }
        return ciudad;
    }

    @FXML
    private void guardarSuplidor(ActionEvent event) {

        try {

            String nombre = txtNombre.getText();
            String rnc = txtRNC.getText();
            String telefono = txtCel.getText();
            String correo = txtCorreo.getText();
            String ciudad = cmbCiudad.getValue();

            conexion conexion = new conexion();
            Connection con = conexion.establecerConexion();

            String sql = "INSERT INTO suplidor (nombre, rnc, telefono, correo, ciudad) VALUES (?, ?, ?, ?, ?)";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, nombre);
            ps.setString(2, rnc);
            ps.setString(3, telefono);
            ps.setString(4, correo);
            ps.setString(5, ciudad);
            ps.executeUpdate();
            System.out.println(cmbCiudad.getValue().toString());
            System.out.println("Suplidor guardado en BD");

            limpiarCampos();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void cargarDesdeBD() {

        listaObservable.clear();

        try {
            // conexion conexion = new conexion();
            Connection con = conexion.establecerConexion();

            String sql = "SELECT * FROM suplidor";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                suplidorModelo s = new suplidorModelo(
                        rs.getString("nombre"),
                        rs.getString("rnc"),
                        rs.getString("telefono"),
                        rs.getString("correo"),
                        rs.getString("ciudad")
                );

                listaObservable.add(s);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }0
            .

    ObservableList<String> Ciudad = FXCollections.observableArrayList();

    public void MostrarCiudad(Event event) {
        cmbCiudad.setItems(llenaCombo());
        //System.out.println(cmbCiudad.getValue().toString());

        //esto es para que seleccione el que esta de primero como por default
        cmbCiudad.getSelectionModel().selectFirst();
//        cmbCiudad.valueProperty().addListener((ObservableValue observable, Object valorantiguo, Object nuevo)) -> ;
    }

    //esto es para consultar, para qu evaya a la bd, busque esa info y me la traiga
    public void fnbuscar(ActionEvent actionEvent) {
        String Nombre = this.txtNombre.getText().trim();
        String sql = "SELECT * FROM suplidor WHERE nombre='" + Nombre + "'";
        buscarDatos(sql); //para buscar la informacion en la base de datos
    }

    public void fnEditar(ActionEvent actionEvent){
        int idSpulidor=parseInt(this.txt)
        String Nombre=(this.txtNombre.getText().trim());
        String RNC=(this.txtRNC.getText().trim());
        String Telefono=(this.txtCel.getText().trim());
        String Correo=(this.txtCorreo.getText().trim());

        String sql="update Suplidor set nombre ='" + Nombre + "',RNC='" + RNC + "',Telefono='" + Telefono +
                "',correo='" + Correo + "'where idSuplidor='" + idSpulidor + "'";
        System.out.println(sql);
        EjecutarSQL(sql);
        actualizarDatos();
    }

    public void actualizarDatos() {
        // tener las columnas con el modelo
        colNombre.setCellValueFactory(data -> data.getValue().nombreProperty());
        colRnc.setCellValueFactory(data -> data.getValue().rncProperty());
        colTelefono.setCellValueFactory(data -> data.getValue().telefonoProperty());
        colCorreo.setCellValueFactory(data -> data.getValue().correoProperty());
        colCiudad.setCellValueFactory(data -> data.getValue().ciudadProperty());

        // conectar tabla con la lista
        tablaSuplidores.setItems(listaObservable);

        cargarDesdeBD();
    }

    public void EjecutarSQL(String sql){
        try {
            Connection con = conexion.establecerConexion();
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            int result = preparedStatement.executeUpdate();
            if (result == 1) {
                JOptionPane.showMessageDialog(null, "Accion realizada correctamente");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "error" + e.toString());
        }
    }

    private void buscarDatos(String sql) {

        try {
            conexion conexion = new conexion();
            Connection con = conexion.establecerConexion();

            PreparedStatement preparedStatement = con.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {

                txtNombre.setText(resultSet.getString("nombre"));
                txtRNC.setText(resultSet.getString("rnc"));
                txtCel.setText(resultSet.getString("telefono"));
                txtCorreo.setText(resultSet.getString("correo"));
                String ciudad=resultSet.getString("ciudad");
                this.cmbCiudad.getSelectionModel().select(ciudad);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void fnlimpiar(ActionEvent actionEvent){
        limpiarCampos();
    }

    @FXML
    private void limpiarCampos() {
        txtNombre.clear();
        txtRNC.clear();
        txtCel.clear();
        txtCorreo.clear();
        cmbCiudad.getSelectionModel().selectFirst();
    }

    public void insertarDatos() {
        String sql = "INSERT INTO persona (nombre, apellido, direccion, telefono, email, status)" +
                "VALUES (?,?,?,?,?,?)";

        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, "caroline");
            pstmt.setString(2, "herrera");
            pstmt.setString(3, "caracas venezuela");
            pstmt.setString(4, "8095670048");
            pstmt.setString(5, "carolinaherrera@gmail.com");
            pstmt.setString(6, "activo");

            //ejecutar la insercion
            int filasInsertadas = pstmt.executeUpdate();
            System.out.println("filas insertadas: *" + filasInsertadas);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al insertar en la bd" + e.toString());
        }
    }

    public void borrarDatos(int id) {
        String query = "delete from persona where idpersona=?";

        try {
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, id);
            int filasborrada = pstmt.executeUpdate();
            System.out.println("Registro borrado: " + filasborrada);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "error al borrar los datos" + e.toString());
        }
    }

    public void actualizarDatos(int id) {
        String query = "Update Persona set direccion = ? where idPersona = 1";

        try {
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setString(1, "caracas");

            int filaactualizada = pstmt.executeUpdate();
            System.out.println("Fila actualizada: " + filaactualizada);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al actualizar el dato" + e.toString());
        }
    }

    public void leerDatos() {

        String sql = "select * from persona";
        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next())
            {
                System.out.println("Codigo: " + rs.getInt("idpersona"));
                System.out.println("Nombre: " + rs.getString("nombre"));
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al leer/mostrar datos" + e.toString());
        }
    }
}
}
