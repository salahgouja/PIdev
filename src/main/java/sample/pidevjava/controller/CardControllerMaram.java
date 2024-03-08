package sample.pidevjava.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import sample.pidevjava.model.Partenaire;
import sample.pidevjava.model.Reponse;
import sample.pidevjava.controller.PartenaireService;
import sample.pidevjava.controller.ReponseService;

import java.io.IOException;
import java.sql.SQLException;

public class CardControllerMaram {
    @FXML
    private Label email;

    @FXML
    private Label montant;

    @FXML
    private Label nom;

    @FXML
    private Label prenom;

    @FXML
    private Label quantite;

    @FXML
    private Label tel;

    @FXML
    private Label typee;
    @FXML
    private Label etat;

    @FXML
    private Label typep;
    @FXML
    private Button acc;
    @FXML
    private Button rej;

    private Partenaire currentEvent;
    PartenaireService ser = new PartenaireService();
    public static String reponse = "";


    public void setEventData(Partenaire event) {
        if (event != null) {
            currentEvent = event;
            nom.setText(event.getNom());
            prenom.setText(event.getPrenom());
            tel.setText(String.valueOf(event.getNum_tel()));
            email.setText(event.getEmail());
            typep.setText(event.getType_partenaire());
            montant.setText(String.valueOf(event.getMontant()) + " DNT");
            typee.setText(event.getType_equipement());
            quantite.setText(String.valueOf(event.getQuantite()));
            if(event.getEtat() == 0){
                etat.setText("En cours");
            }
            if(event.getEtat() == 1){
                etat.setText("Acceptée");
            }
            if(event.getEtat() == -1){
                etat.setText("Refusée");
            }
        } else {
            // Handle null event
            clearLabels();
        }
    }

    private void clearLabels() {
        nom.setText("");
        prenom.setText("");
        tel.setText("");
        email.setText("");
        typep.setText("");
        montant.setText("");
        typee.setText("");
        quantite.setText("");
        etat.setText("");
    }
    @FXML
    private void handleAcceptButton(ActionEvent event) throws SQLException {
        // Implement the action to be performed when "Accept" button is clicked
        if (currentEvent != null) {
            currentEvent.setEtat(1);
            ser.modifier(currentEvent);
        }
    }

    @FXML
    private void handleRejectButton(ActionEvent event) throws SQLException, IOException {
        // Implement the action to be performed when "Reject" button is clicked
        // Load the response dialog FXML file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/sample/pidevjava/reponseDialog.fxml"));
        AnchorPane pane = loader.load();

        // Create the dialog stage
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Add Response");
        Scene scene = new Scene(pane);
        dialogStage.setScene(scene);

        // Set the controller and initialize the dialog stage
        ReponseDialogController controller = loader.getController();
        controller.setDialogStage(dialogStage);

        // Show the dialog and wait for user response
        dialogStage.showAndWait();

        // Check if the response was saved
        if (controller.isResponseSaved()) {
            if (currentEvent != null) {
                currentEvent.setEtat(-1);
                ser.modifier(currentEvent);
                ReponseService repp = new ReponseService();
                Reponse r = new Reponse(reponse,currentEvent.getId_partenaire());
                repp.ajouter(r);
            }
        }

    }
}