package com.example.chocolateria.Database;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML
    private ComboBox<String> comboRol;

    @FXML
    private TextField txtUsuario;

    @FXML
    private PasswordField txtPassword;

    private ObservableList<String> roles =
            FXCollections.observableArrayList(
                    "Cliente",
                    "Empleado"
            );

    @FXML
    public void initialize() {
        comboRol.setItems(roles);
    }
}