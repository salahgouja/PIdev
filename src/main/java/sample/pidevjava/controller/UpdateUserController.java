package sample.pidevjava.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

public class UpdateUserController {
    public PasswordField password;
    public Label lblpassword;
    @FXML
    private TextField firstname;
    @FXML
    private TextField lastname;
    @FXML
    private TextField phone;
    @FXML
    private TextField email;
    @FXML
    private Button update_user;
    @FXML
    private BorderPane root2;

    public void add_user(ActionEvent actionEvent) {
    }

    public void annuler(ActionEvent actionEvent) {
    }
}
