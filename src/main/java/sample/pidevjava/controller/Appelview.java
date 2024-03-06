package sample.pidevjava.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import sample.pidevjava.model.Appeloffre;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Appelview implements Initializable {

    @FXML
    private Text cvTF;

    @FXML
    private TextArea filtreTF;

    @FXML
    private Text nomTF;

    @FXML
    private Text numeroTF;

    @FXML
    private Text prenomTF;

    @FXML
    private Text prixTF;

    @FXML
    private Text scoreTF;




    public Appelview(){

    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void updateNom(String nouveauNom) {
        nomTF.setText(nouveauNom);
    }



    public void updatePrenom(String nouvelleCapacite) {
        prenomTF.setText(nouvelleCapacite);
    }

    public void updatePrix(String nouveauPrix) {
        prixTF.setText(nouveauPrix);
    }
  /*
    public void updateScore(String nouveauScore) {
        scoreTF.setText(nouveauScore);
    }
*/
    public void updateCV(String nouveauCV) {
        cvTF.setText(nouveauCV);
    }

    public void updateFiltre(String nouveauFiltre) {
        filtreTF.setText(nouveauFiltre);
    }

    public void updateScore(String n){
        scoreTF.setText(n);
    }

    public void updateNumero(String nouveauNumero) {
        numeroTF.setText(nouveauNumero);
    }





    }

