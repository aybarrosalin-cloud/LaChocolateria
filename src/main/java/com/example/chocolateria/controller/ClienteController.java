package com.example.chocolateria.controller;

import com.example.chocolateria.Database.Conexion;
import com.example.chocolateria.modelo.Cliente;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class ClienteController {


    @FXML private TextField txtNombre;
    @FXML private TextField txtApellido;
    @FXML private TextField txtEmail;
    @FXML private TextField txtTelefono;

    //TABLA
    @FXML private TableView<Cliente> tablaClientes;
    @FXML private TableColumn<Cliente, String> colNombre;
    @FXML private TableColumn<Cliente, String> colApellido;
    @FXML private TableColumn<Cliente, String> colEmail;
    @FXML private TableColumn<Cliente, String> colTelefono;

    private final Conexion conexion = new Conexion();

    // LISTA O SEA EL ARRAY
    private final ObservableList<Cliente> listaClientes =
            FXCollections.observableArrayList();

    //SE EJECUTA AL ABRIR EL FORMULARIO
    @FXML
    public void initialize() {
        colNombre.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(d.getValue().getNombre()));
        colApellido.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(d.getValue().getApellido()));
        colEmail.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(d.getValue().getEmail()));
        colTelefono.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(d.getValue().getTelefono()));

        tablaClientes.setItems(listaClientes);
    }


    @FXML
    private void registrarCliente() {

        String nombre = txtNombre.getText();
        String apellido = txtApellido.getText();
        String correo = txtEmail.getText();
        String telefono = txtTelefono.getText();


        conexion.insertarCliente(nombre, apellido, correo, telefono);

        //  ARRAY
        Cliente cliente = new Cliente(nombre, apellido, correo, telefono);
        listaClientes.add(cliente);

        // TRES MENSAJES
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Éxito");
        alert.setHeaderText(null);
        alert.setContentText("Cliente registrado correctamente");
        alert.showAndWait();

        // LIMPIAR CAMPOS
        txtNombre.clear();
        txtApellido.clear();
        txtEmail.clear();
        txtTelefono.clear();
    }
}