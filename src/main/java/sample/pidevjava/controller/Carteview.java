package sample.pidevjava.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class Carteview extends AnchorPane implements Initializable {

    @FXML
    private TextField nomTF;

    @FXML
    private TextField capacitetf;

    @FXML
    private TextField typetf;

    @FXML
    private TextField etattf;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void updateNom(String nouveauNom) {
        nomTF.setText(nouveauNom);
    }

    public void updateCapacite(String nouvelleCapacite) {
        capacitetf.setText(nouvelleCapacite);
    }

    public void updateType(String nouveauType) {
        typetf.setText(nouveauType);
    }

    public void updateEtat(String nouveauEtat) {
        etattf.setText(nouveauEtat);
    }
}